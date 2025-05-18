
package DAO;

import DTO.*;
import config.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ProductDAO {

    public List<Product> getAllProductsOptimized() {
        List<Product> products = new ArrayList<>();
        Map<String, Product> productMap = new HashMap<>();

        AtributeDAO atributeDAO = new AtributeDAO();
        Map<String, Brand> brandMap = new HashMap<>();
        for (Brand b : atributeDAO.getAllBrand()) brandMap.put(b.getMaThuongHieu(), b);

        Map<String, Catalog> catalogMap = new HashMap<>();
        for (Catalog c : atributeDAO.getAllCatalogs()) catalogMap.put(c.getMaDanhMuc(), c);

        String sql = "SELECT sp.*, pl.idPhanLoai, pl.STTPL, pl.Gia AS giaPhienBan, pl.soLuongTonKho, pl.trangThai AS variantTrangThai " +
                     "FROM sanpham sp LEFT JOIN phanloaisp pl ON sp.idSanPham = pl.idSanPham";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
                    double gia = rs.getDouble("Gia");

                    product = new Product(idSP, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh, gia, trangThai);
                    product.setDanhSachPhienBan(new ArrayList<>());
                    productMap.put(idSP, product);
                }

                int idVariant = rs.getInt("idPhanLoai");
                if (!rs.wasNull()) {
                    int phienBan = rs.getInt("STTPL");
                    double giaPhienBan = rs.getDouble("giaPhienBan");
                    int soLuong = rs.getInt("soLuongTonKho");
                    boolean tthai = rs.getBoolean("variantTrangThai");
                    System.out.println("Trạng thái hiện tại: " + tthai);
                    Variant variant = new Variant(idVariant, phienBan, giaPhienBan, soLuong, tthai);

                    String maDanhMuc = product.getDanhMuc().getMaDanhMuc();
                    List<ChiTietCauHinh> chiTiet = getCauhinhTrucTiep(conn, idSP, phienBan, maDanhMuc);
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
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("tenSanPham");
                    String mota = rs.getString("moTaSanPham");
                    String anh = rs.getString("anhSanPham");
                    boolean trangThai = rs.getBoolean("trangThai");
                    String idDM = rs.getString("idDanhMuc");
                    String idTH = rs.getString("idThuongHieu");
                    double gia = rs.getDouble("Gia");

                    product = new Product(id, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh, gia, trangThai);

                    List<Variant> variants = new ArrayList<>();
                    String sqlVariants = "SELECT * FROM phanloaisp WHERE idSanPham = ?";
                    try (PreparedStatement stmtVariants = conn.prepareStatement(sqlVariants)) {
                        stmtVariants.setString(1, id);
                        try (ResultSet rsVar = stmtVariants.executeQuery()) {
                            while (rsVar.next()) {
                                int idPL = rsVar.getInt("idPhanLoai");
                                int phienBan = rsVar.getInt("STTPL");
                                double giaPhienBan = rsVar.getDouble("Gia");
                                int soLuong = rsVar.getInt("soLuongTonKho");
                                boolean tt = rsVar.getBoolean("trangThai");
                                System.out.println("Trạng thái hiện tại: " + tt);
                                Variant variant = new Variant(idPL, phienBan, giaPhienBan, soLuong, tt);
                                List<ChiTietCauHinh> ct = getCauhinhTrucTiep(conn, id, phienBan, product.getDanhMuc().getMaDanhMuc());
                                variant.setChitiet(ct);
                                variants.add(variant);
                            }
                        }
                    }

                    product.setDanhSachPhienBan(variants);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        AtributeDAO atributeDAO = new AtributeDAO();
        Map<String, Brand> brandMap = new HashMap<>();
        for (Brand b : atributeDAO.getAllBrand()) brandMap.put(b.getMaThuongHieu(), b);

        Map<String, Catalog> catalogMap = new HashMap<>();
        for (Catalog c : atributeDAO.getAllCatalogs()) catalogMap.put(c.getMaDanhMuc(), c);

        String sql = "SELECT * FROM sanpham";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("idSanPham");
                String name = rs.getString("tenSanPham");
                String mota = rs.getString("moTaSanPham");
                String anh = rs.getString("anhSanPham");
                boolean trangThai = rs.getBoolean("trangThai");
                String idDM = rs.getString("idDanhMuc");
                String idTH = rs.getString("idThuongHieu");
                double giasp = rs.getDouble("Gia");

                Product product = new Product(id, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh, giasp, trangThai);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<ChiTietCauHinh> getCauhinhTrucTiep(Connection conn, String idSP, int phienBan, String maDanhMuc) {
        List<ChiTietCauHinh> ctch = new ArrayList<>();
        boolean createdLocalConnection = false;

        try {
            if (conn == null) {
                conn = DatabaseConnection.getConnection();
                createdLocalConnection = true;
            }

            String sql;
            if ("Laptop".equals(maDanhMuc)) {
                sql = "SELECT * FROM cauhinhlaptop WHERE idSanPham = ? AND STTPL = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, idSP);
                    pstmt.setInt(2, phienBan);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            String idThongTin = rs.getString("idThongTin");
                            String thongTin = rs.getString("ThongTin");
                            ctch.add(new CauHinhLaptop(idSP, idThongTin, phienBan, thongTin));
                        }
                    }
                }
            } else if ("DM002".equals(maDanhMuc)) {
                sql = "SELECT * FROM cauhinhpc WHERE idSanPham = ? AND STTPL = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, idSP);
                    pstmt.setInt(2, phienBan);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        List<String> linhKienIds = new ArrayList<>();
                        while (rs.next()) {
                            String idThongTin = rs.getString("idThongTin");
                            String idLinhKien = rs.getString("idLinhKien");
                            linhKienIds.add(idLinhKien);
                            ctch.add(new CauHinhPC(idSP, idThongTin, phienBan, null)); // Temporarily null
                        }
                        if (!linhKienIds.isEmpty()) {
                            List<Product> linhKiens = getSanPhamLiteBatch(conn, linhKienIds);
                            for (int i = 0; i < ctch.size(); i++) {
                                ((CauHinhPC) ctch.get(i)).setLinhKien(linhKiens.get(i));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (createdLocalConnection && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return ctch;
    }

    private List<Product> getSanPhamLiteBatch(Connection conn, List<String> ids) {
        List<Product> products = new ArrayList<>();
        if (ids.isEmpty()) return products;

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT * FROM sanpham WHERE idSanPham IN (" + placeholders + ")";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                pstmt.setString(i + 1, ids.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("idSanPham");
                    String name = rs.getString("tenSanPham");
                    String mota = rs.getString("moTaSanPham");
                    String anh = rs.getString("anhSanPham");
                    boolean trangThai = rs.getBoolean("trangThai");
                    double gia = rs.getDouble("Gia");

                    products.add(new Product(id, name, null, null, mota, anh, gia, trangThai));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Product getSanPhamLite(String id) {
        return getSanPhamLiteBatch(DatabaseConnection.getConnection(), Collections.singletonList(id)).stream().findFirst().orElse(null);
    }

    public Product getProductById(String id) {
        for (Product p : getAllProductsOptimized()) {
            if (p.getMaSp().equals(id)) return p;
        }
        return null;
    }

    public List<ProductDetail> getAllProductDetails(Integer id) {
        List<ProductDetail> productDetails = new ArrayList<>();

        String sql = id == null ? "SELECT * FROM chitietsp" : "SELECT * FROM chitietsp WHERE idPhanLoai = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (id != null) {
                ps.setInt(1, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String serialNumber = rs.getString("SerialNumber");
                    int idSP = rs.getInt("idPhanLoai");
                    boolean trangThai = rs.getBoolean("trangThai");
                    String maphieunhap = rs.getString("maphieunhap");
                    String maphieuxuat = rs.getString("maphieuxuat");

                    ProductDetail tmp = new ProductDetail(serialNumber, idSP, idSP, trangThai, maphieunhap, maphieuxuat);
                    productDetails.add(tmp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id == null) {
            return productDetails;
        }

        List<ProductDetail> danhsach = new ArrayList<>();
        for (ProductDetail p : productDetails) {
            if (p.getIdPhanLoai() == id) {
                danhsach.add(p);
            }
        }

        return danhsach;
    }

    public boolean isProductExist(String idSanPham) {
        String sql = "SELECT COUNT(*) FROM SanPham WHERE idSanPham = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSanPham);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getDanhMucCha(String idDanhMuc) {
        String sql = "SELECT idDanhMucCha FROM DanhMuc WHERE idDanhMuc = ?";
        String danhMucCha = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idDanhMuc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String idDanhMucCha = rs.getString("idDanhMucCha");
                    danhMucCha = idDanhMucCha != null ? idDanhMucCha : idDanhMuc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhMucCha;
    }

    public boolean checkMaSPDaTonTai(String idsp) {
        String sql = "SELECT idSanPham FROM SanPham WHERE idSanPham = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idsp);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Product> getAllProductsbyCT(String idDM) {
        List<Product> ds = new ArrayList<>();

        String sql = "SELECT * FROM sanpham WHERE idDanhMuc = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idDM);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setMaSp(rs.getString("idSanPham"));
                    p.setTenSp(rs.getString("tenSanPham"));
                    ds.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public String RandomIdSP(String id) {
        String prefix = "Laptop".equals(id) ? "Laptop" : "DM002".equals(id) ? "PC" : "SP";
        String idsp;
        Random rand = new Random();
        int attempt = 0;
        do {
            int randomNum = 100 + rand.nextInt(900);
            idsp = prefix + randomNum;
            attempt++;
        } while (checkMaSPDaTonTai(idsp) && attempt < 10);

        return idsp;
    }

    public boolean insertSP(Product sp) {
        String idsp = RandomIdSP(getDanhMucCha(sp.getDanhMuc().getMaDanhMuc()));
        String sql = "INSERT INTO SanPham (idSanPham, tenSanPham, idDanhMuc, idThuongHieu, moTaSanPham, anhSanPham, Gia, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idsp);
            stmt.setString(2, sp.getTenSp());
            stmt.setString(3, sp.getDanhMuc().getMaDanhMuc());
            stmt.setString(4, sp.getThuongHieu().getMaThuongHieu());
            stmt.setString(5, sp.getMoTaSanPham());
            stmt.setString(6, sp.getAnhSanPham());
            stmt.setDouble(7, sp.getGiasp());
            stmt.setBoolean(8, sp.isTrangThai());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Inserted product: " + idsp);
                insertplsp(conn, idsp, 0, sp.getGiasp(), 0);
            }
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertplsp(Connection conn, String idsp, int STTPL, double gia, int soLuongTonKho) {
        String sql = "INSERT INTO phanloaisp (idSanPham, STTPL, Gia, soLuongTonKho) VALUES (?, ?, ?, ?)";
        boolean createdLocalConnection = false;

        try {
            if (conn == null) {
                conn = DatabaseConnection.getConnection();
                createdLocalConnection = true;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, idsp);
                stmt.setInt(2, STTPL);
                stmt.setDouble(3, gia);
                stmt.setInt(4, soLuongTonKho);

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            if (createdLocalConnection && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getIDPhanLoai(String idsp, int STTPL) {
        String sql = "SELECT idPhanLoai FROM phanloaisp WHERE idSanPham = ? AND STTPL = ?";
        int idPhanLoai = 1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idsp);
            stmt.setInt(2, STTPL);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idPhanLoai = rs.getInt("idPhanLoai");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idPhanLoai;
    }

    public boolean updateplsp(String idPhanLoai, String idsp, int STTPL, double gia, int soLuongTonKho, boolean trangThai) {
        String sql = "UPDATE phanloaisp SET Gia = ?, soLuongTonKho = ?, idSanPham = ?, STTPL = ?, trangThai = ? WHERE idPhanLoai = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, gia);
            stmt.setInt(2, soLuongTonKho);
            stmt.setString(3, idsp);
            stmt.setInt(4, STTPL);
            stmt.setBoolean(5, trangThai);
            stmt.setString(6, idPhanLoai);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertCauHinhPC(String idSP, String idThongTin, String idLinhKien, int STTPL) {
        String sql = "INSERT INTO cauhinhpc (idSanPham, idThongTin, idLinhKien, STTPL) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSP);
            stmt.setString(2, idThongTin);
            stmt.setString(3, idLinhKien);
            stmt.setInt(4, STTPL);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertCauHinh(String idSP, String idThongTin, String ThongTin, int STTPL) {
        String sql = "INSERT INTO cauhinhlaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSP);
            stmt.setString(2, idThongTin);
            stmt.setString(3, ThongTin);
            stmt.setInt(4, STTPL);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product sp) {
        String sql = "UPDATE sanpham SET tenSanPham = ?, idDanhMuc = ?, idThuongHieu = ?, moTaSanPham = ?, anhSanPham = ?, Gia = ? WHERE idSanPham = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sp.getTenSp());
            stmt.setString(2, sp.getDanhMuc().getMaDanhMuc());
            stmt.setString(3, sp.getThuongHieu().getMaThuongHieu());
            stmt.setString(4, sp.getMoTaSanPham());
            stmt.setString(5, sp.getAnhSanPham());
            stmt.setDouble(6, sp.getGiasp());
            stmt.setString(7, sp.getMaSp());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String id) {
        String sql = "UPDATE sanpham SET trangThai = ? WHERE idSanPham = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, false);
            stmt.setString(2, id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCauhinhLaptop(String idSp, int STTPL) {
        String sql = "DELETE FROM cauhinhlaptop WHERE idSanPham = ? AND STTPL = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSp);
            stmt.setInt(2, STTPL);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCauhinhPC(String idSp, int STTPL) {
        String sql = "DELETE FROM cauhinhpc WHERE idSanPham = ? AND STTPL = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSp);
            stmt.setInt(2, STTPL);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTrangThaiplsp(int idPhanLoai, boolean trangThai) {
        String sql = "UPDATE phanloaisp SET trangThai = ? WHERE idPhanLoai = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, trangThai);
            stmt.setInt(2, idPhanLoai);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getProductIDbyMaPhanLoai(int idPhanLoai) {
        String sql = "SELECT idSanPham FROM phanloaisp WHERE idPhanLoai = ?";
        String masp = "";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhanLoai);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    masp = rs.getString("idSanPham");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return masp;
    }

    public int getphienbanbyIdPL(int idPhanLoai) {
        String sql = "SELECT STTPL FROM phanloaisp WHERE idPhanLoai = ?";
        int phienBan = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhanLoai);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    phienBan = rs.getInt("STTPL");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return phienBan;
    }

    public void updateTatCaSoLuongTonKho() {
        String sql = "UPDATE phanloaisp pl SET soluongtonkho = (" +
                     "SELECT COUNT(*) FROM chitietsp ct WHERE ct.idphanloai = pl.idphanloai " +
                     "AND ct.maphieunhap IS NOT NULL AND (ct.maphieuxuat IS NULL OR ct.maphieuxuat = '-1'))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Updated " + rowsUpdated + " rows in phanloaisp for inventory.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Lay danh sach chi tiet san pham con trong kho va chua duoc xuat
    public List<ProductDetail> getAvailProductForInvoice()
    {
        List<ProductDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietsp WHERE maphieuxuat = -1";
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String serialNumber = rs.getString("SerialNumber");
                int idPhanLoai = rs.getInt("idPhanLoai");
                String maphieuxuat = rs.getString("maphieuxuat");
                String maphieunhap = rs.getString("maphieunhap");
                boolean trangThai = rs.getBoolean("trangThai");
                ProductDetail productDetail = new ProductDetail(serialNumber, idPhanLoai, 0,  trangThai, maphieunhap, maphieuxuat);
                list.add(productDetail);
            }

            conn.close();
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductDetail> getAvailProductForInvoiceEdit()
    {
        List<ProductDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietsp WHERE maphieuxuat != -1";
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String serialNumber = rs.getString("SerialNumber");
                int idPhanLoai = rs.getInt("idPhanLoai");
                String maphieuxuat = rs.getString("maphieuxuat");
                String maphieunhap = rs.getString("maphieunhap");
                boolean trangThai = rs.getBoolean("trangThai");
                ProductDetail productDetail = new ProductDetail(serialNumber, idPhanLoai, 0,  trangThai, maphieunhap, maphieuxuat);
                list.add(productDetail);
            }

            conn.close();
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Lay ten san pham theo ID phan loai
    public String getProductNamebyMaPhanLoai(int idPhanLoai){
        String sql = "SELECT tenSanPham FROM sanpham, phanloaisp where idphanloai = ? and phanloaisp.idsanpham = sanpham.idsanpham;";
        String tenSP="Không tìm thấy";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhanLoai);


            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tenSP =  rs.getString("tenSanPham");
                return tenSP;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenSP;
    }

    public double getProductPricebyMaPhanLoai(int idPhanLoai){
        String sql = "SELECT Gia FROM phanloaisp where idphanloai = ?;";
        double gia = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhanLoai);


            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                gia =  rs.getDouble("Gia");
                return gia;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gia;
    }

    public boolean updateChiTietMaPhieuXuat(String serialNumber, String idPhieuXuat) {
        String sql = "UPDATE chitietsp SET maphieuxuat = ? WHERE SerialNumber = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPhieuXuat);
            stmt.setString(2, serialNumber);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
