package DTO;

import java.util.*;

public class Variant {
    private String PhienBan;
    private Double giaBan;
    private int soLuong;
    private List<ChiTietCauHinh> chitiet;

   public void setChitiet(List<ChiTietCauHinh> chitiet) {
       this.chitiet = chitiet;
   }
   public void setGiaBan(Double giaBan) {
       this.giaBan = giaBan;
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
   public Double getGiaBan() {
       return giaBan;
   }
   public String getPhienBan() {
       return PhienBan;
   }
   public int getSoLuong() {
       return soLuong;
   }
}
