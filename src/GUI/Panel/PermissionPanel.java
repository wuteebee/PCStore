package GUI.Panel;

import BUS.PermissionBUS;
import DTO.PermissionGroup;
import GUI.Components.MenuChucNang;
import GUI.Dialog.AddPermissionGroupDialog;
import GUI.Dialog.ViewPermissionGroupDialog;
import GUI.Main;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PermissionPanel extends JPanel {
    private JTable tableNhomQuyen;
    private DefaultTableModel tableModel;
    private JPanel tablePanel;
    private PermissionBUS permissionBUS;
    private Main mainFrame;
    private JTextField searchField;
    private JButton btnSearch;
    private JButton btnReset;

    public PermissionPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        System.out.println("Khởi tạo PermissionPanel");
        try {
            permissionBUS = new PermissionBUS();
            setPreferredSize(new Dimension(1080, 770));
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            initComponents();
        } catch (Exception e) {
            System.err.println("Lỗi khởi tạo PermissionPanel: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu nhóm quyền do lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            initEmptyPanel();
        }
    }

    private void initEmptyPanel() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel errorLabel = new JLabel("Không thể tải dữ liệu nhóm quyền. Vui lòng kiểm tra cơ sở dữ liệu!", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(errorLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initComponents() {
        // Thanh công cụ
        JPanel topPanel = createToolbar();

        // Bảng nhóm quyền
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        String[] columnNames = {"Mã nhóm quyền", "Tên nhóm quyền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableNhomQuyen = new JTable(tableModel);
        tableNhomQuyen.setPreferredScrollableViewportSize(new Dimension(930, 525));
        tableNhomQuyen.getTableHeader().setReorderingAllowed(false);

        // Thiết lập header bảng
        JTableHeader header = tableNhomQuyen.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableNhomQuyen.getColumnCount(); i++) {
            tableNhomQuyen.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thiết lập bảng
        tableNhomQuyen.setShowGrid(false);
        tableNhomQuyen.setRowHeight(30);
        tableNhomQuyen.setSelectionBackground(new Color(173, 216, 230));
        tableNhomQuyen.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tableNhomQuyen);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Tải dữ liệu
        loadData();
    }

    public JPanel createToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));

        // Panel hành động
        MenuChucNang menu = new MenuChucNang();
        JPanel actionPanel = menu.createActionPanel(this, mainFrame);
        toolbar.add(actionPanel);

        // Panel tìm kiếm
        JPanel searchPanel = MenuChucNang.createSearchPanel();
        toolbar.add(searchPanel);

        // Lấy các thành phần tìm kiếm
        Component[] components = searchPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("Tìm kiếm")) {
                    btnSearch = button;
                } else if (button.getText().equals("Làm mới")) {
                    btnReset = button;
                }
            } else if (comp instanceof JTextField) {
                searchField = (JTextField) comp;
            }
        }

        // Gán sự kiện tìm kiếm và làm mới
        btnSearch.addActionListener(e -> searchPermissionGroups());
        btnReset.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });

        return toolbar;
    }

    private void searchPermissionGroups() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        List<PermissionGroup> groups = permissionBUS.getAllPermissionGroups().stream()
                .filter(p -> p.getIdNhomQuyen().toLowerCase().contains(keyword.toLowerCase()) ||
                             p.getTenNhomQuyen().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        tableModel.setRowCount(0);
        for (PermissionGroup p : groups) {
            tableModel.addRow(new Object[]{
                p.getIdNhomQuyen(),
                p.getTenNhomQuyen()
            });
        }
    }

    public void openAddPermissionGroupDialog() {
        AddPermissionGroupDialog.showAddPermissionGroupDialog(mainFrame, null);
        loadData();
    }

    public void openEditPermissionGroupDialog(String id) {
        PermissionGroup group = permissionBUS.getPermissionGroupById(id);
        if (group != null) {
            AddPermissionGroupDialog.showAddPermissionGroupDialog(mainFrame, group);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhóm quyền với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openViewPermissionGroupDialog(String id) {
        PermissionGroup group = permissionBUS.getPermissionGroupById(id);
        if (group != null) {
            ViewPermissionGroupDialog.showViewPermissionGroupDialog(mainFrame, group);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhóm quyền với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openRemovePermissionGroupDialog(String id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhóm quyền này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (permissionBUS.deletePermissionGroup(id)) {
                JOptionPane.showMessageDialog(this, "Xóa nhóm quyền thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa nhóm quyền thất bại!");
            }
        }
    }

    public String getSelectedPermissionGroupId() {
        int row = tableNhomQuyen.getSelectedRow();
        if (row >= 0) {
            return tableModel.getValueAt(row, 0).toString();
        }
        return "-1";
    }

    public void loadData() {
        try {
            tableModel.setRowCount(0);
            List<PermissionGroup> groups = permissionBUS.getAllPermissionGroups();
            System.out.println("Đã tải " + groups.size() + " nhóm quyền");
            for (PermissionGroup p : groups) {
                tableModel.addRow(new Object[]{
                    p.getIdNhomQuyen(),
                    p.getTenNhomQuyen()
                });
            }
        } catch (Exception e) {
            System.err.println("Lỗi tải nhóm quyền: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách nhóm quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g);
        g2.dispose();
    }
}