package DTO;

public class ComboProduct {
    private String idKhuyenMai;
    private String idSanPham;
    private String tenSanPham;

    // Constructors
    public ComboProduct() {}

    public ComboProduct(String idKhuyenMai, String idSanPham, String tenSanPham) {
        this.idKhuyenMai = idKhuyenMai;
        this.idSanPham = idSanPham;
        this.tenSanPham = tenSanPham;
    }

    // Getters and Setters
    public String getIdKhuyenMai() { return idKhuyenMai; }
    public void setIdKhuyenMai(String idKhuyenMai) { this.idKhuyenMai = idKhuyenMai; }
    public String getIdSanPham() { return idSanPham; }
    public void setIdSanPham(String idSanPham) { this.idSanPham = idSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
}

