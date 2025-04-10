package DTO;

public class CauHinhLaptop extends ChiTietCauHinh {
    private String thongTin;
    public CauHinhLaptop(){}
    public CauHinhLaptop(String idSanPham,String idThongTin,int phienBan ,String thongTin){
        super(idSanPham, idThongTin, phienBan);
        this.thongTin=thongTin;
    }
    public void setThongTin(String thongTin) {
        this.thongTin = thongTin;
    }
    public String getThongTin() {
        return thongTin;
    }

   
}
