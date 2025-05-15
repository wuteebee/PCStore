package DAO;

import DTO.ChiTietDonNhap;
import DTO.HoaDonNhap;
import DTO.ProductDetail;
import GUI.Panel.DashFinance;
import config.DatabaseConnection;
//import config.H2DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhieuNhapDAO {
    private Connection conn;

    //public PhieuNhapDAO() {
        //conn = H2DatabaseConnection.getConnection();
    //}

    // Thêm hóa đơn nhập mới
    public boolean insert(HoaDonNhap hdn) {
        String sql = "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hdn.getIdHoaDonNhap());
            ps.setString(2, hdn.getNhanVien().getId());
            ps.setString(3, hdn.getNhaCungCap().getId());
            ps.setDate(4, hdn.getNgayTao());
            ps.setDouble(5, hdn.getTongTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả hóa đơn nhập
    public List<HoaDonNhap> getAll() {
        List<HoaDonNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDonNhap ORDER BY ngayTao DESC";
        EmployeeDAO employeeDAO = new EmployeeDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoaDonNhap hdn = new HoaDonNhap();
                hdn.setIdHoaDonNhap(rs.getString("idHoaDonNhap"));
                String MaNhanvien=(rs.getString("idNhanVien"));
                hdn.setNhanVien(employeeDAO.getEmployeeById(MaNhanvien));
                String MaNhacungcap=rs.getString("idNhaCungCap");
                hdn.setNhaCungCap(supplierDAO.getSupplierById(MaNhacungcap));
                hdn.setNgayTao(rs.getDate("ngayTao"));
                hdn.setTongTien(rs.getDouble("tongTien"));
                list.add(hdn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Xóa hóa đơn nhập (nếu cần)
    public boolean delete(String id) {
        String sql = "DELETE FROM HoaDonNhap WHERE idHoaDonNhap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy hóa đơn theo ID (dùng cho sửa hoặc chi tiết)
    // public HoaDonNhap getById(String id) {
    //     String sql = "SELECT * FROM HoaDonNhap WHERE idHoaDonNhap = ?";
    //     try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setString(1, id);
    //         try (ResultSet rs = ps.executeQuery()) {
    //             if (rs.next()) {
    //                 return new HoaDonNhap(
    //                     rs.getString("idHoaDonNhap"),
    //                     rs.getString("idNhanVien"),
    //                     rs.getString("idNhaCungCap"),
    //                     rs.getDate("ngayTao"),
    //                     rs.getDouble("tongTien")
    //                 );
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    
    public List<ChiTietDonNhap> getAll_CTDonNhap(){
        List<ChiTietDonNhap> danhsach=new ArrayList<>();
        String sql="SELECT * FROM chitietdonnhap";
       try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                String idDonHang=rs.getString("idDonHang");
                String SN=rs.getString("SN");
                double donGia=rs.getDouble("donGia");

                ChiTietDonNhap tmp=new ChiTietDonNhap(idDonHang, SN, donGia);

                danhsach.add(tmp);
                
            }
        
       } catch (Exception e) {
        // TODO: handle exception
       }


        return danhsach;
    } 
public String insertHoaDonNhap(HoaDonNhap hdn) {
    String maHoaDon = null;

    String sql = "INSERT INTO hoadonnhap (idNhanVien, idNhaCungCap,ngayTao,tongTien) VALUES (?, ?, ?,?)";

    try (Connection conn = DatabaseConnection.getConnection(); // <-- dùng kết nối tới DB
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         System.out.println("Tổng tiền"+hdn.getTongTien());
        stmt.setString(2, hdn.getNhaCungCap().getId());        // idNhaCungCap
        stmt.setString(1, hdn.getNhanVien().getId());          // idNhanVien
        stmt.setDate(3, hdn.getNgayTao());
        stmt.setDouble(4,hdn.getTongTien());                 // java.sql.Date

        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    maHoaDon = String.valueOf(generatedKeys.getInt(1));
                }
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return maHoaDon;
}

public boolean insertChitietSP(ProductDetail productDetail) {
    boolean Save = false;
    String sql = "INSERT INTO chitietsp (SerialNumber, idPhanLoai,maphieunhap) VALUES (?, ?,?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, productDetail.getSerialNumber());
        stmt.setInt(2, productDetail.getIdPhanLoai());
        stmt.setString(3, productDetail.getMaPhieuNhap());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            Save = true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return Save;
}

public boolean insertChitietPhieuNhap(ProductDetail productDetail) {
    boolean Save = false;
    System.out.println("Mã đơn hàng trong insert"+productDetail.getMaPhieuNhap());
    String sql = "INSERT INTO chitietdonnhap (idDonHang,SN,donGia) VALUES (?,?,?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, productDetail.getMaPhieuNhap());
        stmt.setString(2, productDetail.getSerialNumber());
        stmt.setDouble(3, productDetail.getGiaNhap());


        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            Save = true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return Save;
}

public boolean isImeiExistInDatabase(String imei) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT COUNT(*) FROM chitietsp WHERE SerialNumber = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, imei);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean ktraXuatHang(String maPhieuNhap) {
    String sql = "SELECT COUNT(*) FROM chitietsp WHERE maphieunhap = ? AND maphieuxuat != -1";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maPhieuNhap);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0; // true nếu có ít nhất 1 sản phẩm đã được xuất
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


public boolean xoaChiTietPhieuNhap(String idPhieuNhap) {
    String sql = "DELETE FROM chitietdonnhap WHERE idDonHang = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, idPhieuNhap);
        int affectedRows = ps.executeUpdate();

        return affectedRows > 0; // Trả về true nếu có dòng bị xoá
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


public boolean xoaChiTietSPTheoPhieuNhap(String maPhieuNhap) {
    String sql = "DELETE FROM chitietsp WHERE maphieunhap = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maPhieuNhap);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0; // Trả về true nếu có dòng bị xóa
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


public boolean xoaHoaDonNhap(String maPhieuNhap) {
    String sql = "DELETE FROM hoadonnhap WHERE idHoaDonNhap = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maPhieuNhap);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0; // Trả về true nếu có dòng bị xóa
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

public boolean updateSTTK(String idPL, int soLuong) {
    Connection conn = null;
    PreparedStatement stmt = null;
    String sql = "UPDATE phanloaisp SET soLuongTonKho = soLuongTonKho + ? WHERE idPhanLoai = ?";

    try {
        conn = DatabaseConnection.getConnection();
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, soLuong);    // số lượng cần cộng thêm
        stmt.setString(2, idPL);    // mã phân loại sản phẩm

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





    public List<HoaDonNhap> getByDateRange(Date startDate, Date endDate) {
        List<HoaDonNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDonNhap WHERE ngayTao BETWEEN ? AND ? ORDER BY ngayTao DESC";
        EmployeeDAO employeeDAO = new EmployeeDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDonNhap hdn = new HoaDonNhap();
                    hdn.setIdHoaDonNhap(rs.getString("idHoaDonNhap"));
                    String MaNhanvien = rs.getString("idNhanVien");
                    hdn.setNhanVien(employeeDAO.getEmployeeById(MaNhanvien));
                    String MaNhacungcap = rs.getString("idNhaCungCap");
                    hdn.setNhaCungCap(supplierDAO.getSupplierById(MaNhacungcap));
                    hdn.setNgayTao(rs.getDate("ngayTao"));
                    hdn.setTongTien(rs.getDouble("tongTien"));
                    list.add(hdn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, BigDecimal> getByPeriod(String period) {
        Map<String, BigDecimal> result = new HashMap<>();
        String sql;
        if ("Month".equals(period)) {
            sql = "SELECT TO_CHAR(ngayTao, 'YYYY-MM') AS month, SUM(tongTien) AS total " +
                    "FROM HoaDonNhap " +
                    "GROUP BY TO_CHAR(ngayTao, 'YYYY-MM') " +
                    "ORDER BY month";
        } else {
            sql = "SELECT EXTRACT(YEAR FROM ngayTao) AS year, SUM(tongTien) AS total " +
                    "FROM HoaDonNhap " +
                    "GROUP BY EXTRACT(YEAR FROM ngayTao) " +
                    "ORDER BY year";
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String key = "Month".equals(period) ? rs.getString("month") : String.valueOf(rs.getInt("year"));
                result.put(key, DashFinance.bdCon(rs.getDouble("total")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
