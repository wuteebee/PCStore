package DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionGroup {
    private String idNhomQuyen;
    private String tenNhomQuyen;
    private int trangThai;
    private Map<String, List<String>> permissions = new HashMap<>();

    public PermissionGroup() {}

    public PermissionGroup(String idNhomQuyen, String tenNhomQuyen, int trangThai) {
        this.idNhomQuyen = idNhomQuyen;
        this.tenNhomQuyen = tenNhomQuyen;
        this.trangThai = trangThai;
    }

    public String getIdNhomQuyen() {
        return idNhomQuyen;
    }

    public void setIdNhomQuyen(String idNhomQuyen) {
        this.idNhomQuyen = idNhomQuyen;
    }

    public String getTenNhomQuyen() {
        return tenNhomQuyen;
    }

    public void setTenNhomQuyen(String tenNhomQuyen) {
        this.tenNhomQuyen = tenNhomQuyen;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Map<String, List<String>> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, List<String>> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(String idChucNang, String hanhDong) {
        permissions.computeIfAbsent(idChucNang, k -> new ArrayList<>()).add(hanhDong);
    }
}