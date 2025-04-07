package DTO;

import java.util.HashMap;
import java.util.Map;

public class Catalog {
   private String MaDanhMuc;
   private String TenDanhMuc;
   private Catalog DanhMucCha;
   private boolean trangThai;
public Catalog(){

}


public Catalog(String MaDanhMuc,String TenDanhMuc,String idDanhMucCha,boolean trangThai){
    this.MaDanhMuc=MaDanhMuc;
    this.TenDanhMuc=TenDanhMuc;
    Catalog tmp= new Catalog();

    if(idDanhMucCha!=""){
        tmp.setMaDanhMuc(idDanhMucCha);
        this.DanhMucCha=tmp;
    }
    else{
        this.DanhMucCha=null;
    }

    this.trangThai=trangThai;
}
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
