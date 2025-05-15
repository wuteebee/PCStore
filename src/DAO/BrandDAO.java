package DAO;

import DTO.Brand;
import config.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class BrandDAO {
    private Connection conn = DatabaseConnection.getConnection();

    public List<Brand> getAllBrands() {
        List<Brand> list = new ArrayList<>();
        String sql = "SELECT * FROM ThuongHieu";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Brand(
                    rs.getString("idThuongHieu"),
                    rs.getString("tenThuongHieu"),
                    rs.getString("idDanhMuc"),
                    rs.getInt("trangThai") == 1
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Brand brand) {
        String sql = "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand.getMaThuongHieu());
            ps.setString(2, brand.getTenThuongHieu());
            ps.setString(3, brand.getmaDanhMuc());
            ps.setInt(4, brand.isTrangThai() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Brand brand) {
        String sql = "UPDATE ThuongHieu SET tenThuongHieu = ?, idDanhMuc = ?, trangThai = ? WHERE idThuongHieu = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand.getTenThuongHieu());
            ps.setString(2, brand.getmaDanhMuc());
            ps.setInt(3, brand.isTrangThai() ? 1 : 0);
            ps.setString(4, brand.getMaThuongHieu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id) {
        // Trước khi xóa, kiểm tra xem thương hiệu có đang được dùng trong bảng sản phẩm không
        if (isBrandInUse(id)) {
            return false; // Không được xóa nếu còn ràng buộc
        }

        String sql = "DELETE FROM ThuongHieu WHERE idThuongHieu = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isBrandInUse(String brandId) {
        String sql = "SELECT COUNT(*) FROM SanPham WHERE idThuongHieu = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brandId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; // nếu xảy ra lỗi, mặc định không cho xóa
    }

    public Brand getById(String id) {
        String sql = "SELECT * FROM ThuongHieu WHERE idThuongHieu = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Brand(
                    rs.getString("idThuongHieu"),
                    rs.getString("tenThuongHieu"),
                    rs.getString("idDanhMuc"),
                    rs.getInt("trangThai") == 1
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}