package DAO;

import DTO.ChiTietDonNhap;
import DTO.HoaDonNhap;
import config.DatabaseConnection;
import config.H2DatabaseConnection;

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
}
