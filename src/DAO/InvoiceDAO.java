package DAO;

import DTO.DetailedSalesInvoice;
import DTO.SalesInvoice;
import config.DatabaseConnection;
import config.H2DatabaseConnection;

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
        EmployeeDAO employeeDAO = new EmployeeDAO();
        CustomerDAO customerDAO=new CustomerDAO();
        PromotionDAO promotionDAO=new PromotionDAO();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    SalesInvoice hdx=new SalesInvoice();
                    hdx.setId(rs.getString("idHoaDonXuat"));
                    String MaNhanvien=(rs.getString("idNhanVien"));
                    hdx.setNhanVien(employeeDAO.getEmployeeById(MaNhanvien));
                    String maKhachHang=rs.getString("idKhachHang");
                    hdx.setKhachhang(customerDAO.getCustomerbyId(maKhachHang));
                    String maKhuyenMai=rs.getString("idKhuyenMai");
                    hdx.setKhuyenmai(promotionDAO.getPromotionById(maKhuyenMai));
                    hdx.setDate(rs.getDate("ngayTao"));
                    hdx.setTotalPayment(rs.getDouble("tongTien"));
                    salesInvoices.add(hdx);

                }
             } catch (SQLException e) {
            // handdle exception
            System.out.println("Lỗi lấy hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
        }
        // attachDetailedSalesInvoice();
        return salesInvoices;
    }

    public List<DetailedSalesInvoice> getDetailedSalesInvoice() {
        List<DetailedSalesInvoice> danhsach=new ArrayList<>();
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
                danhsach.add(detailedSalesInvoice);
            }
        } catch (SQLException e) {
            // handle exception
            System.out.println("Lỗi lấy chi tiết hóa đơn xuất: " + e.getMessage());
            e.printStackTrace();
        }

        return danhsach;
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

    // public boolean addSalesInvoice(SalesInvoice salesInvoice) {
    //     String sql = "INSERT INTO hoadonxuat (idHoaDonXuat, idNhanVien, idKhachHang, idNgayTao, tongTien, idKhuyenMai)";
    //     try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setString(1, salesInvoice.getId());
    //         ps.setString(2, salesInvoice.getEid());
    //         ps.setString(3, salesInvoice.getCid());
    //         ps.setString(4, String.valueOf(Date.valueOf(salesInvoice.getDate())));
    //         ps.setString(5, String.valueOf(salesInvoice.getTotalPayment()));
    //         ps.setString(6, salesInvoice.getDid());
    //         // Thực thi câu lệnh và kiểm tra kết quả
    //         return (ps.executeUpdate() > 0 && addDetailedSalesInvoice(salesInvoice));
    //     } catch (SQLException e) {
    //         System.err.println("Lỗi khi thêm hóa đơn xuất: " + e.getMessage());
    //         e.printStackTrace();
    //         return false;
    //     }
    // }

 
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
