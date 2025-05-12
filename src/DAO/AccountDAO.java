package DAO;

import DTO.Account;
import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private Connection conn;

    public AccountDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                        rs.getString("idTaiKhoan"),
                        rs.getString("idNhanVien"),
                        rs.getBytes("anhDaiDien"),
                        rs.getString("idNhomQuyen"),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"),
                        rs.getInt("trangThai"),
                        rs.getString("maOTP")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE trangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Account account = new Account(
                    rs.getString("idTaiKhoan"),
                    rs.getString("idNhanVien"),
                    rs.getBytes("anhDaiDien"),
                    rs.getString("idNhomQuyen"),
                    rs.getString("tenDangNhap"),
                    rs.getString("matKhau"),
                    rs.getInt("trangThai"),
                    rs.getString("maOTP")
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean insertAccount(Account account) {
        String sql = "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, tenDangNhap, matKhau, trangThai, maOTP) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getIdTaiKhoan());
            ps.setString(2, account.getIdNhanVien());
            ps.setBytes(3, account.getAnhDaiDien());
            ps.setString(4, account.getIdNhomQuyen());
            ps.setString(5, account.getTenDangNhap());
            ps.setString(6, account.getMatKhau());
            ps.setInt(7, account.getTrangThai());
            ps.setString(8, account.getMaOTP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAccount(Account account) {
        String sql = "UPDATE TaiKhoan SET idNhanVien = ?, anhDaiDien = ?, idNhomQuyen = ?, tenDangNhap = ?, matKhau = ?, maOTP = ? " +
                     "WHERE idTaiKhoan = ? AND trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getIdNhanVien());
            ps.setBytes(2, account.getAnhDaiDien());
            ps.setString(3, account.getIdNhomQuyen());
            ps.setString(4, account.getTenDangNhap());
            ps.setString(5, account.getMatKhau());
            ps.setString(6, account.getMaOTP());
            ps.setString(7, account.getIdTaiKhoan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(String idTaiKhoan, String newPassword) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE idTaiKhoan = ? AND trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, idTaiKhoan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOTP(String idTaiKhoan, String otp) {
        String sql = "UPDATE TaiKhoan SET maOTP = ? WHERE idTaiKhoan = ? AND trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, otp);
            ps.setString(2, idTaiKhoan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount(String idTaiKhoan) {
        String sql = "UPDATE TaiKhoan SET trangThai = 0 WHERE idTaiKhoan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTaiKhoan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAvailableEmployees() {
        List<String> employees = new ArrayList<>();
        String sql = "SELECT idNhanVien, TenNhanVien FROM NhanVien WHERE trangThai = 1 " +
                     "AND idNhanVien NOT IN (SELECT idNhanVien FROM TaiKhoan WHERE trangThai = 1)";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(rs.getString("idNhanVien") + " - " + rs.getString("TenNhanVien"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<String> getPermissionGroups() {
        List<String> groups = new ArrayList<>();
        String sql = "SELECT idNhomQuyen, tenNhomQuyen FROM NhomQuyen WHERE trangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groups.add(rs.getString("idNhomQuyen") + " - " + rs.getString("tenNhomQuyen"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public String getEmployeeName(String idNhanVien) {
        String sql = "SELECT TenNhanVien FROM NhanVien WHERE idNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenNhanVien");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPermissionGroupName(String idNhomQuyen) {
        String sql = "SELECT tenNhomQuyen FROM NhomQuyen WHERE idNhomQuyen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idNhomQuyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenNhomQuyen");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}