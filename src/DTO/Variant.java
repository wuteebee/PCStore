package DTO;

import java.util.*;

public class Variant {
    private String idVariant;
    private String PhienBan;
    private Double gia;
    private int soLuong;
    private List<ChiTietCauHinh> chitiet;


    public Variant() {
    }

    public Variant(String idVariant, String PhienBan, Double gia, int soLuong) {
        this.idVariant = idVariant;
        this.PhienBan = PhienBan;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public void setChitiet(List<ChiTietCauHinh> chitiet) {
        this.chitiet = chitiet;
    }

    public void setGiaBan(Double gia) {
        this.gia = gia;
    }

    public void setPhienBan(String phienBan) {
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

    public String getPhienBan() {
        return PhienBan;
    }

    public int getSoLuong() {
        return soLuong;
    }
}
