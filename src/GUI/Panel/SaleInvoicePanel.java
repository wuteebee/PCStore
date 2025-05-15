package GUI.Panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import BUS.CustomerBUS;
import BUS.EmployeeBUS;
import BUS.InvoiceBUS;
import DAO.EmployeeDAO;
import DTO.Customer;
import DTO.Employee;
import DTO.SalesInvoice;
import GUI.Main;
import GUI.Components.MenuChucNang;

public class SaleInvoicePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String selectedPhieuId = "-1";
    private Main mainFrame;
    private InvoiceBUS bus;

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private EmployeeBUS employeeBUS = new EmployeeBUS(employeeDAO);
    private CustomerBUS customerBUS = new CustomerBUS();
    public SaleInvoicePanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.bus = new InvoiceBUS();
        initComponent();
        addTableSelectionListener();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);

        add(createCustomToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
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

        String[] columns = {"ID Hóa đơn xuất", "Tên nhân viên", "Tên khách hàng", "Ngày tạo", "Tổng tiền", "ID Khuyến mãi"};
        tableModel = new DefaultTableModel(columns, 0);

        List<SalesInvoice> danhSach = bus.fetchSalesInvoice();
        for (SalesInvoice p : danhSach) {
            Object[] row = {
                    p.getId(),
                    findEmployeeName(p.getEid()),
                    findCustomerName(p.getCid()),
                    p.getDate(),
                    String.format("%,.0f VND", p.getTotalPayment()),
                    p.getDid() == null ? "Không áp dụng" : p.getDid()
            };
            tableModel.addRow(row);
        }

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
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
            System.out.println(selectedPhieuId);
        });
    }

    public void addRowToTable(List <SalesInvoice> list) {
        tableModel.setRowCount(0);
        for (SalesInvoice p : list) {
            Object[] row = {
                    p.getId(),
                    findEmployeeName(p.getEid()),
                    findCustomerName(p.getCid()),
                    p.getDate(),
                    String.format("%,.0f VND", p.getTotalPayment()),
                    p.getDid() == null ? "Không áp dụng" : p.getDid()
            };
            tableModel.addRow(row);
        }
    }


    public String findEmployeeName(String eid) {
        List<Employee> employeeList = employeeBUS.getAllEmployees();
        for (Employee e : employeeList) {
            if (e.getId().equals(eid))
                return e.getName();
        }
        return "";
    }

    public String findCustomerName(String cid)
    {
        List<Customer> customerList = customerBUS.getAllCustomers();
        for (Customer e : customerList) {
            if (e.getId().equals(cid))
                return e.getName();
        }
        return "";
    }
}
