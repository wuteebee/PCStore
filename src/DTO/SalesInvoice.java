package DTO;

import java.time.LocalDate;

public class SalesInvoice {
    private String id;              // id hóa đơn xuất
    private String eid;             // id nhân viên
    private String cid;             // id khách hàng
    private LocalDate date;         // ngày tạo hóa đơn
    private double totalPayment;    // tổng tiền
    private String did;             // id khuyến mãi

    public SalesInvoice(String id, String eid, String cid, LocalDate date, double totalPayment, String did) {
        this.cid = cid;
        this.date = date;
        this.did = did;
        this.eid = eid;
        this.id = id;
        this.totalPayment = totalPayment;
    }

    public SalesInvoice(String eid, String cid, LocalDate date, double totalPayment, String did) {
        this.cid = cid;
        this.date = date;
        this.did = did;
        this.eid = eid;
        this.totalPayment = totalPayment;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }
}
