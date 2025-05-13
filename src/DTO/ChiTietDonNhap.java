package DTO;

public class ChiTietDonNhap {
     private String maPhieuNhap;
     private String SerialNumber;
     private double donGia;


     public ChiTietDonNhap(){

     }
     public ChiTietDonNhap(String maPhieuNhap,String SerialNumber,double donGia){
        this.maPhieuNhap=maPhieuNhap;
        this.SerialNumber=SerialNumber;
        this.donGia=donGia;
     }
     public void setDonGia(double donGia) {
         this.donGia = donGia;
     }
     public void setMaPhieuNhap(String maPhieuNhap) {
         this.maPhieuNhap = maPhieuNhap;
     }
     public void setSerialNumber(String serialNumber) {
         SerialNumber = serialNumber;
     }
     public double getDonGia() {
         return donGia;
     }
     public String getMaPhieuNhap() {
         return maPhieuNhap;
     }
     public String getSerialNumber() {
         return SerialNumber;
     }
}
