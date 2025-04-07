package DTO;

public class Brand {
    private String MaThuongHieu;
    private String TenThuongHieu;
    private Catalog Danhmuc;
    private boolean trangThai;

    public Brand (){

    }
    public Brand(String MaThuongHieu,String TenThuongHieu,Catalog Danhmuc,boolean trangThai){
        this.MaThuongHieu=MaThuongHieu;
        this.TenThuongHieu=TenThuongHieu;
        this.Danhmuc=Danhmuc;
        this.trangThai=trangThai;
    }
    public void setMaThuongHieu(String maThuongHieu) {
        MaThuongHieu = maThuongHieu;
    }
    public void setTenThuongHieu(String tenThuongHieu) {
        TenThuongHieu = tenThuongHieu;
    }
    public String getMaThuongHieu() {
        return MaThuongHieu;
    }
    public String getTenThuongHieu() {
        return TenThuongHieu;
    }
    public void setDanhmuc(Catalog danhmuc) {
        Danhmuc = danhmuc;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    public Catalog getDanhmuc() {
        return Danhmuc;
    }
    

    
}
