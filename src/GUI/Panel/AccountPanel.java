package GUI.Panel;

import BUS.AccountBUS;
import DTO.Account;
import GUI.Components.MenuChucNang;
import GUI.Dialog.AccountDetailDialog;
import GUI.Dialog.ThemTaiKhoanDialog;
import GUI.Main;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AccountPanel extends JPanel {
    private JTable tableTaiKhoan;
    private DefaultTableModel tableModel;
    private JPanel tablePanel;
    private AccountBUS accountBUS;
    private Main mainFrame;
    private JTextField searchField;
    private JButton btnSearch;
    private JButton btnReset;

    public AccountPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        try {
            accountBUS = new AccountBUS();
            setPreferredSize(new Dimension(1080, 770));
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            initComponents();
        } catch (Exception e) {
            System.err.println("Failed to initialize AccountPanel: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu tài khoản do lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            initEmptyPanel();
        }
    }

    private void initEmptyPanel() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel errorLabel = new JLabel("Không thể tải dữ liệu tài khoản. Vui lòng kiểm tra cơ sở dữ liệu!", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(errorLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initComponents() {
        JPanel topPanel = createAccountToolbar();
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        String[] columnNames = {"Mã TK", "Tên NV", "Nhóm Quyền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTaiKhoan = new JTable(tableModel);
        tableTaiKhoan.setPreferredScrollableViewportSize(new Dimension(930, 525));
        tableTaiKhoan.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tableTaiKhoan.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableTaiKhoan.getColumnCount(); i++) {
            tableTaiKhoan.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableTaiKhoan.setShowGrid(false);
        tableTaiKhoan.setRowHeight(30);
        tableTaiKhoan.setSelectionBackground(new Color(173, 216, 230));
        tableTaiKhoan.setSelectionForeground(Color.BLACK);

        // Loại bỏ sự kiện nhấp đúp
        // tableTaiKhoan.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         if (e.getClickCount() == 2) {
        //             int row = tableTaiKhoan.rowAtPoint(e.getPoint());
        //             if (row >= 0) {
        //                 String idTaiKhoan = tableModel.getValueAt(row, 0).toString();
        //                 new AccountDetailDialog(AccountPanel.this, idTaiKhoan).setVisible(true);
        //             }
        //         }
        //     }
        // });

        JScrollPane scrollPane = new JScrollPane(tableTaiKhoan);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        loadData();
    }

    public JPanel createAccountToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));

        MenuChucNang menu = new MenuChucNang();
        JPanel actionPanel = menu.createActionPanel(this, mainFrame);
        toolbar.add(actionPanel);

        JPanel searchPanel = MenuChucNang.createSearchPanel();
        toolbar.add(searchPanel);

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

        btnSearch.addActionListener(e -> searchAccounts());
        btnReset.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });

        return toolbar;
    }

    private void searchAccounts() {
        String keyword = searchField.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        for (Account a : accountBUS.getAllAccounts()) {
            String employeeName = accountBUS.getEmployeeName(a.getIdNhanVien());
            String groupName = accountBUS.getPermissionGroupName(a.getIdNhomQuyen());
            if (a.getIdTaiKhoan().toLowerCase().contains(keyword.toLowerCase()) ||
                employeeName.toLowerCase().contains(keyword.toLowerCase()) ||
                groupName.toLowerCase().contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    a.getIdTaiKhoan(),
                    employeeName,
                    groupName
                });
            }
        }
    }

    public void openAddAccountDialog() {
        if (accountBUS.getAvailableEmployees().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tồn tại nhân viên chưa có tài khoản!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new ThemTaiKhoanDialog(this, null).setVisible(true);
    }

    public void openEditAccountDialog(String id) {
        Account a = accountBUS.getAllAccounts().stream()
                .filter(acc -> acc.getIdTaiKhoan().equals(id)).findFirst().orElse(null);
        if (a != null) {
            new ThemTaiKhoanDialog(this, a).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openRemoveAccountDialog(String id) {
        if (accountBUS.deleteAccount(id)) {
            JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa tài khoản thất bại!");
        }
    }

    public String getSelectedAccountId() {
        int row = tableTaiKhoan.getSelectedRow();
        if (row >= 0) {
            return tableModel.getValueAt(row, 0).toString();
        }
        return "-1";
    }

    public void loadData() {
        try {
            tableModel.setRowCount(0);
            for (Account a : accountBUS.getAllAccounts()) {
                tableModel.addRow(new Object[]{
                    a.getIdTaiKhoan(),
                    accountBUS.getEmployeeName(a.getIdNhanVien()),
                    accountBUS.getPermissionGroupName(a.getIdNhomQuyen())
                });
            }
        } catch (Exception e) {
            System.err.println("Error loading accounts: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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