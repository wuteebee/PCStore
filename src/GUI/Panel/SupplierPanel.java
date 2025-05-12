package GUI.Panel;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import com.toedter.calendar.JDateChooser;

import BUS.SupplierBUS;
import DAO.SupplierDAO;
import DTO.Supplier;
import GUI.Main;
import GUI.Components.MenuChucNang;
import GUI.Dialog.ThemNhaCungCap;
import java.util.List;

public class SupplierPanel extends JPanel {
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

        // Tạo panel trung tâm chứa bộ lọc và bảng
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.setBackground(Color.WHITE);

        // centerPanel.add(createFilterPanel(), BorderLayout.WEST);
        // centerPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
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
        toolbar.add(MenuChucNang.createSearchPanel());

        return toolbar;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300, 500));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Nhà cung cấp
        JLabel supplierLabel = new JLabel("Nhà cung cấp:");
        JComboBox<String> supplierCombo = new JComboBox<>(new String[]{"-- Tất cả --", "Công ty A", "Công ty B", "Công ty C"});

        // Nhân viên nhập
        JLabel staffLabel = new JLabel("Nhân viên nhập:");
        JComboBox<String> staffCombo = new JComboBox<>(new String[]{"-- Tất cả --", "Nguyễn Văn A", "Trần Thị B"});

        // Từ ngày - Đến ngày
        JLabel fromDateLabel = new JLabel("Từ ngày:");
        JDateChooser fromDate = new JDateChooser();
        fromDate.setDate(new Date());

        JLabel toDateLabel = new JLabel("Đến ngày:");
        JDateChooser toDate = new JDateChooser();
        toDate.setDate(new Date());

        // Từ số tiền - Đến số tiền
        JLabel minAmountLabel = new JLabel("Từ số tiền:");
        JTextField minAmountField = new JTextField();

        JLabel maxAmountLabel = new JLabel("Đến số tiền:");
        JTextField maxAmountField = new JTextField();

        // Button Lọc
        JButton filterButton = new JButton("Lọc");

        panel.add(supplierLabel);
        panel.add(supplierCombo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(staffLabel);
        panel.add(staffCombo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(fromDateLabel);
        panel.add(fromDate);
        panel.add(Box.createVerticalStrut(10));
        panel.add(toDateLabel);
        panel.add(toDate);
        panel.add(Box.createVerticalStrut(10));
        panel.add(minAmountLabel);
        panel.add(minAmountField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(maxAmountLabel);
        panel.add(maxAmountField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(filterButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã phiếu nhập", "Nhà cung cấp", "Nhân viên nhập", "Thời gian", "Tổng tiền"};
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

        // Dữ liệu giả lập phiếu nhập
        Object[][] fakeData = {
            {"PN001", "Công ty A", "Nguyễn Văn A", "2024-05-01", "5,000,000"},
            {"PN002", "Công ty B", "Trần Thị B", "2024-05-03", "7,200,000"},
            {"PN003", "Công ty C", "Nguyễn Văn A", "2024-05-05", "4,000,000"},
            {"PN004", "Công ty A", "Trần Thị B", "2024-05-07", "9,500,000"}
        };

        for (Object[] row : fakeData) {
            tableModel.addRow(row);
        }
    }
}
