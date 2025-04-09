package DTO;

public class ThongSoKyThuat {
    private String IDThongSo;
    private String IDDanhMuc;
    private String TenThongSo;


    public void setIDDanhMuc(String iDDanhMuc) {
        IDDanhMuc = iDDanhMuc;
    }

    public void setIdThongSo(String idThongSo) {
        this.idThongSo = idThongSo;
    }

    public void setTenThongSo(String tenThongSo) {
        this.tenThongSo = tenThongSo;
    }

    public String getIdDanhMuc() {
        return idDanhMuc;
    }

    public String getIdThongSo() {
        return idThongSo;
    }

    public String getTenThongSo() {
        return tenThongSo;
    }
}