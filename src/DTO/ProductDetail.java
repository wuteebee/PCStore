package DTO;

public class ProductDetail {
    private String serialNumber;
    private String idPhanLoai;
    private String maPhieuNhap;
    private String maPhieuXuat;
    private double giaNhap;
    private boolean trangThai;

    public ProductDetail() {
    }

    public ProductDetail(String serialNumber, String idPhanLoai, double giaNhap, boolean trangThai) {
        this.serialNumber = serialNumber;
        this.idPhanLoai = idPhanLoai;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
    }
    
    public ProductDetail(String serialNumber, String idPhanLoai, double giaNhap, boolean trangThai,String maPhieuNhap, String maPhieuXuat) {
        this.maPhieuNhap = maPhieuNhap;
        this.serialNumber = serialNumber;
        this.idPhanLoai = idPhanLoai;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
        this.maPhieuNhap=maPhieuNhap;
        this.maPhieuXuat=maPhieuXuat;
    }


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIdPhanLoai() {
        return idPhanLoai;
    }

    public void setIdPhanLoai(String idPhanLoai) {
        this.idPhanLoai = idPhanLoai;
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public String getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(String maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDetail)) return false;
        ProductDetail that = (ProductDetail) o;
        return serialNumber != null && serialNumber.equals(that.serialNumber);
    }

    @Override
    public int hashCode() {
        return serialNumber != null ? serialNumber.hashCode() : 0;
    }
}
