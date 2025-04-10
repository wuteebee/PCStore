package GUI.Panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import BUS.CustomerBUS;
import DAO.CustomerDAO;
import DAO.EmployeeDAO;
import DTO.Customer;
import DTO.Employee;
import GUI.Main;
import GUI.Components.MenuChucNang;
import GUI.Dialog.ThemKhachHang;
import GUI.Dialog.ThemNhanVien;

public class CustomerPanel extends JPanel{
    private List <Customer> customers;
    private CustomerBUS customerBUS;
    private CustomerDAO customerDAO;
    private DefaultTableModel tableModel;
    private JTable customerTable;
    private String selectedCustomerId="-1";
    private Main mainFrame;

    public CustomerPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
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
    public String getSelectedCustomerId() {
        return selectedCustomerId;
    }


    
    public void openAddCustomerDialog() {
        ThemKhachHang khmoi=new ThemKhachHang(customerBUS, this);
        khmoi.FormThemKhachHang("Thêm khách hàng mới","Thêm",null);
        loadCustomerTable();
    }

    public void openEditCustomerDialog(String id) {
        CustomerBUS customerbus = new CustomerBUS();
        ThemKhachHang khmoi = new ThemKhachHang(customerbus, this);
        customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerbyId(id);
        // System.out.println("Hiii"+customer.getId());
        khmoi.FormThemKhachHang("Chỉnh sửa thông tin nhân viên","Cập nhật",customer);
        loadCustomerTable();
    }
        public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));
        
        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this,mainFrame));  
        toolbar.add(MenuChucNang.createSearchPanel()); 
        
        return toolbar;
    }

       private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        customerBUS = new CustomerBUS();
        customers = customerBUS.getAllCustomers();

        // Tiêu đề cột
        String[] columnNames = {"ID", "Họ Tên","Số Điện Thoại", "Email", "Ngày tham gia"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa ô trong bảng
            }
        };

        // Thiết lập header bảng
        JTableHeader header = customerTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Đổ dữ liệu vào bảng
        loadCustomerTable();

        // Áp dụng căn giữa cho tất cả các cột
        for (int i = 0; i < customerTable.getColumnCount(); i++) {
            customerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thiết lập bảng
        customerTable.setShowGrid(false);
        customerTable.setRowHeight(30);
        customerTable.setSelectionBackground(new Color(173, 216, 230));
        customerTable.setSelectionForeground(Color.BLACK);

        // Đưa bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        customerTable.setPreferredScrollableViewportSize(new Dimension(800, 300));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addTableSelectionListener() {
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow != -1) {          
                    selectedCustomerId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }

    public void loadCustomerTable() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        customers = customerBUS.getAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer.getName());
            Object[] rowData = {
                customer.getId(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getDateOfJoining()
            };
            tableModel.addRow(rowData);
        }
    }

    public void openRemoveEmployeeDialog(String id){
        CustomerBUS cs=new CustomerBUS();
        cs.deleteCustomer(id);
        loadCustomerTable();
    }

 

}
