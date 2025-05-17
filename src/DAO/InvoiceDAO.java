package DAO;

import DTO.DetailedSalesInvoice;
import DTO.SalesInvoice;
import config.DatabaseConnection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class InvoiceDAO {
    private Connection conn;
    static private Map<String, SalesInvoice> salesInvoiceMap = new HashMap<>();
    static private Map<String, DetailedSalesInvoice> detailedSalesInvoiceMap = new HashMap<>();


    public InvoiceDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<SalesInvoice> getAllSalesInvoice() {
        List<SalesInvoice> salesInvoices = new ArrayList<>();
        String sql = "SELECT * FROM hoadonxuat";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("idHoaDonXuat");
                String eid = rs.getString("idNhanVien");
                String cid = rs.getString("idKhachHang");
                LocalDate date = rs.getDate("ngayTao").toLocalDate();
                double tongTien = rs.getDouble("tongTien");
                String did = rs.getString("idKhuyenMai");


                SalesInvoice salesInvoice = new SalesInvoice(id, eid, cid, date, tongTien, did);
                salesInvoiceMap.put(id, salesInvoice);
                salesInvoices.add(salesInvoice);
            }
        } catch (SQLException e) {
            // handdle exception
            System.out.println("Lỗi lấy hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
        }
        attachDetailedSalesInvoice();
        return salesInvoices;
    }

    public void attachDetailedSalesInvoice() {
        String sql = "SELECT * FROM chitiethoadonxuat";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("idChiTietHoaDonXuat");
                String fid = rs.getString("idHoaDonXuat");
                String seri = rs.getString("SN");
                double donGia = rs.getDouble("donGia");

                DetailedSalesInvoice detailedSalesInvoice = new DetailedSalesInvoice(id, fid, seri, donGia);
                (salesInvoiceMap.get(fid)).addDetailedSalesInvoice(detailedSalesInvoice);
                detailedSalesInvoiceMap.put(id, detailedSalesInvoice);
            }
        } catch (SQLException e) {
            // handle exception
            System.out.println("Lỗi lấy chi tiết hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    public List<DetailedSalesInvoice> getAllDetailedSalesInvoice() {
//        List<DetailedSalesInvoice> detailedSalesInvoices = new ArrayList<>();
//        for(SalesInvoice si : salesInvoiceMap) {
//            for(DetailedSalesInvoice dsi : si.getDetailedSalesInvoices()) {
//                detailedSalesInvoices.add(dsi);
//            }
//        }
//        return detailedSalesInvoices;
//    }

    public boolean addSalesInvoice(SalesInvoice salesInvoice) {
        String sql = "INSERT INTO hoadonxuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, salesInvoice.getId());
            ps.setString(2, salesInvoice.getEid());
            ps.setString(3, salesInvoice.getCid());
            ps.setString(4, String.valueOf(Date.valueOf(salesInvoice.getDate())));
            ps.setString(5, String.valueOf(salesInvoice.getTotalPayment()));
            ps.setString(6, salesInvoice.getDid());
            // Thực thi câu lệnh và kiểm tra kết quả
            return (ps.executeUpdate() > 0 && addDetailedSalesInvoice(salesInvoice));
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean addDetailedSalesInvoice(SalesInvoice salesInvoice) {
        int flag = 0;
        String sql = "INSERT INTO chitiethoadonxuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<DetailedSalesInvoice> detailedSalesInvoices = salesInvoice.getDetailedSalesInvoices();
            for (DetailedSalesInvoice detailedSalesInvoice : detailedSalesInvoices) {
                ps.setString(1, detailedSalesInvoice.getId());
                ps.setString(2, detailedSalesInvoice.getFid());
                ps.setString(3, detailedSalesInvoice.getSeri());
                ps.setString(4, String.valueOf(detailedSalesInvoice.getDonGia()));
                flag = ps.executeUpdate();
                if (flag > 0) salesInvoiceMap.put(salesInvoice.getId(), salesInvoice);
            }
            // Thực thi câu lệnh và kiểm tra kết quả
            return flag > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm chi tiết hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSalesInvoice(List<DetailedSalesInvoice> detailedSalesInvoices) {
        String chiTietHoaDonXuatsql = "UPDATE chitiethoadonxuat SET seri = ? donGia = ? WHERE idChiTietHoaDonXuat = ?";
        String hoaDonXuatsql = "UPDATE hoadonxuat SET tongTien = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(chiTietHoaDonXuatsql);
            for (DetailedSalesInvoice detailedSalesInvoice : detailedSalesInvoices) {
                ps.setString(1, detailedSalesInvoice.getSeri());
                ps.setDouble(2, detailedSalesInvoice.getDonGia());
                ps.setString(3, detailedSalesInvoice.getFid());
                System.out.println("Cập nhật chi tiết hóa đơn xuất: " + detailedSalesInvoice);
                if (!(ps.executeUpdate() > 0)) return false;
            }

            double totalPayment = 0;
            SalesInvoice tmp = salesInvoiceMap.get(detailedSalesInvoices.get(0).getFid());
            for (DetailedSalesInvoice dsi : tmp.getDetailedSalesInvoices()) {
                totalPayment += dsi.getDonGia();
            }
            if (totalPayment != tmp.getTotalPayment()) {
                tmp.setTotalPayment(totalPayment);
                PreparedStatement pss = conn.prepareStatement(hoaDonXuatsql);
                pss.setDouble(1, totalPayment);
                pss.setString(2, tmp.getId());
                System.out.println("Cập nhật thành công hóa đơn xuất: " + salesInvoiceMap.get(detailedSalesInvoices.get(0).getFid()));
                return ps.executeUpdate() > 0 && pss.executeUpdate() > 0;
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật chi tiết hóa đơn xuất hoặc chi tiết hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSalesInvoice(String id) {
        String hoaDonXuatsql = "DELETE FROM hoadonxuat WHERE idHoaDonXuat = ?";
        try (PreparedStatement ps = conn.prepareStatement(hoaDonXuatsql)) {
            if (deleteDetailedSalesInvoice(id)) {
                salesInvoiceMap.remove(id);
            }
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa hóa đơn xuất: " + e.getMessage());
            e.getStackTrace();
            return false;
        }
    }

    public boolean deleteDetailedSalesInvoice(String id) {
        String chiTietHoaDonXuatsql = "DELETE FROM chitiethoadonxuat WHERE idHoaDonXuat = ?";
        try (PreparedStatement ps = conn.prepareStatement(chiTietHoaDonXuatsql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa chi tiết hóa đơn xuất: " + e.getMessage());
            e.getStackTrace();
            return false;
        }
    }

    public static Map<String, DetailedSalesInvoice> getDetailedSalesInvoiceMap() {
        return detailedSalesInvoiceMap;
    }

    public static Map<String, SalesInvoice> getSalesInvoiceMap() {
        return salesInvoiceMap;
    }

    public double getFirstPromotionInSalesInvoice() {
        String sql = "SELECT km.giaTri, km.idKhuyenMai FROM KhuyenMai AS km\n" +
                "LEFT JOIN HoaDonXuat AS hdx ON km.idKhuyenMai = hdx.idKhuyenMai\n" +
                "ORDER BY hdx.ngayTao ASC\n" +
                "LIMIT 1";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            double giaTri = rs.getDouble("giaTri");
            return giaTri;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy khuyến mãi: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }

    }

public List<String> getSNbyIDPhanLoai(String id) {
    List<String> result = new ArrayList<>();
    String sql = "SELECT SerialNumber FROM chitietsp WHERE idPhanLoai = ? AND maPhieuXuat = -1";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            result.add(rs.getString("SerialNumber"));
        }

    } catch (Exception e) {
        e.printStackTrace(); // Hoặc log lỗi
    }
    return result;
}

public int getDetailCount()
{
    String sql = "SELECT COUNT(idChiTietHoaDonXuat) as count from chitiethoadonxuat;";
    int count = 0;
    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next())
        count = rs.getInt("count");
        return count;
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
}
}
