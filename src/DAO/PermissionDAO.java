package DAO;

import DTO.PermissionGroup;
import config.DatabaseConnection;
import config.H2DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionDAO {
    private Connection conn;

    public PermissionDAO() {
        conn = H2DatabaseConnection.getConnection();
    }

    public List<PermissionGroup> getAllPermissionGroups() {
        List<PermissionGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM NhomQuyen WHERE trangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PermissionGroup group = new PermissionGroup();
                group.setIdNhomQuyen(rs.getString("idNhomQuyen"));
                group.setTenNhomQuyen(rs.getString("tenNhomQuyen"));
                group.setTrangThai(rs.getInt("trangThai"));
                // Load permissions
                group.setPermissions(loadPermissions(group.getIdNhomQuyen()));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    private Map<String, List<String>> loadPermissions(String idNhomQuyen) {
        Map<String, List<String>> permissions = new HashMap<>();
        String sql = "SELECT idChucNang, hanhDong FROM ChiTietQuyen WHERE idNhomQuyen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idNhomQuyen);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String idChucNang = rs.getString("idChucNang");
                    String hanhDong = rs.getString("hanhDong");
                    permissions.computeIfAbsent(idChucNang, k -> new ArrayList<>()).add(hanhDong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    public boolean insertPermissionGroup(PermissionGroup group) {
        String sql = "INSERT INTO NhomQuyen (idNhomQuyen, tenNhomQuyen, trangThai) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, group.getIdNhomQuyen());
            ps.setString(2, group.getTenNhomQuyen());
            ps.setInt(3, group.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePermissionGroup(PermissionGroup group) {
        String sql = "UPDATE NhomQuyen SET tenNhomQuyen = ?, trangThai = ? WHERE idNhomQuyen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, group.getTenNhomQuyen());
            ps.setInt(2, group.getTrangThai());
            ps.setString(3, group.getIdNhomQuyen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePermissionGroup(String id) {
        String sql = "UPDATE NhomQuyen SET trangThai = 0 WHERE idNhomQuyen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertPermission(String idNhomQuyen, String idChucNang, String hanhDong) {
        String sql = "INSERT INTO ChiTietQuyen (idNhomQuyen, idChucNang, hanhDong) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idNhomQuyen);
            ps.setString(2, idChucNang);
            ps.setString(3, hanhDong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePermissions(String idNhomQuyen) {
        String sql = "DELETE FROM ChiTietQuyen WHERE idNhomQuyen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idNhomQuyen);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getChucNangIdByName(String tenChucNang) {
        String sql = "SELECT idChucNang FROM ChucNang WHERE tenChucNang = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenChucNang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("idChucNang");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasPermission(String idNhomQuyen, String idChucNang, String hanhDong) {
        String sql = "SELECT COUNT(*) FROM ChiTietQuyen WHERE idNhomQuyen = ? AND idChucNang = ? AND hanhDong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idNhomQuyen);
            ps.setString(2, idChucNang);
            ps.setString(3, hanhDong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}