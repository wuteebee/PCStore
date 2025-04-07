package DTO;

import java.util.*;
import java.util.Locale.Category;

public class Product {
    private String MaSp;
    private String TenSp;
    private Catalog DanhMuc;
    private Brand ThuongHieu;
    private String motaSanPham;
    private String anhSanPham;
    private boolean trangThai;
    private List<Variant> DanhSachPhienBan;
    


    public Product(){}
    public Product(String MaSp,String TenSp,Catalog DanhMuc,Brand ThuongHieu,String moTaSanPham,String anhSanPham,boolean trangThai){
        this.MaSp=MaSp;
        this.TenSp=TenSp;
        this.DanhMuc=DanhMuc;
        this.ThuongHieu=ThuongHieu;
        this.motaSanPham=moTaSanPham;
        this.anhSanPham=anhSanPham;
        this.trangThai=trangThai;

    }
  public void setAnhSanPham(String anhSanPham) {
      this.anhSanPham = anhSanPham;
  }
  public void setDanhMuc(Catalog danhMuc) {
      DanhMuc = danhMuc;
  }
  public void setDanhSachPhienBan(List<Variant> danhSachPhienBan) {
      DanhSachPhienBan = danhSachPhienBan;
  }
  public void setMaSp(String maSp) {
      MaSp = maSp;
  }
  public void setMotaSanPham(String motaSanPham) {
      this.motaSanPham = motaSanPham;
  }
  public void setTenSp(String tenSp) {
      TenSp = tenSp;
  }
  public void setThuongHieu(Brand thuongHieu) {
      ThuongHieu = thuongHieu;
  }
  public void setTrangThai(boolean trangThai) {
      this.trangThai = trangThai;
  }
  public String getAnhSanPham() {
      return anhSanPham;
  }
  public Catalog getDanhMuc() {
      return DanhMuc;
  }
  public List<Variant> getDanhSachPhienBan() {
      return DanhSachPhienBan;
  }
  public String getMaSp() {
      return MaSp;
  }
  public String getMotaSanPham() {
      return motaSanPham;
  }
  public String getTenSp() {
      return TenSp;
  }
  public Brand getThuongHieu() {
      return ThuongHieu;
  }
}
