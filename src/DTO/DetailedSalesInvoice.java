package DTO;

public class DetailedSalesInvoice {
    private String id;                  // ma chi tiet hoa don gia
    private String fid;                 // ma hoa don xuat
    private String seri;                 // so seri
    private double donGia;              // don gia


    public DetailedSalesInvoice() {
        this.id = "";
        this.fid = "";
        this.seri = "";
        this.donGia = 0;
    }

    public DetailedSalesInvoice(String id, String fid, String seri, int soLuong, double donGia) {
        this.id = id;
        this.fid = fid;
        this.seri = seri;
        this.donGia = donGia;
    }


    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return seri;
    }

    public void setPid(String pid) {
        this.seri = pid;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    @Override
    public String toString() {
        return String.format("|%-20s|%-20s|%-20s|%-15f|", id, fid, seri, donGia);
    }
}
