package DAO;

import DTO.ChiTietDonNhap;
import DTO.HoaDonNhap;
import DTO.ProductDetail;
import config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {
    private Connection conn;

    public PhieuNhapDAO() {
        conn = DatabaseConnection.getConnection();
    }

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
    String sql = "INSERT INTO chitietsp (SerialNumber, idPhanLoai) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, productDetail.getSerialNumber());
        stmt.setInt(2, productDetail.getIdPhanLoai());

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


}
