package DTO;

public class CauHinhPC extends ChiTietCauHinh {  
    private Product linhKien;   
    
    public CauHinhPC(String idSanPham,String idThongTin,int phienBan,Product linhKien ){
        super(idSanPham, idThongTin, phienBan);
        this.linhKien=linhKien;
    }

    public Product getLinhKien() {
        return linhKien;
    }

    public void setLinhKien(Product linhKien) {
        this.linhKien = linhKien;
    }
}
