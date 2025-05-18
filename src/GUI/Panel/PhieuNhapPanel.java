
package GUI.Panel;

import java.awt.*;
import java.text.Normalizer;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;
import BUS.EmployeeBUS;
import BUS.PhieuNhapBUS;
import BUS.SupplierBUS;
import DAO.EmployeeDAO;
import DAO.SupplierDAO;
import DTO.Employee;
import DTO.HoaDonNhap;
import DTO.Supplier;
import GUI.Main;
import GUI.Components.MenuChucNang;

public class PhieuNhapPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String selectedPhieuId = "-1";
    private Main mainFrame;
    private PhieuNhapBUS bus;
    private JComboBox<String> supplierComboBox;
    private JComboBox<String> employeeComboBox;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JTextField searchField;
    private JButton btnSearch;
    private JButton btnReset;
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 14);

    public PhieuNhapPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.bus = new PhieuNhapBUS();
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponent();
        addTableSelectionListener();
    }

    private void initComponent() {
        add(createCustomToolbar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout(10, 0));
        mainContent.setBackground(Color.WHITE);

        JPanel leftPanel = createFilterPanel();
        JPanel rightPanel = createTablePanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0);

        mainContent.add(splitPane, BorderLayout.CENTER);
        return mainContent;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setPreferredSize(new Dimension(300, 0));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Dimension fieldSize = new Dimension(200, 30);

        JLabel lblSupplier = new JLabel("Nhà cung cấp:");
        lblSupplier.setFont(LABEL_FONT);
        filterPanel.add(lblSupplier, gbc);
        gbc.gridy++;
        supplierComboBox = new JComboBox<>();
        SupplierBUS ncc = new SupplierBUS();
        List<Supplier> dsncc = ncc.getAllSuppliers();
        supplierComboBox.addItem("Tất cả");
        for (Supplier supplier : dsncc) {
            supplierComboBox.addItem(supplier.getName());
        }
        supplierComboBox.setPreferredSize(fieldSize);
        supplierComboBox.setFont(LABEL_FONT);
        filterPanel.add(supplierComboBox, gbc);

        gbc.gridy++;
        JLabel lblEmployee = new JLabel("Nhân viên:");
        lblEmployee.setFont(LABEL_FONT);
        filterPanel.add(lblEmployee, gbc);
        gbc.gridy++;
        employeeComboBox = new JComboBox<>();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        EmployeeBUS employeeBUS = new EmployeeBUS(employeeDAO);
        List<Employee> dsnv = employeeBUS.getAllEmployees();
        employeeComboBox.addItem("Tất cả");
        for (Employee employee : dsnv) {
            employeeComboBox.addItem(employee.getName());
        }
        employeeComboBox.setPreferredSize(fieldSize);
        employeeComboBox.setFont(LABEL_FONT);
        filterPanel.add(employeeComboBox, gbc);

        gbc.gridy++;
        JLabel lblFromDate = new JLabel("Từ ngày:");
        lblFromDate.setFont(LABEL_FONT);
        filterPanel.add(lblFromDate, gbc);
        gbc.gridy++;
        fromDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("dd/MM/yyyy");
        fromDateChooser.setPreferredSize(fieldSize);
        fromDateChooser.setFont(LABEL_FONT);
        filterPanel.add(fromDateChooser, gbc);

        gbc.gridy++;
        JLabel lblToDate = new JLabel("Đến ngày:");
        lblToDate.setFont(LABEL_FONT);
        filterPanel.add(lblToDate, gbc);
        gbc.gridy++;
        toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("dd/MM/yyyy");
        toDateChooser.setPreferredSize(fieldSize);
        toDateChooser.setFont(LABEL_FONT);
        filterPanel.add(toDateChooser, gbc);

        supplierComboBox.addActionListener(e -> refreshTable());
        employeeComboBox.addActionListener(e -> refreshTable());
        fromDateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) refreshTable();
        });
        toDateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) refreshTable();
        });

        return filterPanel;
    }

    private JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));

        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));

        JPanel searchPanel = MenuChucNang.createSearchPanel();
        searchPanel.setBackground(Color.WHITE);

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

        btnSearch.addActionListener(e -> searchPhieuNhap());
        btnReset.addActionListener(e -> {
            searchField.setText("");
            supplierComboBox.setSelectedIndex(0);
            employeeComboBox.setSelectedIndex(0);
            fromDateChooser.setDate(null);
            toDateChooser.setDate(null);
            refreshTable();
        });

        toolbar.add(searchPanel);
        return toolbar;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã phiếu nhập", "Nhân viên", "Nhà cung cấp", "Ngày tạo", "Tổng tiền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshTable();
        return panel;
    }

    private void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setFont(HEADER_FONT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setShowGrid(false);
        table.setRowHeight(30);
        table.setFont(LABEL_FONT);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
    }

    private String removeDiacritics(String str) {
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }

    private void searchPhieuNhap() {
        String keyword = removeDiacritics(searchField.getText().trim().toLowerCase());
        tableModel.setRowCount(0);
        List<HoaDonNhap> danhSach = bus.getAllPhieuNhap();

        List<HoaDonNhap> filteredList = new ArrayList<>();
        for (HoaDonNhap p : danhSach) {
            String id = removeDiacritics(p.getIdHoaDonNhap().toLowerCase());
            String employeeName = removeDiacritics(p.getNhanVien().getName().toLowerCase());
            String supplierName = removeDiacritics(p.getNhaCungCap().getName().toLowerCase());
            String dateStr = removeDiacritics(p.getNgayTao().toString().toLowerCase());
            String totalStr = removeDiacritics(String.format("%,.0f VND", p.getTongTien()).toLowerCase());

            if (keyword.isEmpty() || id.contains(keyword) || employeeName.contains(keyword) ||
                supplierName.contains(keyword) || dateStr.contains(keyword) ||
                totalStr.contains(keyword)) {
                if (matchesFilters(p)) {
                    filteredList.add(p);
                }
            }
        }

        Collections.sort(filteredList, (p1, p2) -> {
            String id1 = p1.getIdHoaDonNhap();
            String id2 = p2.getIdHoaDonNhap();
            try {
                int num1 = Integer.parseInt(id1);
                int num2 = Integer.parseInt(id2);
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                return id1.compareTo(id2);
            }
        });

        for (HoaDonNhap p : filteredList) {
            Object[] row = {
                p.getIdHoaDonNhap(),
                p.getNhanVien().getName(),
                p.getNhaCungCap().getName(),
                p.getNgayTao(),
                String.format("%,.0f VND", p.getTongTien())
            };
            tableModel.addRow(row);
        }
    }

    private boolean matchesFilters(HoaDonNhap p) {
        String selectedSupplier = (String) supplierComboBox.getSelectedItem();
        String selectedEmployee = (String) employeeComboBox.getSelectedItem();
        Date fromDate = fromDateChooser.getDate();
        Date toDate = toDateChooser.getDate();

        if (!"Tất cả".equals(selectedSupplier) && !p.getNhaCungCap().getName().equals(selectedSupplier)) {
            return false;
        }
        if (!"Tất cả".equals(selectedEmployee) && !p.getNhanVien().getName().equals(selectedEmployee)) {
            return false;
        }
        if (fromDate != null && p.getNgayTao().before(fromDate)) {
            return false;
        }
        if (toDate != null && p.getNgayTao().after(toDate)) {
            return false;
        }
        return true;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<HoaDonNhap> danhSach = bus.getAllPhieuNhap();

        List<HoaDonNhap> filteredList = new ArrayList<>();
        for (HoaDonNhap p : danhSach) {
            if (matchesFilters(p)) {
                filteredList.add(p);
            }
        }

        Collections.sort(filteredList, (p1, p2) -> {
            String id1 = p1.getIdHoaDonNhap();
            String id2 = p2.getIdHoaDonNhap();
            try {
                int num1 = Integer.parseInt(id1);
                int num2 = Integer.parseInt(id2);
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                return id1.compareTo(id2);
            }
        });

        for (HoaDonNhap p : filteredList) {
            Object[] row = {
                p.getIdHoaDonNhap(),
                p.getNhanVien().getName(),
                p.getNhaCungCap().getName(),
                p.getNgayTao(),
                String.format("%,.0f VND", p.getTongTien())
            };
            tableModel.addRow(row);
        }
    }

    public String getSelectedPhieuId() {
        return selectedPhieuId;
    }

    private void addTableSelectionListener() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedPhieuId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }

    public boolean deleteHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String idPhieuNhap = table.getValueAt(selectedRow, 0).toString();
        PhieuNhapBUS phieuNhapBUS = new PhieuNhapBUS();

        if (phieuNhapBUS.ktraXuatHang(idPhieuNhap)) {
            JOptionPane.showMessageDialog(this, "Phiếu nhập này đã có sản phẩm được xuất kho, không thể xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        boolean deleted = phieuNhapBUS.deleteFull(idPhieuNhap);
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Xóa phiếu nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa phiếu nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return deleted;
    }

    public boolean fixHoadon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String idPhieuNhap = table.getValueAt(selectedRow, 0).toString();
        PhieuNhapBUS phieuNhapBUS = new PhieuNhapBUS();

        if (phieuNhapBUS.ktraXuatHang(idPhieuNhap)) {
            JOptionPane.showMessageDialog(this, "Phiếu nhập này đã có sản phẩm được xuất kho, không thể chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        mainFrame.setMainPanel(new NhapHoaDonPanel(mainFrame, true));
        return true;
    }
}
