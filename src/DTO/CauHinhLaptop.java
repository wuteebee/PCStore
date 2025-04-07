package DTO;

public class CauHinhLaptop implements ChiTietCauHinh {
    private String tenThongTin; 
    private String giaTri;      

    public CauHinhLaptop(String tenThongTin, String giaTri) {
        this.tenThongTin = tenThongTin;
        this.giaTri = giaTri;
    }

    @Override
    public String getTenThongTin() {
        return tenThongTin;
    }

    public String getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(String giaTri) {
        this.giaTri = giaTri;
    }
}
