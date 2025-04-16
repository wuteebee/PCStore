import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DatabaseConnection {
    private static final String URL = "jdbc:h2:file:./src/config/PCStoreTest;DATABASE_TO_UPPER=FALSE;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static public void main (String[] args) {
        Connection a = getConnection();
        insertSampleData(a);
    }

    public static Connection getConnection() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to H2 database.");
            String[] sqlStatements = {
                    "CREATE TABLE IF NOT EXISTS ChucNang (idChucNang VARCHAR(20) PRIMARY KEY, tenChucNang VARCHAR(50) NOT NULL);",
                    "CREATE TABLE IF NOT EXISTS NhomQuyen (idNhomQuyen VARCHAR(20) PRIMARY KEY, tenNhomQuyen VARCHAR(50) NOT NULL, trangThai INT DEFAULT 1);",
                    "CREATE TABLE IF NOT EXISTS ChiTietQuyen (idNhomQuyen VARCHAR(20) NOT NULL, idChucNang VARCHAR(20) NOT NULL, hanhDong VARCHAR(5) NOT NULL, PRIMARY KEY (idNhomQuyen, idChucNang), FOREIGN KEY (idNhomQuyen) REFERENCES NhomQuyen(idNhomQuyen), FOREIGN KEY (idChucNang) REFERENCES ChucNang(idChucNang));",
                    "CREATE TABLE IF NOT EXISTS DanhMuc (idDanhMuc VARCHAR(20) PRIMARY KEY, tenDanhMuc VARCHAR(50) NOT NULL, idDanhMucCha VARCHAR(20), trangThai INT DEFAULT 1, FOREIGN KEY (idDanhMucCha) REFERENCES DanhMuc(idDanhMuc));",
                    "CREATE TABLE IF NOT EXISTS ThuongHieu (idThuongHieu VARCHAR(20) PRIMARY KEY, tenThuongHieu VARCHAR(50) NOT NULL, idDanhMuc VARCHAR(20), trangThai INT DEFAULT 1, CONSTRAINT fk_thuonghieu_danhmuc FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc));",
                    "CREATE TABLE IF NOT EXISTS NhaCungCap (idNhaCungCap VARCHAR(20) PRIMARY KEY, tenNhaCungCap VARCHAR(255) NOT NULL, diaChi VARCHAR(500), soDienThoai VARCHAR(20), email VARCHAR(255), trangThai INT DEFAULT 1);",
                    "CREATE TABLE IF NOT EXISTS SanPham (idSanPham VARCHAR(20) PRIMARY KEY, idDanhMuc VARCHAR(20) NOT NULL, tenSanPham VARCHAR(255) NOT NULL, idThuongHieu VARCHAR(20) NOT NULL, Gia DECIMAL(12,2) NOT NULL, moTaSanPham VARCHAR(1000), anhSanPham BLOB, trangThai INT DEFAULT 1, FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc), FOREIGN KEY (idThuongHieu) REFERENCES ThuongHieu(idThuongHieu));",
                    "CREATE TABLE IF NOT EXISTS ThongTinKyThuat (idDanhMuc VARCHAR(20) NOT NULL, idThongTin VARCHAR(20) PRIMARY KEY, tenThongTin VARCHAR(50) NOT NULL, FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc));",
                    "CREATE TABLE IF NOT EXISTS CauHinhLaptop (idSanPham VARCHAR(20), idThongTin VARCHAR(20), ThongTin VARCHAR(255), STTPL INT DEFAULT 0, PRIMARY KEY (idSanPham, idThongTin, STTPL), FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham), FOREIGN KEY (idThongTin) REFERENCES ThongTinKyThuat(idThongTin));",
                    "CREATE TABLE IF NOT EXISTS CauHinhPC (idSanPham VARCHAR(20), idThongTin VARCHAR(20), idLinhKien VARCHAR(20), STTPL INT DEFAULT 0, PRIMARY KEY (idSanPham, idThongTin, idLinhKien, STTPL), FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham), FOREIGN KEY (idThongTin) REFERENCES ThongTinKyThuat(idThongTin), FOREIGN KEY (idLinhKien) REFERENCES SanPham(idSanPham));",
                    "CREATE TABLE IF NOT EXISTS PhanLoaiSP (idPhanLoai INT PRIMARY KEY, idSanPham VARCHAR(20), STTPL INT DEFAULT 0, Gia DECIMAL(12,2), soLuongTonKho INT DEFAULT 0, FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham));",
                    "CREATE TABLE IF NOT EXISTS ChiTietSP (SerialNumber VARCHAR(50) PRIMARY KEY, idPhanLoai INT, giaNhap DECIMAL(12,2), trangThai INT DEFAULT 1, FOREIGN KEY (idPhanLoai) REFERENCES PhanLoaiSP(idPhanLoai));",
                    "CREATE TABLE IF NOT EXISTS KhuyenMai (idKhuyenMai VARCHAR(20) PRIMARY KEY, giaTri DECIMAL(10,2) NOT NULL);",
                    "CREATE TABLE IF NOT EXISTS KhuyenMaiCombo (idKhuyenMai VARCHAR(20) NOT NULL, idSanPham VARCHAR(20) NOT NULL, PRIMARY KEY (idKhuyenMai, idSanPham), FOREIGN KEY (idKhuyenMai) REFERENCES KhuyenMai(idKhuyenMai), FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham));",
                    "CREATE TABLE IF NOT EXISTS NhanVien (idNhanVien VARCHAR(20) PRIMARY KEY, TenNhanVien VARCHAR(50) NOT NULL, SDT VARCHAR(20), Mail VARCHAR(255), NgayVaoLam DATE, ViTri VARCHAR(50), Luong DECIMAL(10,2), trangThai INT DEFAULT 1);",
                    "CREATE TABLE IF NOT EXISTS TaiKhoan (idTaiKhoan VARCHAR(20) PRIMARY KEY, idNhanVien VARCHAR(20) NOT NULL, anhDaiDien BLOB, idNhomQuyen VARCHAR(20) NOT NULL, matKhau VARCHAR(50) NOT NULL, trangThai INT DEFAULT 1, FOREIGN KEY (idNhomQuyen) REFERENCES NhomQuyen(idNhomQuyen), FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien));",
                    "CREATE TABLE IF NOT EXISTS KhachHang (idKhachHang INT AUTO_INCREMENT PRIMARY KEY, tenKhachHang VARCHAR(50) NOT NULL, soDienThoai VARCHAR(20) NOT NULL, Mail VARCHAR(255), NgayThamGia DATE, trangThai INT DEFAULT 1);",
                    "CREATE TABLE IF NOT EXISTS HoaDonXuat (idHoaDonXuat VARCHAR(20) PRIMARY KEY, idNhanVien VARCHAR(20) NOT NULL, idKhachHang INT NOT NULL, ngayTao DATE NOT NULL, tongTien DECIMAL(17,2) NOT NULL, idKhuyenMai VARCHAR(20), FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien), FOREIGN KEY (idKhachHang) REFERENCES KhachHang(idKhachHang), FOREIGN KEY (idKhuyenMai) REFERENCES KhuyenMai(idKhuyenMai));",
                    "CREATE TABLE IF NOT EXISTS ChiTietHoaDonXuat (idChiTietHoaDonXuat VARCHAR(20) PRIMARY KEY, idHoaDonXuat VARCHAR(20) NOT NULL, SN VARCHAR(50) NOT NULL, donGia DECIMAL(12,2) NOT NULL, FOREIGN KEY (idHoaDonXuat) REFERENCES HoaDonXuat(idHoaDonXuat), FOREIGN KEY (SN) REFERENCES ChiTietSP(SerialNumber));",
                    "CREATE TABLE IF NOT EXISTS HoaDonNhap (idHoaDonNhap VARCHAR(20) PRIMARY KEY, idNhanVien VARCHAR(20) NOT NULL, idNhaCungCap VARCHAR(20) NOT NULL, ngayTao DATE NOT NULL, tongTien DECIMAL(10,2) NOT NULL, FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien), FOREIGN KEY (idNhaCungCap) REFERENCES NhaCungCap(idNhaCungCap));",
                    "CREATE TABLE IF NOT EXISTS ChiTietDonNhap (idDonHang INT, SN VARCHAR(50) NOT NULL, donGia DECIMAL(12,2) NOT NULL, thanhTien DECIMAL(15,2) NOT NULL, PRIMARY KEY (idDonHang, SN), FOREIGN KEY (SN) REFERENCES ChiTietSP(SerialNumber));"
            };
            try (Statement stmt = conn.createStatement()) {
                for (String sql : sqlStatements) {
                    stmt.execute(sql);
                }
                System.out.println("Tables created successfully.");
            }
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("Web server started on port: " + "http://localhost:" + webServer.getPort());
            return conn;
        } catch (SQLException e) {
            System.err.println("Error creating H2 database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error craeting!", e);
        }
    }
    private static void insertSampleData(Connection conn) {
        String[] insertStatements = {
                // Insert data into ChucNang
                "INSERT INTO ChucNang (idChucNang, tenChucNang) VALUES ('CN001', 'Quản lý người dùng');",
                "INSERT INTO ChucNang (idChucNang, tenChucNang) VALUES ('CN002', 'Quản lý sản phẩm');",
                "INSERT INTO ChucNang (idChucNang, tenChucNang) VALUES ('CN003', 'Quản lý hóa đơn');",

                // Insert data into NhomQuyen
                "INSERT INTO NhomQuyen (idNhomQuyen, tenNhomQuyen, trangThai) VALUES ('NQ001', 'Quản trị viên', 1);",
                "INSERT INTO NhomQuyen (idNhomQuyen, tenNhomQuyen, trangThai) VALUES ('NQ002', 'Nhân viên', 1);",
                "INSERT INTO NhomQuyen (idNhomQuyen, tenNhomQuyen, trangThai) VALUES ('NQ003', 'Khách hàng', 1);",

                // Insert data into ChiTietQuyen
//                "INSERT INTO ChiTietQuyen (idNhomQuyen, idChucNang, hanhDong) VALUES ('NQ001', 'CN001', 'READ');",
//                "INSERT INTO ChiTietQuyen (idNhomQuyen, idChucNang, hanhDong) VALUES ('NQ001', 'CN001', 'WRITE');",
//                "INSERT INTO ChiTietQuyen (idNhomQuyen, idChucNang, hanhDong) VALUES ('NQ001', 'CN002', 'READ');",
//                "INSERT INTO ChiTietQuyen (idNhomQuyen, idChucNang, hanhDong) VALUES ('NQ001', 'CN002', 'WRITE');",
//                "INSERT INTO ChiTietQuyen (idNhomQuyen, idChucNang, hanhDong) VALUES ('NQ001', 'CN003', 'READ');",
//                "INSERT INTO ChiTietQuyen (idNhomQuyen, idChucNang, hanhDong) VALUES ('NQ001', 'CN003', 'WRITE');",

                // Insert data into DanhMuc
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('Laptop', 'Laptop', NULL, 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM002', 'PC', NULL, 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM003', 'Linh kiện', NULL, 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM004', 'CPU', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM005', 'GPU', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM006', 'RAM', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM007', 'Storage', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM008', 'Màn hình', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM009', 'Case', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM010', 'Nguồn', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM011', 'Mainboard', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM012', 'Tản nhiệt', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM013', 'SSD', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM014', 'Card âm thanh', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM015', 'Card mạng', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM016', 'Dell', 'Laptop', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM017', 'HP', 'Laptop', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM018', 'Lenovo', 'Laptop', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM019', 'Asus', 'Laptop', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM020', 'Apple', 'Laptop', 1);",

                // Insert data into ThuongHieu
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH001', 'Intel', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH002', 'AMD', 'DM004', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH003', 'NVIDIA', 'DM005', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH004', 'G.Skill', 'DM006', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH005', 'Western Digital', 'DM007', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH006', 'Corsair', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH007', 'HP', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH008', 'Dell', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH009', 'Lenovo', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH010', 'Asus', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH011', 'Apple', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH012', 'Cooler Master', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH013', 'EVGA', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH014', 'MSI', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH015', 'Gigabyte', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH016', 'ASRock', 'DM011', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH017', 'Noctua', 'DM012', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH018', 'Creative', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH019', 'BlasterX', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH020', 'FiiO', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH021', 'TP-Link', 'DM015', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH022', 'D-Link', 'DM015', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH023', 'Samsung', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH024', 'Acer', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH025', 'Crucial', 'DM007', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH026', 'Seagate', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH027', 'Kingston', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH028', 'Western Digital', 'DM007', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH029', 'Fractal Design', 'DM009', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH030', 'Cooler Master', 'DM009', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH031', 'Phanteks', 'DM009', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH032', 'DeepCool', 'DM012', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH033', 'Seasonic', 'DM010', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH034', 'Corsair', 'DM010', 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH035', 'custom', 'DM002', 1);"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String sql : insertStatements) {
                stmt.execute(sql);
            }
            System.out.println("Sample data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting H2 database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error inserting!", e);
        }
    }
}
