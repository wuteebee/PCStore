package DAO;

import DTO.PermissionGroup;
import config.DatabaseConnection;
import config.H2DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionGroupDAO {
    private Connection conn;

    public PermissionGroupDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<PermissionGroup> getAllPermissionGroups() {
        List<PermissionGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM NhomQuyen WHERE trangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PermissionGroup group = new PermissionGroup(
                    rs.getString("idNhomQuyen"),
                    rs.getString("tenNhomQuyen"),
                    rs.getInt("trangThai")
                );
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public PermissionGroup getPermissionGroupById(String id) {
        PermissionGroup group = null;
        String sql = "SELECT * FROM NhomQuyen WHERE idNhomQuyen = ? AND trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    group = new PermissionGroup(
                        rs.getString("idNhomQuyen"),
                        rs.getString("tenNhomQuyen"),
                        rs.getInt("trangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
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