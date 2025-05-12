package DTO;

public class PermissionGroup {
    private String idNhomQuyen;
    private String tenNhomQuyen;
    private int trangThai;

    // Constructors
    public PermissionGroup() {}

    public PermissionGroup(String idNhomQuyen, String tenNhomQuyen, int trangThai) {
        this.idNhomQuyen = idNhomQuyen;
        this.tenNhomQuyen = tenNhomQuyen;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getIdNhomQuyen() { return idNhomQuyen; }
    public void setIdNhomQuyen(String idNhomQuyen) { this.idNhomQuyen = idNhomQuyen; }
    public String getTenNhomQuyen() { return tenNhomQuyen; }
    public void setTenNhomQuyen(String tenNhomQuyen) { this.tenNhomQuyen = tenNhomQuyen; }
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}