package GUI.Panel;

import java.awt.*;
import java.util.List;
import java.util.Date;

import javax.swing.*;
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

    public PhieuNhapPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.bus = new PhieuNhapBUS();
        initComponent();
        addTableSelectionListener();
    }

  

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);

        add(createCustomToolbar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

     
    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout(10, 0));
        mainContent.setBackground(Color.WHITE);

        JPanel leftPanel = createFill();
        JPanel rightPanel = createTablePanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0);

        mainContent.add(splitPane, BorderLayout.CENTER);
        return mainContent;
    }

    private JPanel createFill() {
        JPanel fillPanel = new JPanel(new GridBagLayout());
        fillPanel.setBackground(Color.WHITE);
        fillPanel.setPreferredSize(new Dimension(300, 0));
        fillPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font font = new Font("Segoe UI", Font.PLAIN, 13);
        Dimension fieldSize = new Dimension(200, 28);

        // Nhà cung cấp
        fillPanel.add(new JLabel("Nhà cung cấp:"), gbc);
        gbc.gridy++;
        supplierComboBox = new JComboBox<>();
        SupplierBUS ncc = new SupplierBUS();
        List<Supplier> dsncc = ncc.getAllSuppliers();
        supplierComboBox.addItem("Tất cả");
        for (Supplier supplier : dsncc) {
            supplierComboBox.addItem(supplier.getName());
        }
        supplierComboBox.setPreferredSize(fieldSize);
        supplierComboBox.setFont(font);
        fillPanel.add(supplierComboBox, gbc);

        // Nhân viên
        gbc.gridy++;
        fillPanel.add(new JLabel("Nhân viên:"), gbc);
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
        employeeComboBox.setFont(font);
        fillPanel.add(employeeComboBox, gbc);

        // Từ ngày
        gbc.gridy++;
        fillPanel.add(new JLabel("Từ ngày:"), gbc);
        gbc.gridy++;
        fromDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("dd/MM/yyyy");
        fromDateChooser.setPreferredSize(fieldSize);
        fromDateChooser.setFont(font);
        fillPanel.add(fromDateChooser, gbc);

        // Đến ngày
        gbc.gridy++;
        fillPanel.add(new JLabel("Đến ngày:"), gbc);
        gbc.gridy++;
        toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("dd/MM/yyyy");
        toDateChooser.setPreferredSize(fieldSize);
        toDateChooser.setFont(font);
        fillPanel.add(toDateChooser, gbc);

        // Sự kiện thay đổi
        supplierComboBox.addActionListener(e -> refreshTable());
        employeeComboBox.addActionListener(e -> refreshTable());
        fromDateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) refreshTable();
        });
        toDateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) refreshTable();
        });

        return fillPanel;
    }

    private JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(980, 90));

        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));
        toolbar.add(MenuChucNang.createSearchPanel());

        return toolbar;
    }


    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnNames = { "Mã phiếu nhập", "Nhân viên", "Nhà cung cấp", "Ngày tạo", "Tổng tiền" };
        tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 28));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshTable();

        return panel;
    }

    public String getSelectedPhieuId() {
        return selectedPhieuId;
    }

    public void addTableSelectionListener() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedPhieuId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        String selectedSupplier = (String) supplierComboBox.getSelectedItem();
        String selectedEmployee = (String) employeeComboBox.getSelectedItem();
        Date fromDate = fromDateChooser.getDate();
        Date toDate = toDateChooser.getDate();

        List<HoaDonNhap> danhSach = bus.getAllPhieuNhap();

        for (HoaDonNhap p : danhSach) {
            boolean matches = true;

            if (!"Tất cả".equals(selectedSupplier) && !p.getNhaCungCap().getName().equals(selectedSupplier)) {
                matches = false;
            }
            if (!"Tất cả".equals(selectedEmployee) && !p.getNhanVien().getName().equals(selectedEmployee)) {
                matches = false;
            }
            if (fromDate != null && p.getNgayTao().before(fromDate)) {
                matches = false;
            }
            if (toDate != null && p.getNgayTao().after(toDate)) {
                matches = false;
            }

            if (matches) {
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
    }


    public boolean deleteHoaDon(){
        boolean deleted=true;
        int selectedRow = table.getSelectedRow(); 
        String idPhieuNhap="";
        if (selectedRow != -1) { 
            idPhieuNhap = table.getValueAt(selectedRow, 0).toString(); 
            System.out.println("ID Phiếu Nhập: " + idPhieuNhap);
        }
        // Kiểm tra từng sản phẩm trong phiếu nhập có thuộc phiếu xuất nào kkh
        // lấy các chitietsp dựa vào mã phiếu nhập
        PhieuNhapBUS phieuNhapBUS=new PhieuNhapBUS();
        if (phieuNhapBUS.ktraXuatHang(idPhieuNhap)) {
    JOptionPane.showMessageDialog(null, 
        "Phiếu nhập này đã có sản phẩm được xuất kho, không thể xoá!",
        "Thông báo",
        JOptionPane.WARNING_MESSAGE);
    return false; 
}


    
      deleted= phieuNhapBUS.deleteFull(idPhieuNhap);
        return deleted;
    }

    public boolean fixHoadon(){
        boolean fix=true;
        int selectedRow = table.getSelectedRow(); 
        String idPhieuNhap="";
        if (selectedRow != -1) { 
            idPhieuNhap = table.getValueAt(selectedRow, 0).toString(); 
            System.out.println("ID Phiếu Nhập: " + idPhieuNhap);
        }
        // Kiểm tra từng sản phẩm trong phiếu nhập có thuộc phiếu xuất nào kkh
        // lấy các chitietsp dựa vào mã phiếu nhập
        PhieuNhapBUS phieuNhapBUS=new PhieuNhapBUS();
        if (phieuNhapBUS.ktraXuatHang(idPhieuNhap)) {
    JOptionPane.showMessageDialog(null, 
        "Phiếu nhập này đã có sản phẩm được xuất kho, không thể chỉnh sửa!",
        "Thông báo",
        JOptionPane.WARNING_MESSAGE);
    return false; 


        
    }

    mainFrame.setMainPanel(new NhapHoaDonPanel(mainFrame, true));
    return fix;

}
}