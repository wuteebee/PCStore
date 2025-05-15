package GUI.Panel;

import BUS.EmployeeBUS;
import BUS.InvoiceBUS;
import DAO.EmployeeDAO;
import DTO.Employee;
import DTO.SalesInvoice;
import GUI.Main;
import GUI.Components.MenuChucNang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class SaleInvoicePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String selectedPhieuId = "-1";
    private Main mainFrame;
    private InvoiceBUS bus;
    private JComboBox<String> employeeComboBox;
    private JComboBox<String> customerComboBox;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;

    public SaleInvoicePanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.bus = new InvoiceBUS();
        initComponent();
        addTableSelectionListener();
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

    private JPanel createFill(){
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
        String[] columnNames = {"ID Hóa đơn", "ID Nhân viên", "ID Khách hàng", "Ngày tạo", "Tổng tiền", "ID Khuyến mãi"};
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

public void refreshTable() {
    tableModel.setRowCount(0);

    String selectedEmployee = (String) employeeComboBox.getSelectedItem();
    // String selectedCustomer = (String) customerComboBox.getSelectedItem();
    Date fromDate = fromDateChooser.getDate();
    Date toDate = toDateChooser.getDate();

    List<SalesInvoice> danhSach = bus.fetchSalesInvoice();
    // EmployeeDAO employeeDAO=new EmployeeDAO();
    // EmployeeBUS employeeBUS=new EmployeeBUS(employeeDAO);

    for (SalesInvoice p : danhSach) {
        boolean matches = true;

        if (!"Tất cả".equals(selectedEmployee) && !p.getNhanVien().getName().equals(selectedEmployee)) {
            matches = false;
        }
        // if (!"Tất cả".equals(selectedCustomer) && !p.getKhachhang().getName().equals(selectedCustomer)) {
        //     matches = false;
        // }
        if (fromDate != null && p.getDate().before(fromDate)) {
            matches = false;
        }
        if (toDate != null && p.getDate().after(toDate)) {
            matches = false;
        }

        if (matches) {
            Object[] row = {
                p.getId(),
                p.getNhanVien().getName(),
                p.getKhachhang().getName(),
                p.getDate(),
                String.format("%,.0f VND", p.getTotalPayment()),
                p.getKhuyenmai().getTenKhuyenMai()
            };
            tableModel.addRow(row);
        }
    }
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

    public String getSelectedPhieuId() {
        return selectedPhieuId;
    }
}
