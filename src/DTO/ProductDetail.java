package DTO;

public class ProductDetail {
    private String SerialNumber;
    private Variant SanPham;
    private boolean trangThai;

    public void setSanPham(Variant sanPham) {
        SanPham = sanPham;
    }
    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    public Variant getSanPham() {
        return SanPham;
    }
    public String getSerialNumber() {
        return SerialNumber;
    }
    
}
