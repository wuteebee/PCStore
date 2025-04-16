package DTO;

import java.time.LocalDate;

public class Promotion {
    private String idKhuyenMai;
    private String tenKhuyenMai;
    private double giaTri;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String loai; // "Đơn lẻ" hoặc "Combo"
    private int trangThai;

    // Constructors
    public Promotion() {}

    public Promotion(String idKhuyenMai, String tenKhuyenMai, double giaTri, LocalDate ngayBatDau,
                     LocalDate ngayKetThuc, String loai, int trangThai) {
        this.idKhuyenMai = idKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.giaTri = giaTri;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.loai = loai;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getIdKhuyenMai() { return idKhuyenMai; }
    public void setIdKhuyenMai(String idKhuyenMai) { this.idKhuyenMai = idKhuyenMai; }
    public String getTenKhuyenMai() { return tenKhuyenMai; }
    public void setTenKhuyenMai(String tenKhuyenMai) { this.tenKhuyenMai = tenKhuyenMai; }
    public double getGiaTri() { return giaTri; }
    public void setGiaTri(double giaTri) { this.giaTri = giaTri; }
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}