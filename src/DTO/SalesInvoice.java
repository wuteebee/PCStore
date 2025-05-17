package DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesInvoice {
    private String id;              // id hóa đơn xuất
    private String eid;             // id nhân viên
    private String cid;             // id khách hàng
    private LocalDate date;         // ngày tạo hóa đơn
    private double totalPayment;    // tổng tiền
    private String did;             // id khuyến mãi
    private List<DetailedSalesInvoice> detailedSalesInvoices = new ArrayList<>();

    public SalesInvoice() {
        id = "";
        eid = "";
        cid = "";
        date = LocalDate.now();
        totalPayment = 0;
        did = null;
        // sửa lại khuyến mãi
    }

    public SalesInvoice(String id, String eid, String cid, LocalDate date, double totalPayment, List<DetailedSalesInvoice> detailedSalesInvoices, String did) {
        this.id = id;
        this.eid = eid;
        this.cid = cid;
        this.date = date;
        this.totalPayment = totalPayment;
        this.detailedSalesInvoices = detailedSalesInvoices;
        this.did = did;
    }

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

    public void addDetailedSalesInvoice(DetailedSalesInvoice detailedSalesInvoice) {
        this.detailedSalesInvoices.add(detailedSalesInvoice);
    }

    public List<DetailedSalesInvoice> getDetailedSalesInvoices() {
        return detailedSalesInvoices;
    }

    public void setDetailedSalesInvoice(List<DetailedSalesInvoice> newList) {
        this.detailedSalesInvoices = newList;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("|%-20s|\n|%-20s|\n|%-20s|\n|%-20s|\n|%-20f|\n|%-20s|\n",id, eid, cid, date, totalPayment, did));
        for(DetailedSalesInvoice dsi : detailedSalesInvoices) {
            sb.append(String.format("%-10s", " ")).append(dsi + "\n");
        }
        return sb.toString();
    }
}
