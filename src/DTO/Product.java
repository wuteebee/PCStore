package DTO;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String maSp;
    private String tenSp;
    private Catalog danhMuc;
    private Brand thuongHieu;
    private String moTaSanPham;
    private String anhSanPham;
    private boolean trangThai;
    private List<Variant> danhSachPhienBan;

    public Product() {
        this.danhSachPhienBan = new ArrayList<>();
    }

    public Product(String maSp, String tenSp, Catalog danhMuc, Brand thuongHieu, String moTaSanPham, String anhSanPham, boolean trangThai) {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.danhMuc = danhMuc;
        this.thuongHieu = thuongHieu;
        this.moTaSanPham = moTaSanPham;
        this.anhSanPham = anhSanPham;
        this.trangThai = trangThai;
        this.danhSachPhienBan = new ArrayList<>();
    }
    

    public void setAnhSanPham(String anhSanPham) {
        this.anhSanPham = anhSanPham;
    }

    public void setDanhMuc(Catalog danhMuc) {
        this.danhMuc = danhMuc;
    }

    public void setDanhSachPhienBan(List<Variant> danhSachPhienBan) {
        this.danhSachPhienBan = danhSachPhienBan;
    }

    public void setMaSp(String maSp) {
        this.maSp = maSp;
    }

    public void setMoTaSanPham(String moTaSanPham) {
        this.moTaSanPham = moTaSanPham;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public void setThuongHieu(Brand thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getAnhSanPham() {
        return anhSanPham;
    }

    public Catalog getDanhMuc() {
        return danhMuc;
    }

    public List<Variant> getDanhSachPhienBan() {
        return danhSachPhienBan;
    }

    public String getMaSp() {
        return maSp;
    }

    public String getMoTaSanPham() {
        return moTaSanPham;
    }

    public String getTenSp() {
        return tenSp;
    }

    public Brand getThuongHieu() {
        return thuongHieu;
    }

    public boolean isTrangThai() {
        return trangThai;
    }
}