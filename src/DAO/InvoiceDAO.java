package DAO;

import DTO.DetailedSalesInvoice;
import DTO.SalesInvoice;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import config.DatabaseConnection;
import org.apache.xmlbeans.impl.soap.Detail;

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

    public boolean updateSalesInvoice(SalesInvoice salesInvoice, List<DetailedSalesInvoice> toDeleteList) {
        String hoaDonXuatsql = "UPDATE hoadonxuat SET idNhanVien = ?, idKhachHang = ?, ngayTao = ?, tongTien = ?, idKhuyenMai = ?  WHERE idHoaDonXuat = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(hoaDonXuatsql);
            ps.setString(1, salesInvoice.getEid());
            ps.setString(2, salesInvoice.getCid());
            ps.setDate(3, Date.valueOf(salesInvoice.getDate()));
            ps.setDouble(4, salesInvoice.getTotalPayment());
            ps.setString(5, salesInvoice.getDid());
            ps.setString(6, salesInvoice.getId());
            return (ps.executeUpdate() > 0 && updateDetailedSalesInvoice(salesInvoice, toDeleteList));
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật chi tiết hóa đơn xuất hoặc chi tiết hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDetailedSalesInvoice(SalesInvoice salesInvoice, List<DetailedSalesInvoice> toDeleteList) {
        boolean flag = false;
        String sql = "INSERT INTO chitiethoadonxuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES (?, ?, ?, ?)";
        String sql1 = "delete from chitiethoadonxuat where idchitiethoadonxuat = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps1 = conn.prepareStatement((sql1));
            for (DetailedSalesInvoice e : toDeleteList) {
                ps1.setString(1, e.getId());
                if (ps1.executeUpdate() > 0) flag = true;
            }
            for (DetailedSalesInvoice e : salesInvoice.getDetailedSalesInvoices()) {
                if (!hasDetailedSalesInvoice(e.getId())) {
                    System.out.println(e.getFid());
                    ps.setString(1, e.getId());
                    ps.setString(2, e.getFid());
                    ps.setString(3, e.getSeri());
                    ps.setString(4, String.valueOf(e.getDonGia()));
                    if (ps.executeUpdate() > 0) flag = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật chi tiết hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    public boolean hasDetailedSalesInvoice(String id) {
        String sql = "Select * from chitiethoadonxuat where idChiTietHoaDonXuat = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
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
}
