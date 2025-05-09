package DAO;

import config.DatabaseConnection;
import DTO.*;

import java.math.BigDecimal;
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
                    double gia = rs.getDouble("Gia");

                    product = new Product(idSP, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh,gia, trangThai);
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
                double gia = rs.getDouble("Gia");

                product = new Product(id, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh,gia, trangThai);

                List<Variant> variants = new ArrayList<>();
                String sqlVariants = "SELECT * FROM phanloaisp WHERE idSanPham = ?";
                try (PreparedStatement stmtVariants = conn.prepareStatement(sqlVariants)) {
                    stmtVariants.setString(1, id);
                    ResultSet rsVar = stmtVariants.executeQuery();
                    while (rsVar.next()) {
                        String idPL = rsVar.getString("idPhanLoai");
                        String phienBan = rsVar.getString("STTPL");
                         gia = rsVar.getDouble("Gia");
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
                double giasp = rs.getDouble("Gia");

                Product product = new Product(id, name, catalogMap.get(idDM), brandMap.get(idTH), mota, anh,giasp, trangThai);

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
                    //    System.out.println("ID: " + idThongTin + ", Thong Tin: " + thongTin + "Phiên bản: "+ phienBan); ;
                        ctch.add(new CauHinhLaptop(idSP, idThongTin, Integer.parseInt(phienBan), thongTin));
                        // System.out.println("ID: " + idThongTin + ", Thong Tin: " + thongTin);
                    }
                }
            } else if (maDanhMuc.equals("DM002")) {
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
                double gia = rs.getDouble("Gia");

                product = new Product(id, name, null, null, mota, anh, gia,trangThai);
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


    public List<ProductDetail> getAllProductDetails(Integer id) {
        List<ProductDetail> productDetails = new ArrayList<>();
    
        String sql = (id == null)
            ? "SELECT * FROM chitietsp"
            : "SELECT * FROM chitietsp WHERE idPhanLoai = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            if (id != null) {
                ps.setInt(1, id);
            }
    
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String serialNumber = rs.getString("SerialNumber");
                    int idSP = rs.getInt("idPhanLoai");
                    double giaNhap = rs.getDouble("giaNhap");
                    boolean trangThai = rs.getBoolean("trangThai");
                    
                    ProductDetail tmp = new ProductDetail(serialNumber, idSP, giaNhap, trangThai);
                    productDetails.add(tmp);
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        if(id==null){
            return productDetails;
        }

        List<ProductDetail> danhsach= new ArrayList<>();
   
        for (ProductDetail p : productDetails) {
             if(p.getIdPhanLoai()==id){
                danhsach.add(p);
             }
        }

        return danhsach;
        
    }
    
    public boolean isProductExist(String idSanPham) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM SanPham WHERE idSanPham = ?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSanPham);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0; // Nếu đếm được > 0, tức là mã sản phẩm đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        }

        return exists;
    }
    // Hàm lấy danh mục cha
    public String getDanhMucCha(String idDanhMuc) {
        String sql = "SELECT idDanhMucCha, tenDanhMuc FROM DanhMuc WHERE idDanhMuc = ?";
        String danhMucCha = null;
    
        try (Connection conn = DatabaseConnection.getConnection() ;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idDanhMuc); 
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String idDanhMucCha = rs.getString("idDanhMucCha");
                
                if (idDanhMucCha != null) {
                    danhMucCha=idDanhMucCha;
                } else {
                    danhMucCha = idDanhMuc;
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
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true nếu tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public String RandomIdSP(String id) {
        String prefix;
    
        if ("Laptop".equals(id)) {
            prefix = "Laptop";
        } else if ("DM002".equals(id)) {
            prefix = "PC";
        } else {
            prefix = "SP";
        }
    
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
            return rowsInserted > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
}
