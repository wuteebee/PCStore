package DTO;

public class Catalog {
   private String MaDanhMuc;
   private String TenDanhMuc;
   private Catalog DanhMucCha;

public void setDanhMucCha(Catalog danhMucCha) {
    DanhMucCha = danhMucCha;
}
public void setMaDanhMuc(String maDanhMuc) {
    MaDanhMuc = maDanhMuc;
}
public void setTenDanhMuc(String tenDanhMuc) {
    TenDanhMuc = tenDanhMuc;
}
public Catalog getDanhMucCha() {
    return DanhMucCha;
}
public String getMaDanhMuc() {
    return MaDanhMuc;
}
public String getTenDanhMuc() {
    return TenDanhMuc;
}
}
