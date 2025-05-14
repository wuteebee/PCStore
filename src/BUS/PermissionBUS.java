package BUS;

import DAO.PermissionDAO;
import DTO.PermissionGroup;
import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class PermissionBUS {
    private PermissionDAO permissionDAO;

    public PermissionBUS() {
        permissionDAO = new PermissionDAO();
    }

    public List<PermissionGroup> getAllPermissionGroups() {
        return permissionDAO.getAllPermissionGroups();
    }

    public PermissionGroup getPermissionGroupById(String id) {
        return permissionDAO.getAllPermissionGroups().stream()
                .filter(p -> p.getIdNhomQuyen().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean savePermissionGroup(PermissionGroup group) {
        boolean success = permissionDAO.insertPermissionGroup(group);
        if (success) {
            for (String idChucNang : group.getPermissions().keySet()) {
                for (String hanhDong : group.getPermissions().get(idChucNang)) {
                    permissionDAO.insertPermission(group.getIdNhomQuyen(), idChucNang, hanhDong);
                }
            }
        }
        return success;
    }

    public boolean updatePermissionGroup(PermissionGroup group) {
        boolean success = permissionDAO.updatePermissionGroup(group);
        if (success) {
            permissionDAO.deletePermissions(group.getIdNhomQuyen()); // Xóa quyền cũ
            for (String idChucNang : group.getPermissions().keySet()) {
                for (String hanhDong : group.getPermissions().get(idChucNang)) {
                    permissionDAO.insertPermission(group.getIdNhomQuyen(), idChucNang, hanhDong);
                }
            }
        }
        return success;
    }

    public boolean deletePermissionGroup(String id) {
        permissionDAO.deletePermissions(id); // Xóa quyền liên quan
        return permissionDAO.deletePermissionGroup(id);
    }

    public String getChucNangIdByName(String tenChucNang) {
        return permissionDAO.getChucNangIdByName(tenChucNang);
    }

    public boolean hasPermission(String idNhomQuyen, String idChucNang, String hanhDong) {
        return permissionDAO.hasPermission(idNhomQuyen, idChucNang, hanhDong);
    }

    public String generateUniqueId() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        do {
            id.setLength(0);
            id.append("NQ").append(String.format("%03d", random.nextInt(1000)));
        } while (permissionDAO.getAllPermissionGroups().stream().anyMatch(p -> p.getIdNhomQuyen().equals(id.toString())));
        return id.toString();
    }

    public String generateSequentialId() {
        String newId = "NQ001"; // ID mặc định nếu bảng rỗng
        String sql = "SELECT idNhomQuyen FROM NhomQuyen WHERE idNhomQuyen REGEXP '^NQ[0-9]+$' ORDER BY CAST(SUBSTRING(idNhomQuyen, 3) AS UNSIGNED) DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String maxId = rs.getString("idNhomQuyen"); // Ví dụ: NQ005
                int maxNumber = Integer.parseInt(maxId.substring(2)); // Lấy số: 005 -> 5
                maxNumber++; // Tăng lên: 6
                newId = String.format("NQ%03d", maxNumber); // Định dạng: NQ006
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }
}
