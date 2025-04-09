package DTO;

public class ThongSoKyThuat {
    private String IDThongSo;
    private String IDDanhMuc;
    private String TenThongSo;

    public ThongSoKyThuat(){

    }
    public ThongSoKyThuat(String IDThongSo,String IDDanhMuc,String TenThongSo){
        this.IDDanhMuc=IDDanhMuc;
        this.IDThongSo=IDThongSo;
        this.TenThongSo=TenThongSo;
    }
    public void setIDDanhMuc(String iDDanhMuc) {
        IDDanhMuc = iDDanhMuc;
    }
    public void setIDThongSo(String iDThongSo) {
        IDThongSo = iDThongSo;
    }
    public void setTenThongSo(String tenThongSo) {
        TenThongSo = tenThongSo;
    }
    public String getIDDanhMuc() {
        return IDDanhMuc;
    }
    public String getIDThongSo() {
        return IDThongSo;
    }
    public String getTenThongSo() {
        return TenThongSo;
    }
}
