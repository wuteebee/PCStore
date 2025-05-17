
package GUI.Panel;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

import BUS.SupplierBUS;
import DAO.SupplierDAO;
import DTO.Supplier;
import GUI.Main;
import GUI.Components.MenuChucNang;
import GUI.Dialog.ThemNhaCungCap;

public class SupplierPanel extends JPanel {
    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private List<Supplier> suppliers;
    private SupplierBUS supplierBUS;
    private SupplierDAO supplierDAO;
    private DefaultTableModel tableModel;
    private JTable supplierTable;
    private String selectedSupplierId = "-1";
    private Main mainFrame;

    public SupplierPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.supplierBUS = new SupplierBUS();
        this.supplierDAO = new SupplierDAO();
        initComponents();
        addTableSelectionListener();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);

        add(createCustomToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    public String getSelectedSupplierId() {
        return selectedSupplierId;
    }

    public void openAddSupplierDialog() {
        ThemNhaCungCap dialog = new ThemNhaCungCap(new SupplierBUS(), this);
        dialog.FormThemNCC("Thêm nhà cung cấp", "Thêm", null);
        loadSupplierTable();
    }

    public void openEditSupplierDialog(String id) {
        Supplier supplier = supplierDAO.getSupplierById(id);
        if (supplier != null) {
            ThemNhaCungCap dialog = new ThemNhaCungCap(new SupplierBUS(), this);
            dialog.FormThemNCC("Chỉnh sửa nhà cung cấp", "Cập nhật", supplier);
            loadSupplierTable();
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openRemoveSupplierDialog(String id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SupplierBUS bus = new SupplierBUS();
            bus.deleteSupplier(id);
            loadSupplierTable();
        }
    }

    public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));

        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));

        JPanel searchPanel = MenuChucNang.createSearchPanel();

        // Lấy các thành phần từ searchPanel
        for (Component comp : searchPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if ("Tìm kiếm".equals(btn.getText())) {
                    btnTimKiem = btn;
                } else if ("Làm mới".equals(btn.getText())) {
                    btnLamMoi = btn;
                }
            } else if (comp instanceof JTextField) {
                txtTimKiem = (JTextField) comp;
            }
        }

        if (btnTimKiem != null) btnTimKiem.addActionListener(e -> performSearch());
        if (btnLamMoi != null) btnLamMoi.addActionListener(e -> refreshTable());

        toolbar.add(searchPanel);
        return toolbar;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnNames = { "ID", "Tên nhà cung cấp", "Số điện thoại", "Email", "Địa chỉ" };
        tableModel = new DefaultTableModel(columnNames, 0);
        supplierTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTableHeader header = supplierTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < columnNames.length; i++) {
            supplierTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        supplierTable.setShowGrid(false);
        supplierTable.setRowHeight(30);
        supplierTable.setSelectionBackground(new Color(173, 216, 230));
        supplierTable.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        panel.add(scrollPane, BorderLayout.CENTER);
        loadSupplierTable();

        return panel;
    }

    private void addTableSelectionListener() {
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = supplierTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedSupplierId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }

    public void loadSupplierTable() {
        tableModel.setRowCount(0);
        suppliers = supplierBUS.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            Object[] row = {
                supplier.getId(),
                supplier.getName(),
                supplier.getPhoneNumber(),
                supplier.getEmail(),
                supplier.getAddress()
            };
            tableModel.addRow(row);
        }
    }

    private void performSearch() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        List<Supplier> allSuppliers = supplierBUS.getAllSuppliers();
        List<Supplier> result = new ArrayList<>();

        for (Supplier s : allSuppliers) {
            if (s.getId().toLowerCase().contains(keyword) ||
                s.getName().toLowerCase().contains(keyword) ||
                s.getAddress().toLowerCase().contains(keyword) ||
                s.getPhoneNumber().toLowerCase().contains(keyword) ||
                s.getEmail().toLowerCase().contains(keyword)) {
                result.add(s);
            }
        }

        loadTableData(result);
    }

    private void refreshTable() {
        txtTimKiem.setText("");
        loadTableData(supplierBUS.getAllSuppliers());
    }

    private void loadTableData(List<Supplier> suppliers) {
        tableModel.setRowCount(0);
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getName(),
                s.getPhoneNumber(),
                s.getEmail(),
                s.getAddress(),
                s.getTrangThai() ? "Đang hoạt động" : "Ngưng hoạt động"
            });
        }
    }
}
