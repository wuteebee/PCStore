package DAO;

import DTO.HoaDonNhap;
import config.DatabaseConnection;
import config.H2DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {
    private Connection conn;

    public PhieuNhapDAO() {
        conn = H2DatabaseConnection.getConnection();
    }

    // Thêm hóa đơn nhập mới
    public boolean insert(HoaDonNhap hdn) {
        String sql = "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hdn.getIdHoaDonNhap());
            ps.setString(2, hdn.getIdNhanVien());
            ps.setString(3, hdn.getIdNhaCungCap());
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

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoaDonNhap hdn = new HoaDonNhap();
                hdn.setIdHoaDonNhap(rs.getString("idHoaDonNhap"));
                hdn.setIdNhanVien(rs.getString("idNhanVien"));
                hdn.setIdNhaCungCap(rs.getString("idNhaCungCap"));
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
    public HoaDonNhap getById(String id) {
        String sql = "SELECT * FROM HoaDonNhap WHERE idHoaDonNhap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HoaDonNhap(
                        rs.getString("idHoaDonNhap"),
                        rs.getString("idNhanVien"),
                        rs.getString("idNhaCungCap"),
                        rs.getDate("ngayTao"),
                        rs.getDouble("tongTien")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
