package BUS;

import DTO.DetailedSalesInvoice;
import DTO.SalesInvoice;
import DAO.InvoiceDAO;

import java.util.List;
import java.util.Map;

public class InvoiceBUS {
    private InvoiceDAO invoiceDAO;

    public InvoiceBUS(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }
    public InvoiceBUS()
    {
        invoiceDAO = new InvoiceDAO();
    }

    public List<SalesInvoice> fetchSalesInvoice()
    {
        System.out.println("Fetch data invoice");
        return invoiceDAO.getAllSalesInvoice();
    }

    public boolean addSalesInvoice(SalesInvoice salesInvoice) {
        System.out.println("Thêm hóa đơn xuất vào hệ thống");
        return invoiceDAO.addSalesInvoice(salesInvoice);
    }

    public boolean updateSalesInvoice(List<DetailedSalesInvoice> detailedSalesInvoices) {
        return invoiceDAO.updateSalesInvoice(detailedSalesInvoices);
    }

    public boolean deleteSalesInvoice(String id) {
        return invoiceDAO.deleteSalesInvoice(id);
    }

    public String generateSalesInvoiceUniqueID(Map<String, SalesInvoice> salesInvoiceMap) {
        int idHoaDonXuat = salesInvoiceMap.size();
        return String.format("%017d", idHoaDonXuat);
    }

    public String generateDetailedSalesInvoiceUniqueID(Map<String, DetailedSalesInvoice> detailedSalesInvoiceMap) {
        int idChiTietHoaDonXuat = detailedSalesInvoiceMap.size();
        return String.format("%017d", idChiTietHoaDonXuat);
    }

    public double getFirstPromotionInSalesInvoice() {
        return invoiceDAO.getFirstPromotionInSalesInvoice();
    }

    public List<String> getSNbyidPL(String id){
        return invoiceDAO.getSNbyIDPhanLoai(id);
    }

    public int getDetailCount()
    {
        return invoiceDAO.getDetailCount();
    }
}
