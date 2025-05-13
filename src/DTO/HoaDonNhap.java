package DTO;

import java.sql.Date;

public class HoaDonNhap {
    private String idHoaDonNhap;
    private Employee NhanVien;
    private Supplier NhaCungCap;
    private Date ngayTao;
    private double tongTien;

    public HoaDonNhap() {
    }

    public HoaDonNhap(String idHoaDonNhap, Employee idNhanVien, Supplier NhaCungCap, Date ngayTao, double tongTien) {
        this.idHoaDonNhap = idHoaDonNhap;
        this.NhanVien = idNhanVien;
        this.NhaCungCap = NhaCungCap;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
    }

    public String getIdHoaDonNhap() {
        return idHoaDonNhap;
    }

    public void setIdHoaDonNhap(String idHoaDonNhap) {
        this.idHoaDonNhap = idHoaDonNhap;
    }

    public Employee getNhanVien() {
        return NhanVien;
    }

    public void setNhanVien(Employee NhanVien) {
        this.NhanVien = NhanVien;
    }

    public Supplier getNhaCungCap() {
        return NhaCungCap;
    }

    public void setNhaCungCap(Supplier NhaCungCap) {
        this.NhaCungCap = NhaCungCap;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
