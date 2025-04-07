package DTO;

public class CauHinhPC implements ChiTietCauHinh {
    private String tenThongTin;   
    private Product linhKien;   

    public CauHinhPC(String tenThongTin, Product linhKien) {
        this.tenThongTin = tenThongTin;
        this.linhKien = linhKien;
    }

    @Override
    public String getTenThongTin() {
        return tenThongTin;
    }

    public Product getLinhKien() {
        return linhKien;
    }

    public void setLinhKien(Product linhKien) {
        this.linhKien = linhKien;
    }
}
