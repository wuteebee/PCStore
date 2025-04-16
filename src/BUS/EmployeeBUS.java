package BUS;

import DAO.EmployeeDAO;
import DTO.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployeeBUS {
    private EmployeeDAO employeeDAO;

    public EmployeeBUS(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public boolean addEmployee(Employee employee) {
        System.out.println("Thêm nhân viên vào hệ thống");

        // Kiểm tra tính hợp lệ của email
        if (!employee.getEmail().contains("@")) {
            System.out.println("mail");
            return false; // Email không hợp lệ
        }

        LocalDate date = employee.getDateOfJoining();

        if (date == null) {
            System.out.println("date");
            return false;
        }
        // Lấy danh sách nhân viên hiện tại
        EmployeeDAO tmp = new EmployeeDAO();
        List<Employee> employees = tmp.getAllEmployees();

        Employee newEmployee = new Employee(
                generateUniqueId(employees),
                employee.getName(),
                employee.getPosition(),
                employee.getPhoneNumber(),
                employee.getEmail(),
                date,
                employee.getLuong()
        );

        System.out.println(newEmployee.getId());
        System.out.println(newEmployee.getName());
        System.out.println(newEmployee.getPosition());
        System.out.println(newEmployee.getPhoneNumber());
        System.out.println(newEmployee.getEmail());
        return employeeDAO.addEmployee(newEmployee);
    }

    // Phương thức chỉnh sửa thông tin nhân viên (ví dụ)
    public boolean updateEmployee(Employee employee) {
        return employeeDAO.updateEmployee(employee);
    }

    // Phương thức xóa nhân viên
    public boolean deleteEmployee(String id) {

        return employeeDAO.deleteEmployee(id);
    }

    // Phương thức lấy danh sách nhân viên
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        List<Employee> activeEmployees = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.getTrangThai() == true) {
                activeEmployees.add(emp);
            }

        }
        return activeEmployees;
    }

    public String generateUniqueId(List<Employee> employees) {
        Random random = new Random();
        boolean exists;
        String tmp;

        // Kiểm tra nếu danh sách employees là null hoặc rỗng
        if (employees == null || employees.isEmpty()) {
            // Nếu không có nhân viên, trả về ID ngẫu nhiên
            return "NV" + (1000 + random.nextInt(9000));
        }

        // Tiến hành tạo ID ngẫu nhiên và kiểm tra xem có trùng lặp không
        do {
            int number = 1000 + random.nextInt(9000); // Tạo số từ 1000 đến 9999
            String t = "NV" + number; // ID bắt đầu bằng "NV" và theo sau là một số ngẫu nhiên

            // Kiểm tra xem ID đã tồn tại chưa trong danh sách employees
            exists = employees.stream().anyMatch(e -> e.getId() != null && e.getId().equals(t));
            if (!exists) {
                return t;
            }
        } while (true);


    }

}

