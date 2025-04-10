package DAO;

import config.DatabaseConnection;
import DTO.*;

import java.sql.*;
import java.util.*;

public class ProductDAO {

    public List<Product> getAllProductsOptimized() {
        List<Product> products = new ArrayList<>();
        Map<String, Product> productMap = new HashMap<>();

        AtributeDAO atributeDAO = new AtributeDAO();
        List<Brand> brands = atributeDAO.getAllBrand();
        List<Catalog> catalogs = atributeDAO.getAllCatalogs();

        Map<String, Brand> brandMap = new HashMap<>();
        for (Brand b : brands) brandMap.put(b.getMaThuongHieu(), b);

        Map<String, Catalog> catalogMap = new HashMap<>();
        for (Catalog c : catalogs) catalogMap.put(c.getMaDanhMuc(), c);

        String sql = "SELECT sp.*, pl.idPhanLoai, pl.STTPL, pl.Gia AS giaPhienBan, pl.soLuongTonKho " +
                     "FROM sanpham sp LEFT JOIN phanloaisp pl ON sp.idSanPham = pl.idSanPham";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String idSP = rs.getString("idSanPham");

                Product product = productMap.get(idSP);
                if (product == null) {
                    String name = rs.getString("tenSanPham");
                    String mota = rs.getString("moTaSanPham");
                    String anh = rs.getString("anhSanPham");
                    boolean trangThai = rs.getBoolean("trangThai");
                    String idDM = rs.getString("idDanhMuc");
                    String idTH = rs.getString("idThuongHieu");

                    product = new Product(idSP, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh, trangThai);
                    product.setDanhSachPhienBan(new ArrayList<>());
                    productMap.put(idSP, product);
                }

                String idVariant = rs.getString("idPhanLoai");
                if (idVariant != null) {
                    String phienBan = rs.getString("STTPL");
                    double gia = rs.getDouble("giaPhienBan");
                    int soLuong = rs.getInt("soLuongTonKho");

                    Variant variant = new Variant(idVariant, phienBan, gia, soLuong);

                    String maDanhMuc = product.getDanhMuc().getMaDanhMuc();
                    List<ChiTietCauHinh> chiTiet = getCauhinhTrucTiep(idSP, phienBan, maDanhMuc);
                    variant.setChitiet(chiTiet);

                    product.getDanhSachPhienBan().add(variant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        products.addAll(productMap.values());
        return products;
    }

    public Product getProductByIdFull(String id) {
        Product product = null;

        AtributeDAO atributeDAO = new AtributeDAO();
        Map<String, Brand> brandMap = new HashMap<>();
        for (Brand b : atributeDAO.getAllBrand()) brandMap.put(b.getMaThuongHieu(), b);

        Map<String, Catalog> catalogMap = new HashMap<>();
        for (Catalog c : atributeDAO.getAllCatalogs()) catalogMap.put(c.getMaDanhMuc(), c);

        String sql = "SELECT * FROM sanpham WHERE idSanPham = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("tenSanPham");
                String mota = rs.getString("moTaSanPham");
                String anh = rs.getString("anhSanPham");
                boolean trangThai = rs.getBoolean("trangThai");
                String idDM = rs.getString("idDanhMuc");
                String idTH = rs.getString("idThuongHieu");

                product = new Product(id, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh, trangThai);

                List<Variant> variants = new ArrayList<>();
                String sqlVariants = "SELECT * FROM phanloaisp WHERE idSanPham = ?";
                try (PreparedStatement stmtVariants = conn.prepareStatement(sqlVariants)) {
                    stmtVariants.setString(1, id);
                    ResultSet rsVar = stmtVariants.executeQuery();
                    while (rsVar.next()) {
                        String idPL = rsVar.getString("idPhanLoai");
                        String phienBan = rsVar.getString("STTPL");
                        double gia = rsVar.getDouble("Gia");
                        int soLuong = rsVar.getInt("soLuongTonKho");

                        Variant variant = new Variant(idPL, phienBan, gia, soLuong);
                        List<ChiTietCauHinh> ct = getCauhinhTrucTiep(id, phienBan, product.getDanhMuc().getMaDanhMuc());
                        variant.setChitiet(ct);
                        variants.add(variant);
                    }
                }

                product.setDanhSachPhienBan(variants);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        AtributeDAO atributeDAO = new AtributeDAO();
        List<Brand> brands = atributeDAO.getAllBrand();
        List<Catalog> categories = atributeDAO.getAllCatalogs();

        Map<String, Brand> brandMap = new HashMap<>();
        for (Brand b : brands) brandMap.put(b.getMaThuongHieu(), b);

        Map<String, Catalog> catalogMap = new HashMap<>();
        for (Catalog c : categories) catalogMap.put(c.getMaDanhMuc(), c);

        String sql = "SELECT * FROM sanpham";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("idSanPham");
                String name = rs.getString("tenSanPham");
                String mota = rs.getString("moTaSanPham");
                String anh = rs.getString("anhSanPham");
                boolean trangThai = rs.getBoolean("trangThai");
                String idDM = rs.getString("idDanhMuc");
                String idTH = rs.getString("idThuongHieu");

                Product product = new Product(id, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh, trangThai);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<ChiTietCauHinh> getCauhinhTrucTiep(String idSP, String phienBan, String maDanhMuc) {
        List<ChiTietCauHinh> ctch = new ArrayList<>();

        String sql;
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (maDanhMuc.equals("Laptop")) {
                sql = "SELECT * FROM cauhinhlaptop WHERE idSanPham = ? AND STTPL = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, idSP);
                    pstmt.setInt(2, Integer.parseInt(phienBan));
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String idThongTin = rs.getString("idThongTin");
                        String thongTin = rs.getString("ThongTin");
                        ctch.add(new CauHinhLaptop(idSP, idThongTin, Integer.parseInt(phienBan), thongTin));
                    }
                }
            } else if (maDanhMuc.equals("PC")) {
                sql = "SELECT * FROM cauhinhpc WHERE idSanPham = ? AND STTPL = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, idSP);
                    pstmt.setInt(2, Integer.parseInt(phienBan));
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String idThongTin = rs.getString("idThongTin");
                        String idLinhKien = rs.getString("idLinhKien");

                        Product linhKien = getSanPhamLite(idLinhKien);
                        ctch.add(new CauHinhPC(idSP, idThongTin, Integer.parseInt(phienBan), linhKien));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ctch;
    }

    public Product getSanPhamLite(String id) {
        Product product = null;
        String sql = "SELECT * FROM sanpham WHERE idSanPham = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("tenSanPham");
                String mota = rs.getString("moTaSanPham");
                String anh = rs.getString("anhSanPham");
                boolean trangThai = rs.getBoolean("trangThai");

                product = new Product(id, name, null, null, mota, anh, trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    public Product getProductById(String id) {
        for (Product p : getAllProductsOptimized()) {
            if (p.getMaSp().equals(id)) return p;
        }
        return null;
    }
}
