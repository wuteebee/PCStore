package GUI.Panel;

import BUS.EmployeeBUS;
import DAO.EmployeeDAO;
import DTO.Employee;
import GUI.Components.MenuChucNang;
import GUI.Dialog.ThemNhanVien;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class EmployeePanel extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private List<Employee> employees;
    private EmployeeBUS employeeBUS;
    private String selectedEmployeeId = "-1"; 

    public EmployeePanel() {
        this.employeeBUS = new EmployeeBUS(new EmployeeDAO());
        initComponent();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);

        add(createEmployeeToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        // 🔹 Gọi hàm để lắng nghe sự kiện chọn dòng
        addTableSelectionListener();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        employees = employeeBUS.getAllEmployees();

        // Tiêu đề cột
        String[] columnNames = {"ID", "Họ Tên", "Chức Vụ", "Lương", "Số Điện Thoại", "Email", "Ngày Vào Làm","Trạng Thái"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa ô trong bảng
            }
        };

        // Thiết lập header bảng
        JTableHeader header = employeeTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Đổ dữ liệu vào bảng
        loadEmployeeTable();

        // Áp dụng căn giữa cho tất cả các cột
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thiết lập bảng
        employeeTable.setShowGrid(false);
        employeeTable.setRowHeight(30);
        employeeTable.setSelectionBackground(new Color(173, 216, 230));
        employeeTable.setSelectionForeground(Color.BLACK);

        // Đưa bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        employeeTable.setPreferredScrollableViewportSize(new Dimension(800, 300));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public JPanel createEmployeeToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));
        
        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this));  
        toolbar.add(MenuChucNang.createSearchPanel()); 
        
        return toolbar;
    }

    public void editEmployee() {
        System.out.println("Hihihihihi");
    }

    public void searchEmployee() {
        throw new UnsupportedOperationException("Unimplemented method 'searchEmployee'");
    }

    public void deleteEmployee() {
        throw new UnsupportedOperationException("Unimplemented method 'deleteEmployee'");
    }

    public void ExportExcel() {
        throw new UnsupportedOperationException("Unimplemented method 'ExportExcel'");
    }

    public void openAddEmployeeDialog() {
        ThemNhanVien nvmoi = new ThemNhanVien(employeeBUS, this);
        nvmoi.FormThemNv("Thêm nhân viên","Thêm",null);
    }

    public void openEditEmployeeDialog(String id) {
        ThemNhanVien nvmoi = new ThemNhanVien(employeeBUS, this);
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.getEmployeeById(id);
        nvmoi.FormThemNv("Chỉnh sửa thông tin nhân viên","Cập nhật",employee);
    }
    
    public void openRemoveEmployeeDialog(String id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            employeeBUS.deleteEmployee(id);
            loadEmployeeTable();
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void printSelectedEmployee(int selectedRow) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String position = (String) tableModel.getValueAt(selectedRow, 2);
        
        Object salaryObj = tableModel.getValueAt(selectedRow, 3);
        double salary = salaryObj instanceof Number ? ((Number) salaryObj).doubleValue() : 0;

        String phone = (String) tableModel.getValueAt(selectedRow, 4);
        String email = (String) tableModel.getValueAt(selectedRow, 5);

        Object dateObj = tableModel.getValueAt(selectedRow, 6);
        String dateOfJoining = dateObj != null ? dateObj.toString() : "N/A";

        String address = (String) tableModel.getValueAt(selectedRow, 7);

        System.out.println("ID: " + id);
        System.out.println("Họ tên: " + name);
        System.out.println("Chức vụ: " + position);
        System.out.println("Lương: " + salary);
        System.out.println("Số điện thoại: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Ngày vào làm: " + dateOfJoining);
 
    }



    private void addTableSelectionListener() {
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow != -1) {          
                              selectedEmployeeId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }
    
    // Trả về ID của nhân viên đã chọn
    public String getSelectedEmployeeId() {
        return selectedEmployeeId;
    }
    
    public void loadEmployeeTable() {
        employees = employeeBUS.getAllEmployees();
    
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Employee emp : employees) {

    
                tableModel.addRow(new Object[]{
                    emp.getId(), emp.getName(), emp.getPosition(),
                  emp.getLuong(),  emp.getPhoneNumber(), emp.getEmail(), emp.getDateOfJoining()
                });
    

            }
            tableModel.fireTableDataChanged();
        });
    }
}
