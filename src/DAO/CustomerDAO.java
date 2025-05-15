package DAO;

//import config.H2DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DTO.Customer;
import config.DatabaseConnection;

public class CustomerDAO {
    private Connection conn;

    public CustomerDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("idKhachHang");
                String name = rs.getString("tenKhachHang");
                String phoneNumber = rs.getString("soDienThoai");
                String email = rs.getString("Mail");
                LocalDate dateOfJoining = rs.getDate("NgayThamGia").toLocalDate();
                boolean trangThai = rs.getBoolean("trangThai");

                Customer customer = new Customer(id, name, phoneNumber, email, dateOfJoining, trangThai);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public Customer getCustomerbyId(String id) {
        Customer customer = null;
        String sql = "SELECT * FROM khachhang WHERE idKhachHang = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("tenKhachHang");
                    String phoneNumber = rs.getString("soDienThoai");
                    String email = rs.getString("Mail");
                    LocalDate dateOfJoining = rs.getDate("NgayThamGia").toLocalDate();
                    boolean trangThai = rs.getBoolean("trangThai");

                    customer = new Customer(id, name, phoneNumber, email, dateOfJoining, trangThai);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE khachhang SET tenKhachHang = ?, soDienThoai = ?, Mail = ?, NgayThamGia = ?, trangThai = ? WHERE idKhachHang = ?";
        System.out.println("ID: " + customer.getId());
        System.out.println("Name: " + customer.getName());
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(customer.getDateOfJoining()));
            ps.setBoolean(5, true);
            ps.setInt(6, Integer.parseInt(customer.getId()));
            System.out.println( ps.executeUpdate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO khachhang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getEmail());
            ps.setDate(5, java.sql.Date.valueOf(customer.getDateOfJoining()));
            ps.setBoolean(6, true);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
        public boolean deleteCustomer(String id) {
            String sql = "UPDATE khachhang SET trangThai=? WHERE idKhachHang = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, false);
                ps.setString(2,id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
}
