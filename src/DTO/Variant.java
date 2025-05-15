package DTO;

import java.util.*;

public class Variant {
    private int idVariant;
    private int PhienBan;
    private Double gia;
    private int soLuong;
    private List<ChiTietCauHinh> chitiet;
    private boolean trangThai;


    public Variant() {
    }

    public Variant(int idVariant, int PhienBan, Double gia, int soLuong,boolean trangThai) {
        this.idVariant = idVariant;
        this.PhienBan = PhienBan;
        this.gia = gia;
        this.soLuong = soLuong;
        this.trangThai=trangThai;
    }

    public void setChitiet(List<ChiTietCauHinh> chitiet) {
        this.chitiet = chitiet;
    }

    public void setGiaBan(Double gia) {
        this.gia = gia;
    }

    public void setPhienBan(int phienBan) {
        PhienBan = phienBan;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public List<ChiTietCauHinh> getChitiet() {
        return chitiet;
    }

    public Double getGia() {
        return gia;
    }

    public int getPhienBan() {
        return PhienBan;
    }

    public int getSoLuong() {
        return soLuong;
    }
    public int getIdVariant() {
        return idVariant;
    }
    public boolean isTrangThai() {
        return trangThai;
    }
}