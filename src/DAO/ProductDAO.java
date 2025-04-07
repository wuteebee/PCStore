package DAO;

import config.DatabaseConnection;
import DTO.Brand;
import DTO.Catalog;
import DTO.Product;
import DTO.Variant;

import java.sql.*;
import java.util.*;

public class ProductDAO {
    private Connection conn;

    public ProductDAO() {
        conn = DatabaseConnection.getConnection();
    }

    // Lấy tất cả các Variant, trả về dạng Map<idSanPham, List<Variant>>
    public Map<String, List<Variant>> getVariantsGroupedByProduct() {
        Map<String, List<Variant>> map = new HashMap<>();
        String sql = "SELECT * FROM phanloaisp";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String idVariant = rs.getString("idPhanLoai");
                String idSanPham = rs.getString("idSanPham");
                String phienBan = rs.getString("STTPL");
                double gia = rs.getDouble("Gia");
                int soLuong = rs.getInt("soLuongTonKho");

                Variant variant = new Variant(idVariant, phienBan, gia, soLuong);

                map.computeIfAbsent(idSanPham, k -> new ArrayList<>()).add(variant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        AtributeDAO atributeDAO = new AtributeDAO();
        List<Brand> brands = atributeDAO.getAllBrand();
        List<Catalog> categories = atributeDAO.getAllCatalogs();
        Map<String, List<Variant>> variantsByProduct = getVariantsGroupedByProduct();

        // Tạo map tra nhanh Brand/Catalog
        Map<String, Brand> brandMap = new HashMap<>();
        for (Brand b : brands) brandMap.put(b.getMaThuongHieu(), b);

        Map<String, Catalog> catalogMap = new HashMap<>();
        for (Catalog c : categories) catalogMap.put(c.getMaDanhMuc(), c);

        String sql = "SELECT * FROM sanpham";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("idSanPham");
                String name = rs.getString("tenSanPham");
                String mota = rs.getString("moTaSanPham");
                String anh = rs.getString("anhSanPham");
                double gia = rs.getDouble("Gia");
                boolean trangThai = rs.getBoolean("trangThai");
                String idDM = rs.getString("idDanhMuc");
                String idTH = rs.getString("idThuongHieu");

                Product product = new Product(id,name,catalogMap.get(idDM),brandMap.get(idTH),mota,anh,trangThai);
                product.setDanhSachPhienBan(variantsByProduct.getOrDefault(id, new ArrayList<>()));
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}
