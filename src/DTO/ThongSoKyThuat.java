package DTO;

public class ThongSoKyThuat {
    private String idThongSo;
    private String idDanhMuc;
    private String tenThongSo;
    private String idDMLinhKien;




    public  ThongSoKyThuat(){

    }
    public ThongSoKyThuat(String idThongSo,String idDanhMuc,String tenThongSo,String idDMLinhKien){
        this.idDanhMuc=idDanhMuc;
        this.idThongSo=idThongSo;
        this.tenThongSo=tenThongSo;
        this.idDMLinhKien=idDMLinhKien;
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
    public void setIdDMLinhKien(String idDMLinhKien) {
        this.idDMLinhKien = idDMLinhKien;
    }
    public String getIdDMLinhKien() {
        return idDMLinhKien;
    }
}