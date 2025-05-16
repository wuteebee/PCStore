package DAO;

import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeKhachHangDTO;
import DTO.ThongKe.ThongKeNhaCungCapDTO;
import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import DTO.ThongKe.ThongKeTungNgayDTO;
import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ThongKeDAO {
    private Connection conn;

    public ThongKeDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoTungNam(int namBD, int namKT) {
        ArrayList<ThongKeDoanhThuDTO> result = new ArrayList<>();
        String sql = "SELECT nam, " +
                    "COALESCE((SELECT SUM(hdn.tongTien) " +
                    "          FROM HoaDonNhap hdn " +
                    "          WHERE YEAR(hdn.ngayTao) = nam), 0) AS chiphi, " +
                    "COALESCE(SUM(hdx.tongTien), 0) AS doanhthu, " +
                    "COALESCE(SUM(hdx.tongTien) - (SELECT SUM(hdn.tongTien) " +
                    "                             FROM HoaDonNhap hdn " +
                    "                             WHERE YEAR(hdn.ngayTao) = nam), 0) AS loinhuan " +
                    "FROM (SELECT DISTINCT YEAR(hdx.ngayTao) AS nam " +
                    "      FROM HoaDonXuat hdx " +
                    "      WHERE YEAR(hdx.ngayTao) BETWEEN ? AND ? " +
                    "      UNION " +
                    "      SELECT DISTINCT YEAR(hdn.ngayTao) AS nam " +
                    "      FROM HoaDonNhap hdn " +
                    "      WHERE YEAR(hdn.ngayTao) BETWEEN ? AND ?) AS years " +
                    "LEFT JOIN HoaDonXuat hdx ON YEAR(hdx.ngayTao) = nam " +
                    "GROUP BY nam " +
                    "ORDER BY nam";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, namBD);
            ps.setInt(2, namKT);
            ps.setInt(3, namBD);
            ps.setInt(4, namKT);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeDoanhThuDTO dto = new ThongKeDoanhThuDTO(
                        rs.getInt("nam"),
                        rs.getLong("chiphi"),
                        rs.getLong("doanhthu"),
                        rs.getLong("loinhuan")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTheoThangDTO> getThongKeTheoThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        String sql = "SELECT thang, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu, SUM(doanhthu - chiphi) AS loinhuan " +
                    "FROM ( " +
                    "    SELECT MONTH(hdn.ngayTao) AS thang, " +
                    "           COALESCE(SUM(hdn.tongTien), 0) AS chiphi, " +
                    "           0 AS doanhthu " +
                    "    FROM HoaDonNhap hdn " +
                    "    WHERE YEAR(hdn.ngayTao) = ? " +
                    "    GROUP BY MONTH(hdn.ngayTao) " +
                    "    UNION ALL " +
                    "    SELECT MONTH(hdx.ngayTao) AS thang, " +
                    "           0 AS chiphi, " +
                    "           COALESCE(SUM(hdx.tongTien), 0) AS doanhthu " +
                    "    FROM HoaDonXuat hdx " +
                    "    WHERE YEAR(hdx.ngayTao) = ? " +
                    "    GROUP BY MONTH(hdx.ngayTao) " +
                    ") AS combined " +
                    "GROUP BY thang " +
                    "ORDER BY thang";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeTheoThangDTO dto = new ThongKeTheoThangDTO(
                        rs.getInt("thang"),
                        rs.getLong("chiphi"),
                        rs.getLong("doanhthu"),
                        rs.getLong("loinhuan")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
}

    public ArrayList<ThongKeTheoThangDTO> getThongKeTungNgayTrongThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        String sql = "SELECT thang, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu, SUM(doanhthu - chiphi) AS loinhuan " +
                    "FROM ( " +
                    "    SELECT MONTH(hdn.ngayTao) AS thang, " +
                    "           COALESCE(SUM(hdn.tongTien), 0) AS chiphi, " +
                    "           0 AS doanhthu " +
                    "    FROM HoaDonNhap hdn " +
                    "    WHERE YEAR(hdn.ngayTao) = ? " +
                    "    GROUP BY MONTH(hdn.ngayTao) " +
                    "    UNION ALL " +
                    "    SELECT MONTH(hdx.ngayTao) AS thang, " +
                    "           0 AS chiphi, " +
                    "           COALESCE(SUM(hdx.tongTien), 0) AS doanhthu " +
                    "    FROM HoaDonXuat hdx " +
                    "    WHERE YEAR(hdx.ngayTao) = ? " +
                    "    GROUP BY MONTH(hdx.ngayTao) " +
                    ") AS combined " +
                    "GROUP BY thang " +
                    "ORDER BY thang";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeTheoThangDTO dto = new ThongKeTheoThangDTO(
                        rs.getInt("thang"),
                        rs.getLong("chiphi"),
                        rs.getLong("doanhthu"),
                        rs.getLong("loinhuan")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
}

    public ArrayList<ThongKeTungNgayDTO> getThongKeTuNgayDenNgay(String start, String end) {
        ArrayList<ThongKeTungNgayDTO> result = new ArrayList<>();
        
        // Lấy tất cả các ngày có hóa đơn (nhập hoặc xuất) bằng cách sử dụng UNION
        String sql = "SELECT ngay, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu, SUM(doanhthu - chiphi) AS loinhuan " +
                    "FROM ( " +
                    "    SELECT DATE(hdn.ngayTao) AS ngay, " +
                    "           COALESCE(SUM(hdn.tongTien), 0) AS chiphi, " +
                    "           0 AS doanhthu " +
                    "    FROM HoaDonNhap hdn " +
                    "    WHERE hdn.ngayTao BETWEEN ? AND ? " +
                    "    GROUP BY DATE(hdn.ngayTao) " +
                    "    UNION ALL " +
                    "    SELECT DATE(hdx.ngayTao) AS ngay, " +
                    "           0 AS chiphi, " +
                    "           COALESCE(SUM(hdx.tongTien), 0) AS doanhthu " +
                    "    FROM HoaDonXuat hdx " +
                    "    WHERE hdx.ngayTao BETWEEN ? AND ? " +
                    "    GROUP BY DATE(hdx.ngayTao) " +
                    ") AS combined " +
                    "GROUP BY ngay " +
                    "ORDER BY ngay";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, start);
            ps.setString(2, end);
            ps.setString(3, start);
            ps.setString(4, end);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeTungNgayDTO dto = new ThongKeTungNgayDTO(
                        rs.getString("ngay"),
                        rs.getLong("chiphi"),
                        rs.getLong("doanhthu"),
                        rs.getLong("loinhuan")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeKhachHangDTO> getAllKhachHang() {
        ArrayList<ThongKeKhachHangDTO> result = new ArrayList<>();
        String sql = "SELECT kh.idKhachHang, kh.tenKhachHang, " +
                     "COUNT(hdx.idHoaDonXuat) AS soluongphieu, " +
                     "COALESCE(SUM(hdx.tongTien), 0) AS tongtien " +
                     "FROM KhachHang kh " +
                     "LEFT JOIN HoaDonXuat hdx ON kh.idKhachHang = hdx.idKhachHang " +
                     "GROUP BY kh.idKhachHang, kh.tenKhachHang " +
                     "ORDER BY kh.idKhachHang";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeKhachHangDTO dto = new ThongKeKhachHangDTO(
                        rs.getInt("idKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getInt("soluongphieu"),
                        rs.getLong("tongtien")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeKhachHangDTO> filterKhachHang(String input, Date start, Date end) {
        ArrayList<ThongKeKhachHangDTO> result = new ArrayList<>();
        String sql = "SELECT kh.idKhachHang, kh.tenKhachHang, " +
                     "COUNT(hdx.idHoaDonXuat) AS soluongphieu, " +
                     "COALESCE(SUM(hdx.tongTien), 0) AS tongtien " +
                     "FROM KhachHang kh " +
                     "LEFT JOIN HoaDonXuat hdx ON kh.idKhachHang = hdx.idKhachHang " +
                     "WHERE kh.tenKhachHang LIKE ? AND (hdx.ngayTao BETWEEN ? AND ? OR hdx.ngayTao IS NULL) " +
                     "GROUP BY kh.idKhachHang, kh.tenKhachHang " +
                     "ORDER BY kh.idKhachHang";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + input + "%");
            ps.setDate(2, new java.sql.Date(start.getTime()));
            ps.setDate(3, new java.sql.Date(end.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeKhachHangDTO dto = new ThongKeKhachHangDTO(
                        rs.getInt("idKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getInt("soluongphieu"),
                        rs.getLong("tongtien")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeNhaCungCapDTO> getAllNCC() {
        ArrayList<ThongKeNhaCungCapDTO> result = new ArrayList<>();
        String sql = "SELECT ncc.idNhaCungCap, ncc.tenNhaCungCap, " +
                     "COUNT(hdn.idHoaDonNhap) AS soluong, " +
                     "COALESCE(SUM(hdn.tongTien), 0) AS tongtien " +
                     "FROM NhaCungCap ncc " +
                     "LEFT JOIN HoaDonNhap hdn ON ncc.idNhaCungCap = hdn.idNhaCungCap " +
                     "GROUP BY ncc.idNhaCungCap, ncc.tenNhaCungCap " +
                     "ORDER BY ncc.idNhaCungCap";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNhaCungCapDTO dto = new ThongKeNhaCungCapDTO(
                        rs.getString("idNhaCungCap"),
                        rs.getString("tenNhaCungCap"),
                        rs.getInt("soluong"),
                        rs.getLong("tongtien")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeNhaCungCapDTO> filterNCC(String input, Date start, Date end) {
        ArrayList<ThongKeNhaCungCapDTO> result = new ArrayList<>();
        String sql = "SELECT ncc.idNhaCungCap, ncc.tenNhaCungCap, " +
                     "COUNT(hdn.idHoaDonNhap) AS soluong, " +
                     "COALESCE(SUM(hdn.tongTien), 0) AS tongtien " +
                     "FROM NhaCungCap ncc " +
                     "LEFT JOIN HoaDonNhap hdn ON ncc.idNhaCungCap = hdn.idNhaCungCap " +
                     "WHERE ncc.tenNhaCungCap LIKE ? AND (hdn.ngayTao BETWEEN ? AND ? OR hdn.ngayTao IS NULL) " +
                     "GROUP BY ncc.idNhaCungCap, ncc.tenNhaCungCap " +
                     "ORDER BY ncc.idNhaCungCap";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + input + "%");
            ps.setDate(2, new java.sql.Date(start.getTime()));
            ps.setDate(3, new java.sql.Date(end.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNhaCungCapDTO dto = new ThongKeNhaCungCapDTO(
                        rs.getString("idNhaCungCap"),
                        rs.getString("tenNhaCungCap"),
                        rs.getInt("soluong"),
                        rs.getLong("tongtien")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTonKhoDTO> getTonKho() {
        ArrayList<ThongKeTonKhoDTO> result = new ArrayList<>();
        String sql = "SELECT sp.idSanPham, sp.tenSanPham, " +
                     "COALESCE((SELECT COUNT(ctsp.SerialNumber) FROM ChiTietSP ctsp " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "WHERE plsp.idSanPham = sp.idSanPham), 0) AS tondauky, " +
                     "COALESCE((SELECT COUNT(ctdn.SN) FROM ChiTietDonNhap ctdn " +
                     "JOIN ChiTietSP ctsp ON ctdn.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "WHERE plsp.idSanPham = sp.idSanPham), 0) AS nhaptrongky, " +
                     "COALESCE((SELECT COUNT(cthdx.SN) FROM ChiTietHoaDonXuat cthdx " +
                     "JOIN ChiTietSP ctsp ON cthdx.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "WHERE plsp.idSanPham = sp.idSanPham), 0) AS xuattrongky, " +
                     "COALESCE((SELECT COUNT(ctdn.SN) FROM ChiTietDonNhap ctdn " +
                     "JOIN ChiTietSP ctsp ON ctdn.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "WHERE plsp.idSanPham = sp.idSanPham), 0) - " +
                     "COALESCE((SELECT COUNT(cthdx.SN) FROM ChiTietHoaDonXuat cthdx " +
                     "JOIN ChiTietSP ctsp ON cthdx.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "WHERE plsp.idSanPham = sp.idSanPham), 0) AS toncuoiky " +
                     "FROM SanPham sp " +
                     "ORDER BY sp.idSanPham";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeTonKhoDTO dto = new ThongKeTonKhoDTO(
                        rs.getString("idSanPham"),
                        rs.getString("tenSanPham"),
                        rs.getInt("tondauky"),
                        rs.getInt("nhaptrongky"),
                        rs.getInt("xuattrongky"),
                        rs.getInt("toncuoiky")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTonKhoDTO> filterTonKho(String input, Date start, Date end) {
        ArrayList<ThongKeTonKhoDTO> result = new ArrayList<>();
        String sql = "SELECT sp.idSanPham, sp.tenSanPham, " +
                     "COALESCE((SELECT COUNT(ctsp.SerialNumber) FROM ChiTietSP ctsp " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "WHERE plsp.idSanPham = sp.idSanPham), 0) AS tondauky, " +
                     "COALESCE((SELECT COUNT(ctdn.SN) FROM ChiTietDonNhap ctdn " +
                     "JOIN ChiTietSP ctsp ON ctdn.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "JOIN HoaDonNhap hdn ON ctdn.idDonHang = hdn.idHoaDonNhap " +
                     "WHERE plsp.idSanPham = sp.idSanPham AND hdn.ngayTao BETWEEN ? AND ?), 0) AS nhaptrongky, " +
                     "COALESCE((SELECT COUNT(cthdx.SN) FROM ChiTietHoaDonXuat cthdx " +
                     "JOIN ChiTietSP ctsp ON cthdx.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "JOIN HoaDonXuat hdx ON cthdx.idHoaDonXuat = hdx.idHoaDonXuat " +
                     "WHERE plsp.idSanPham = sp.idSanPham AND hdx.ngayTao BETWEEN ? AND ?), 0) AS xuattrongky, " +
                     "COALESCE((SELECT COUNT(ctdn.SN) FROM ChiTietDonNhap ctdn " +
                     "JOIN ChiTietSP ctsp ON ctdn.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "JOIN HoaDonNhap hdn ON ctdn.idDonHang = hdn.idHoaDonNhap " +
                     "WHERE plsp.idSanPham = sp.idSanPham AND hdn.ngayTao <= ?), 0) - " +
                     "COALESCE((SELECT COUNT(cthdx.SN) FROM ChiTietHoaDonXuat cthdx " +
                     "JOIN ChiTietSP ctsp ON cthdx.SN = ctsp.SerialNumber " +
                     "JOIN PhanLoaiSP plsp ON ctsp.idPhanLoai = plsp.idPhanLoai " +
                     "JOIN HoaDonXuat hdx ON cthdx.idHoaDonXuat = hdx.idHoaDonXuat " +
                     "WHERE plsp.idSanPham = sp.idSanPham AND hdx.ngayTao <= ?), 0) AS toncuoiky " +
                     "FROM SanPham sp " +
                     "WHERE sp.tenSanPham LIKE ? " +
                     "ORDER BY sp.idSanPham";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(start.getTime()));
            ps.setDate(2, new java.sql.Date(end.getTime()));
            ps.setDate(3, new java.sql.Date(start.getTime()));
            ps.setDate(4, new java.sql.Date(end.getTime()));
            ps.setDate(5, new java.sql.Date(end.getTime()));
            ps.setDate(6, new java.sql.Date(end.getTime()));
            ps.setString(7, "%" + input + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeTonKhoDTO dto = new ThongKeTonKhoDTO(
                        rs.getString("idSanPham"),
                        rs.getString("tenSanPham"),
                        rs.getInt("tondauky"),
                        rs.getInt("nhaptrongky"),
                        rs.getInt("xuattrongky"),
                        rs.getInt("toncuoiky")
                    );
                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}