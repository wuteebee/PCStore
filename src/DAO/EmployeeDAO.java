package DAO;

import config.DatabaseConnection;
//import config.H2DatabaseConnection;
import DTO.Employee;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private Connection conn;

    public EmployeeDAO() {
        //Sử dụng kết nối từ DatabaseConnection, nếu có lỗi thì sẽ ném ra RuntimeException
        conn = DatabaseConnection.getConnection();
    }

    // Lấy danh sách tất cả nhân viên
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien"; 
    
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                String id = rs.getString("IDNhanVien");
                String name = rs.getString("TenNhanVien");
                String position = rs.getString("ViTri");
                double salary = rs.getDouble("Luong");
                String phoneNumber = rs.getString("SDT");
                String email = rs.getString("Mail");

                LocalDate dateOfJoining = rs.getDate("NgayVaoLam").toLocalDate();
                boolean trangThai = rs.getBoolean("trangThai");
          
                
                Employee emp = new Employee(id, name, position,  phoneNumber, email, dateOfJoining,salary,trangThai);
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    public Employee getEmployeeById(String id) {
        Employee employee = null;
        String sql = "SELECT * FROM nhanvien WHERE IDNhanVien = ?"; // Truy vấn theo ID nhân viên
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);  // Đặt giá trị ID cho câu lệnh SQL
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Lấy thông tin nhân viên từ kết quả truy vấn
                    
                    String name = rs.getString("TenNhanVien");
                    String position = rs.getString("ViTri");
                    double salary = rs.getDouble("Luong");
                    String phoneNumber = rs.getString("SDT");
                    String email = rs.getString("Mail");
                    LocalDate dateOfJoining = rs.getDate("NgayVaoLam").toLocalDate();
              
                    // Tạo đối tượng Employee từ dữ liệu truy vấn
                    employee = new Employee(id, name, position,phoneNumber, email, dateOfJoining, salary);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employee; 
    }
    
    // Thêm nhân viên
    public boolean addEmployee(Employee emp) {

        String sql = "INSERT INTO nhanvien (IDNhanVien,TenNhanVien, ViTri, SDT, Mail, NgayVaoLam,Luong) VALUES (?,?, ?, ?, ?, ?,?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getId());  
            ps.setString(2, emp.getName());               // TenNhanVien
            ps.setString(3, emp.getPosition());           // ViTri
            ps.setString(4, emp.getPhoneNumber());        // SDT
            ps.setString(5, emp.getEmail());              // Mail
            ps.setDate(6, Date.valueOf(emp.getDateOfJoining())); // NgayVaoLam
            ps.setDouble(7, emp.getLuong());              // Luong
            // Thực thi câu lệnh và kiểm tra kết quả
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // In chi tiết lỗi để debug dễ hơn
            System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE nhanvien SET TenNhanVien = ?, ViTri = ?, SDT = ?, Mail = ?, NgayVaoLam = ?, Luong = ? WHERE IDNhanVien = ?";
    
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getName());                 // TenNhanVien
            ps.setString(2, emp.getPosition());             // ViTri
            ps.setString(3, emp.getPhoneNumber());          // SDT
            ps.setString(4, emp.getEmail());                // Mail
            ps.setDate(5, Date.valueOf(emp.getDateOfJoining())); // NgayVaoLam
            ps.setDouble(6, emp.getLuong());                // Luong (Sửa lại vị trí)
            ps.setString(7, emp.getId());                   // IDNhanVien (Sửa lại vị trí)
            
            System.out.println("Lương cập nhật: " + emp.getLuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật thông tin nhân viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    // Xóa nhân viên theo id
    public boolean deleteEmployee(String id) {
        String sql = "UPDATE nhanvien SET trangThai = 0 WHERE IDNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
