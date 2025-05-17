package DAO;

import DTO.ComboProduct;
import DTO.Promotion;
import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {
    private Connection conn;

    public PromotionDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE trangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Promotion promotion = new Promotion(
                    rs.getString("idKhuyenMai"),
                    rs.getString("tenKhuyenMai"),
                    rs.getDouble("giaTri"),
                    rs.getDate("ngayBatDau") != null ? rs.getDate("ngayBatDau").toLocalDate() : null,
                    rs.getDate("ngayKetThuc") != null ? rs.getDate("ngayKetThuc").toLocalDate() : null,
                    rs.getString("loai"),
                    rs.getInt("trangThai")
                );
                promotions.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    public boolean insertPromotion(Promotion promotion) {
        String sql = "INSERT INTO KhuyenMai (idKhuyenMai, tenKhuyenMai, giaTri, ngayBatDau, ngayKetThuc, loai, trangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promotion.getIdKhuyenMai());
            ps.setString(2, promotion.getTenKhuyenMai());
            ps.setDouble(3, promotion.getGiaTri());
            ps.setDate(4, promotion.getNgayBatDau() != null ? Date.valueOf(promotion.getNgayBatDau()) : null);
            ps.setDate(5, promotion.getNgayKetThuc() != null ? Date.valueOf(promotion.getNgayKetThuc()) : null);
            ps.setString(6, promotion.getLoai());
            ps.setInt(7, promotion.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePromotion(Promotion promotion) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, giaTri = ?, ngayBatDau = ?, ngayKetThuc = ?, loai = ? " +
                     "WHERE idKhuyenMai = ? AND trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promotion.getTenKhuyenMai());
            ps.setDouble(2, promotion.getGiaTri());
            ps.setDate(3, promotion.getNgayBatDau() != null ? Date.valueOf(promotion.getNgayBatDau()) : null);
            ps.setDate(4, promotion.getNgayKetThuc() != null ? Date.valueOf(promotion.getNgayKetThuc()) : null);
            ps.setString(5, promotion.getLoai());
            ps.setString(6, promotion.getIdKhuyenMai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePromotion(String idKhuyenMai) {
        String sql = "UPDATE KhuyenMai SET trangThai = 0 WHERE idKhuyenMai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idKhuyenMai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ComboProduct> getComboProducts(String idKhuyenMai) {
        List<ComboProduct> products = new ArrayList<>();
        String sql = "SELECT kmc.idKhuyenMai, kmc.idSanPham, sp.tenSanPham " +
                     "FROM KhuyenMaiCombo kmc JOIN SanPham sp ON kmc.idSanPham = sp.idSanPham " +
                     "WHERE kmc.idKhuyenMai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idKhuyenMai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ComboProduct product = new ComboProduct(
                        rs.getString("idKhuyenMai"),
                        rs.getString("idSanPham"),
                        rs.getString("tenSanPham")
                    );
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean insertComboProduct(ComboProduct product) {
        String sql = "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getIdKhuyenMai());
            ps.setString(2, product.getIdSanPham());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteComboProducts(String idKhuyenMai) {
        String sql = "DELETE FROM KhuyenMaiCombo WHERE idKhuyenMai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idKhuyenMai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ComboProduct> getAllProducts() {
        List<ComboProduct> products = new ArrayList<>();
        String sql = "SELECT idSanPham, tenSanPham FROM SanPham WHERE trangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ComboProduct product = new ComboProduct(null, rs.getString("idSanPham"), rs.getString("tenSanPham"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }



 public Promotion getPromotionById(String id) {
    String sql = "SELECT * FROM KhuyenMai WHERE idKhuyenMai = ? AND trangThai = 1";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Promotion(
                    rs.getString("idKhuyenMai"),
                    rs.getString("tenKhuyenMai"),
                    rs.getDouble("giaTri"),
                    rs.getDate("ngayBatDau") != null ? rs.getDate("ngayBatDau").toLocalDate() : null,
                    rs.getDate("ngayKetThuc") != null ? rs.getDate("ngayKetThuc").toLocalDate() : null,
                    rs.getString("loai"),
                    rs.getInt("trangThai")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}

