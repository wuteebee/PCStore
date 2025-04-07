package DTO;

public class ThongSoKyThuat {
    private String IDThongSo;
    private String IDDanhMuc;
    private String TenThongSo;


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
