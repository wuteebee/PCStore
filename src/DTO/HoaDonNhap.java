package DTO;

import java.sql.Date;

public class HoaDonNhap {
    private String idHoaDonNhap;
    private String idNhanVien;
    private String idNhaCungCap;
    private Date ngayTao;
    private double tongTien;

    public HoaDonNhap() {
    }

    public HoaDonNhap(String idHoaDonNhap, String idNhanVien, String idNhaCungCap, Date ngayTao, double tongTien) {
        this.idHoaDonNhap = idHoaDonNhap;
        this.idNhanVien = idNhanVien;
        this.idNhaCungCap = idNhaCungCap;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
    }

    public String getIdHoaDonNhap() {
        return idHoaDonNhap;
    }

    public void setIdHoaDonNhap(String idHoaDonNhap) {
        this.idHoaDonNhap = idHoaDonNhap;
    }

    public String getIdNhanVien() {
        return idNhanVien;
    }

    public void setIdNhanVien(String idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public String getIdNhaCungCap() {
        return idNhaCungCap;
    }

    public void setIdNhaCungCap(String idNhaCungCap) {
        this.idNhaCungCap = idNhaCungCap;
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
