package DTO;


import java.util.Date;

public class SalesInvoice {
    private String id;              // id hóa đơn xuất
    private Employee NhanVien;        
    private Customer khachhang;
    private Date date;         // ngày tạo hóa đơn
    private double totalPayment;    // tổng tiền
    private Promotion khuyenmai;
    

    public SalesInvoice() {
          
    }

    public SalesInvoice(String id,Employee NhanVien,Customer khachhang,Date date,double totalPayment,Promotion khuyenmai) {
        this.id = id;
        this.NhanVien=NhanVien;
        this.khachhang=khachhang;
        this.date=date;
        this.totalPayment = totalPayment;
        this.khuyenmai=khuyenmai;   
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setKhachhang(Customer khachhang) {
        this.khachhang = khachhang;
    }
    public void setKhuyenmai(Promotion khuyenmai) {
        this.khuyenmai = khuyenmai;
    }
    public void setNhanVien(Employee nhanVien) {
        NhanVien = nhanVien;
    }
    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }
    public Date getDate() {
        return date;
    }
    public String getId() {
        return id;
    }
    public Customer getKhachhang() {
        return khachhang;
    }
    public Promotion getKhuyenmai() {
        return khuyenmai;
    }
    public double getTotalPayment() {
        return totalPayment;
    }
    public Employee getNhanVien() {
        return NhanVien;
    }
    

}
