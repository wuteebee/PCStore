package DTO;

public class ThongSoKyThuat {
    private String idThongSo;
    private String idDanhMuc;
    private String tenThongSo;




    public  ThongSoKyThuat(){

    }
    public ThongSoKyThuat(String idThongSo,String idDanhMuc,String tenThongSo){
        this.idDanhMuc=idDanhMuc;
        this.idThongSo=idThongSo;
        this.tenThongSo=tenThongSo;
    }
    public void setIDDanhMuc(String iDDanhMuc) {
        idDanhMuc = iDDanhMuc;
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