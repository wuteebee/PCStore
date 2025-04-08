package DTO;

public class ProductDetail {
    private String serialNumber;
    private Variant sanPham;
    private boolean trangThai;


    public ProductDetail() {
    }


    public ProductDetail(String serialNumber, Variant sanPham, boolean trangThai) {
        this.serialNumber = serialNumber;
        this.sanPham = sanPham;
        this.trangThai = trangThai;
    }

    public void setSanPham(Variant sanPham) {
        this.sanPham = sanPham;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public Variant getSanPham() {
        return sanPham;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public boolean isTrangThai() {
        return trangThai;
    }
}