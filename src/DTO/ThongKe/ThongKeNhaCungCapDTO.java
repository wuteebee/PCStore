package DTO.ThongKe;

import java.util.Objects;

public class ThongKeNhaCungCapDTO {
    String mancc; // Đổi từ int sang String
    String tenncc;
    int soluong;
    long tongtien;
    
    public ThongKeNhaCungCapDTO() {}

    public ThongKeNhaCungCapDTO(String mancc, String tenncc, int soluong, long tongtien) {
        this.mancc = mancc;
        this.tenncc = tenncc;
        this.soluong = soluong;
        this.tongtien = tongtien;
    }

    public String getMancc() {
        return mancc;
    }

    public void setMancc(String mancc) {
        this.mancc = mancc;
    }

    public String getTenncc() {
        return tenncc;
    }

    public void setTenncc(String tenncc) {
        this.tenncc = tenncc;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public long getTongtien() {
        return tongtien;
    }

    public void setTongtien(long tongtien) {
        this.tongtien = tongtien;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.mancc);
        hash = 59 * hash + Objects.hashCode(this.tenncc);
        hash = 59 * hash + this.soluong;
        hash = 59 * hash + (int) (this.tongtien ^ (this.tongtien >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ThongKeNhaCungCapDTO{" + "mancc=" + mancc + ", tenncc=" + tenncc + ", soluong=" + soluong + ", tongtien=" + tongtien + '}';
    }
}