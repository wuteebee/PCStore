package config;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DatabaseConnection {
    private static final String URL = "jdbc:h2:file:./src/config/PCStoreTest;DATABASE_TO_UPPER=FALSE;DB_CLOSE_ON_EXIT=FALSE;CASE_INSENSITIVE_IDENTIFIERS=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static boolean initialized = false;
    private static boolean created = false;
    private static Server webServer;

    // Tạp database và thêm sample data trước
    public static void main(String[] args) {
        H2DatabaseConnection conn = new H2DatabaseConnection();
        if (!initialized) {
            initialized = true;
            if (!created) {
                conn.createDatabase(conn.getConnection());
                created = true;
            }
        }
    }

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to H2 database.");

            if (webServer == null) {
                try {
                    webServer = Server.createWebServer("-web", "-webPort", "8082").start();
                    System.out.println("Web server started on port: http://localhost:" + webServer.getPort());
                } catch (SQLException e) {
                    System.err.println("Error starting web server: " + e.getMessage());
                }
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Error connecting: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private static void createDatabase(Connection conn) {
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
                "CREATE TABLE IF NOT EXISTS ChiTietSP (SerialNumber VARCHAR(50) PRIMARY KEY, idPhanLoai INT, trangThai INT DEFAULT 1, FOREIGN KEY (idPhanLoai) REFERENCES PhanLoaiSP(idPhanLoai));",
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
            insertSampleData(conn);
        } catch (SQLException e) {
            System.err.println("Error creating H2 database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating database!", e);
        }
    }

    private static void insertSampleData(Connection conn) {
        String[] insertStatements = {
                // DanhMuc
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('Laptop', 'Laptop', NULL);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM002', 'PC', NULL);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM003', 'Linh kiện', NULL);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM004', 'CPU', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM005', 'GPU', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM006', 'RAM', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM007', 'Storage', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM008', 'ManHinh', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM009', 'Case', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM010', 'Nguồn', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM011', 'Mainboard', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM012', 'Tản nhiệt', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM013', 'SSD', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM014', 'Card âm thanh', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM015', 'Card mạng', 'DM003');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM016', 'Dell', 'Laptop');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM017', 'HP', 'Laptop');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM018', 'Lenovo', 'Laptop');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM019', 'Asus', 'Laptop');",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES ('DM020', 'Apple', 'Laptop');",

                // ThuongHieu
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH001', 'Apple', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH002', 'Dell', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH003', 'HP', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH004', 'Lenovo', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH005', 'Asus', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH006', 'Corsair', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH007', 'NZXT', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH008', 'Fractal Design', 'DM009');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH009', 'Cooler Master', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH010', 'Phanteks', 'DM009');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH011', 'DeepCool', 'DM012');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH012', 'Seasonic', 'DM010');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH013', 'EVGA', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH014', 'Cooler Master', 'DM010');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH015', 'Intel', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH016', 'AMD', 'DM004');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH017', 'NVIDIA', 'DM005');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH018', 'G.Skill', 'DM006');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH019', 'Kingston', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH020', 'Western Digital', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH021', 'Seagate', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH022', 'Crucial', 'DM007');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH023', 'Samsung', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH024', 'Acer', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH025', 'MSI', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH026', 'Gigabyte', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH027', 'ASRock', 'DM011');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH028', 'Noctua', 'DM012');",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH029', 'Creative', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH030', 'BlasterX', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH031', 'FiiO', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH032', 'TP-Link', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH033', 'D-Link', NULL);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc) VALUES ('TH034', 'custom', 'DM002');",

                // ThongTinKyThuat
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'CPU', 'Bộ vi xử lý');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'GPU', 'Card đồ họa');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'RAM', 'Bộ nhớ RAM');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'Storage', 'Bộ nhớ lưu trữ');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'ManHinh', 'Màn hình');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'TanSoQuet', 'Tần số quét màn hình');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'Pin', 'Dung lượng pin');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'CanNang', 'Trọng lượng');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'CongKetNoi', 'Cổng kết nối');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'WiFi', 'Chuẩn WiFi');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'Bluetooth', 'Phiên bản Bluetooth');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'HeDieuHanh', 'Hệ điều hành');",

                // SanPham
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('LAP001', 'Laptop', 'Apple MacBook Pro 14-inch', 'TH001', 56799000, 'Laptop Apple MacBook Pro 14-inch với chip M1 Pro, màn hình Retina 14-inch');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('LAP002', 'Laptop', 'Dell XPS 13', 'TH002', 35199000, 'Laptop Dell XPS 13 với màn hình InfinityEdge, vi xử lý Intel Core i7');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('LAP003', 'Laptop', 'HP Spectre x360', 'TH003', 37199000, 'Laptop HP Spectre x360 với màn hình cảm ứng, vi xử lý Intel Core i7');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('LAP004', 'Laptop', 'Lenovo ThinkPad X1 Carbon', 'TH004', 42599000, 'Laptop Lenovo ThinkPad X1 Carbon, màn hình 14-inch, vi xử lý Intel Core i7');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('LAP005', 'Laptop', 'Asus ZenBook 13', 'TH005', 25199000, 'Laptop Asus ZenBook 13, vi xử lý Intel Core i5, màn hình 13-inch');",

                // CauHinhLaptop
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP001', 'CPU', 'Apple M1 Pro 8-core');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP001', 'RAM', '16GB RAM');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP001', 'Storage', '512GB SSD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP001', 'ManHinh', '14-inch Retina Display');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP001', 'Pin', '17 hours battery life');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP002', 'CPU', 'Intel Core i7-1165G7');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP002', 'RAM', '8GB RAM');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP002', 'Storage', '512GB SSD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP002', 'ManHinh', '13.4-inch InfinityEdge');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP002', 'Pin', '12 hours battery life');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP003', 'CPU', 'Intel Core i7-1165G7');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP003', 'RAM', '16GB RAM');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP003', 'Storage', '1TB SSD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP003', 'ManHinh', '13.3-inch Full HD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP003', 'Pin', '13 hours battery life');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP004', 'CPU', 'Intel Core i7-1165G7');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP004', 'RAM', '16GB RAM');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP004', 'Storage', '512GB SSD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP004', 'ManHinh', '14-inch Full HD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP004', 'Pin', '15 hours battery life');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP005', 'CPU', 'Intel Core i5-1135G7');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP005', 'RAM', '8GB RAM');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP005', 'Storage', '256GB SSD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP005', 'ManHinh', '13.3-inch Full HD');",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin) VALUES ('LAP005', 'Pin', '10 hours battery life');",

                // SanPham for CPU
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP001', 'DM004', 'Intel Core i9-13900K', 'TH015', 7490000, 'Bộ vi xử lý Intel Core i9-13900K, 16 lõi, 24 luồng, xung nhịp tối đa 5.8GHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP002', 'DM004', 'AMD Ryzen 9 7900X', 'TH016', 5890000, 'Bộ vi xử lý AMD Ryzen 9 7900X, 12 lõi, 24 luồng, xung nhịp tối đa 5.6GHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP003', 'DM004', 'Intel Core i7-12700K', 'TH015', 4990000, 'Bộ vi xử lý Intel Core i7-12700K, 12 lõi, 20 luồng, xung nhịp tối đa 5.0GHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP004', 'DM004', 'AMD Ryzen 7 5800X', 'TH016', 3990000, 'Bộ vi xử lý AMD Ryzen 7 5800X, 8 lõi, 16 luồng, xung nhịp tối đa 4.7GHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP005', 'DM004', 'Intel Core i5-12600K', 'TH015', 3290000, 'Bộ vi xử lý Intel Core i5-12600K, 6 lõi, 12 luồng, xung nhịp tối đa 4.9GHz');",

                // SanPham for GPU
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP006', 'DM005', 'NVIDIA GeForce RTX 3080', 'TH017', 6990000, 'Card đồ họa NVIDIA GeForce RTX 3080, 10GB GDDR6X');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP007', 'DM005', 'AMD Radeon RX 6900 XT', 'TH016', 7990000, 'Card đồ họa AMD Radeon RX 6900 XT, 16GB GDDR6');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP008', 'DM005', 'NVIDIA GeForce RTX 3070', 'TH017', 4990000, 'Card đồ họa NVIDIA GeForce RTX 3070, 8GB GDDR6');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP009', 'DM005', 'AMD Radeon RX 6800 XT', 'TH016', 5990000, 'Card đồ họa AMD Radeon RX 6800 XT, 16GB GDDR6');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP010', 'DM005', 'NVIDIA GeForce RTX 3060', 'TH017', 3990000, 'Card đồ họa NVIDIA GeForce RTX 3060, 12GB GDDR6');",

                // SanPham for RAM
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP011', 'DM006', 'Corsair Vengeance LPX 16GB', 'TH006', 799000, 'RAM Corsair Vengeance LPX 16GB (2x8GB), DDR4 3200MHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP012', 'DM006', 'G.Skill Ripjaws V 32GB', 'TH018', 1499000, 'RAM G.Skill Ripjaws V 32GB (2x16GB), DDR4 3600MHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP013', 'DM006', 'Kingston HyperX Fury 16GB', 'TH019', 699000, 'RAM Kingston HyperX Fury 16GB (2x8GB), DDR4 3200MHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP014', 'DM006', 'Corsair Dominator Platinum 32GB', 'TH006', 1999000, 'RAM Corsair Dominator Platinum 32GB (2x16GB), DDR4 3200MHz');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP015', 'DM006', 'G.Skill Trident Z RGB 16GB', 'TH018', 899000, 'RAM G.Skill Trident Z RGB 16GB (2x8GB), DDR4 3600MHz');",

                // SanPham for Storage
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP016', 'DM007', 'Samsung 970 EVO Plus 1TB', 'TH020', 3490000, 'SSD Samsung 970 EVO Plus 1TB, NVMe M.2, tốc độ đọc 3500MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP017', 'DM007', 'Western Digital Black SN850 500GB', 'TH022', 2490000, 'SSD WD Black SN850 500GB, PCIe Gen 4.0, tốc độ đọc 7000MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP018', 'DM007', 'Seagate FireCuda 520 1TB', 'TH021', 2990000, 'SSD Seagate FireCuda 520 1TB, NVMe M.2, tốc độ đọc 5000MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP019', 'DM007', 'Kingston KC2500 500GB', 'TH019', 1990000, 'SSD Kingston KC2500 500GB, NVMe M.2, tốc độ đọc 3500MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP020', 'DM007', 'Corsair MP600 1TB', 'TH006', 3490000, 'SSD Corsair MP600 1TB, PCIe Gen 4.0, tốc độ đọc 4950MB/s');",

                // SanPham for Monitor
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP021', 'DM008', 'LG 27GN950-B', 'TH023', 7990000, 'Màn hình LG 27GN950-B, 27 inch, 4K UHD, 144Hz, IPS');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP022', 'DM008', 'Dell Alienware AW2521H', 'TH002', 5490000, 'Màn hình Dell Alienware AW2521H, 24.5 inch, Full HD, 360Hz, IPS');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP023', 'DM008', 'Samsung Odyssey G7 32', 'TH023', 6990000, 'Màn hình cong Samsung Odyssey G7 32, 2560x1440, 240Hz, QLED');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP024', 'DM008', 'Asus ROG Swift PG259QN', 'TH005', 5990000, 'Màn hình Asus ROG Swift PG259QN, 24.5 inch, Full HD, 360Hz, IPS');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP025', 'DM008', 'Acer Predator X27', 'TH024', 9990000, 'Màn hình Acer Predator X27, 27 inch, QHD, 165Hz, IPS');",

                // SanPham for SSD
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP036', 'DM013', 'Samsung 990 PRO 1TB', 'TH023', 4790000, 'SSD Samsung 990 PRO 1TB, NVMe M.2, tốc độ đọc 7450MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP037', 'DM013', 'Kingston NV2 1TB', 'TH019', 2190000, 'SSD Kingston NV2 1TB, NVMe M.2, tốc độ đọc 3500MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP038', 'DM013', 'Western Digital SN770 1TB', 'TH020', 2990000, 'SSD WD SN770 1TB, NVMe M.2, tốc độ đọc 5150MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP039', 'DM013', 'Seagate FireCuda 530 1TB', 'TH021', 5490000, 'SSD Seagate FireCuda 530 1TB, NVMe PCIe 4.0, tốc độ đọc 7300MB/s');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP040', 'DM013', 'Crucial P5 Plus 500GB', 'TH022', 1890000, 'SSD Crucial P5 Plus 500GB, NVMe PCIe 4.0, tốc độ đọc 6600MB/s');",

                // SanPham for Sound Card
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP041', 'DM014', 'Creative Sound Blaster AE-7', 'TH029', 4290000, 'Card âm thanh Creative Sound Blaster AE-7, PCIe, Hi-Res Audio');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP042', 'DM014', 'ASUS Xonar AE', 'TH005', 1990000, 'Card âm thanh ASUS Xonar AE, 7.1, hỗ trợ Hi-Res');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP043', 'DM014', 'EVGA NU Audio Pro', 'TH013', 5290000, 'Card âm thanh EVGA NU Audio Pro, DAC chất lượng cao, PCIe');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP044', 'DM014', 'Sound BlasterX G6', 'TH030', 3490000, 'Card âm thanh Sound BlasterX G6, hỗ trợ gaming, DAC chất lượng cao');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP045', 'DM014', 'FiiO K5 Pro', 'TH031', 3890000, 'Amp/DAC FiiO K5 Pro, hỗ trợ Hi-Res Audio, phù hợp audiophile');",

                // SanPham for Network Card
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP046', 'DM015', 'ASUS PCE-AX3000', 'TH005', 1290000, 'Card mạng ASUS PCE-AX3000, WiFi 6, Bluetooth 5.0, PCIe');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP047', 'DM015', 'TP-Link Archer TX3000E', 'TH032', 1090000, 'Card mạng TP-Link Archer TX3000E, WiFi 6, Bluetooth 5.0, PCIe');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP048', 'DM015', 'Intel AX200', 'TH015', 990000, 'Card mạng Intel AX200, WiFi 6, Bluetooth 5.1, M.2 module');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP049', 'DM015', 'Gigabyte GC-WBAX200', 'TH026', 1190000, 'Card mạng Gigabyte GC-WBAX200, WiFi 6, Bluetooth 5.0, PCIe');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP050', 'DM015', 'D-Link DGE-560T', 'TH033', 790000, 'Card mạng D-Link DGE-560T, Gigabit Ethernet PCIe, hỗ trợ VLAN');",

                // SanPham for Case
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP026', 'DM009', 'NZXT H510', 'TH007', 1800000, 'Case NZXT H510, Mid Tower, hỗ trợ ATX, thiết kế tối giản');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP027', 'DM009', 'Corsair 4000D Airflow', 'TH010', 2300000, 'Case Corsair 4000D Airflow, Mid Tower, thiết kế tối ưu luồng khí');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP028', 'DM009', 'Fractal Design Meshify C', 'TH011', 2500000, 'Case Fractal Design Meshify C, Mid Tower, thiết kế lưới, hỗ trợ ATX');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP029', 'DM009', 'Cooler Master MasterBox Q300L', 'TH009', 1200000, 'Case Cooler Master MasterBox Q300L, MicroATX, hỗ trợ các kích thước bo mạch nhỏ');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP030', 'DM009', 'Phanteks Eclipse P400A', 'TH013', 2000000, 'Case Phanteks Eclipse P400A, Mid Tower, thiết kế thông gió tốt');",

                // SanPham for Cooling
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP031', 'DM012', 'Noctua NH-D15', 'TH030', 2590000, 'Tản nhiệt khí Noctua NH-D15, hiệu năng cao, phù hợp CPU mạnh');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP032', 'DM012', 'Corsair iCUE H150i ELITE', 'TH006', 3990000, 'Tản nhiệt nước Corsair iCUE H150i ELITE, 360mm, RGB');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP033', 'DM012', 'NZXT Kraken X73', 'TH007', 3490000, 'Tản nhiệt nước NZXT Kraken X73, 360mm, RGB LED');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP034', 'DM012', 'DeepCool AS500 Plus', 'TH011', 1290000, 'Tản nhiệt khí DeepCool AS500 Plus, 140mm, hiệu suất cao');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('SP035', 'DM012', 'Cooler Master Hyper 212', 'TH009', 890000, 'Tản nhiệt khí Cooler Master Hyper 212, 120mm, phổ biến');",

                // SanPham for PC
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('PC001', 'DM002', 'PC Gaming High-End', 'TH006', 25000000, 'Máy tính chơi game cao cấp với cấu hình mạnh mẽ');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('PC002', 'DM002', 'PC Workstation', 'TH002', 20000000, 'Máy tính làm việc chuyên nghiệp, ổn định');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('PC003', 'DM002', 'PC Văn Phòng', 'TH003', 12000000, 'Máy tính cho công việc văn phòng, cấu hình vừa phải');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('PC004', 'DM002', 'PC Mini ITX', 'TH007', 18000000, 'PC nhỏ gọn nhưng hiệu năng cao cho không gian hạn chế');",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) VALUES ('PC005', 'DM002', 'PC Custom Build', 'TH034', 30000000, 'Máy tính được lắp ráp theo yêu cầu, tối ưu hóa hiệu suất');",

                // CauHinhPC
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC001', 'CPU', 'SP001', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC001', 'RAM', 'SP011', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC001', 'GPU', 'SP006', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC002', 'CPU', 'SP003', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC002', 'RAM', 'SP012', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC002', 'GPU', 'SP007', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC003', 'CPU', 'SP005', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC003', 'RAM', 'SP013', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC003', 'GPU', 'SP008', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC004', 'CPU', 'SP004', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC004', 'RAM', 'SP011', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC004', 'GPU', 'SP009', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC005', 'CPU', 'SP005', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC005', 'RAM', 'SP014', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC005', 'GPU', 'SP010', 0);",

                // NhaCungCap
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC001', 'Công ty Linh Kiện ABC', '123 Lê Lợi, Q.1, TP.HCM', '0909123456', 'abc@linhkien.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC002', 'Thiết Bị Điện Tử Hưng Phát', '56 Nguyễn Trãi, Q.5, TP.HCM', '0912345678', 'hungphat@tb.com', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC003', 'Phát Triển Công Nghệ Việt', '789 Trường Chinh, Hà Nội', '0988777666', 'info@viettech.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC004', 'Vinatech Solutions', '45 Lạc Long Quân, Đà Nẵng', '0933555777', 'sales@vinatech.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC005', 'Linh Kiện Sài Gòn', '101 Cách Mạng Tháng Tám, Q.3, TP.HCM', '0909888777', 'contact@lksaigon.vn', 1);"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String sql : insertStatements) {
                stmt.execute(sql);
            }
            System.out.println("Sample data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting sample data: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error inserting sample data!", e);
        }
    }

    public static void shutdown() {
        if (webServer != null) {
            webServer.stop();
            System.out.println("Web server stopped.");
        }
        try {
            DriverManager.getConnection("jdbc:h2:file:./src/config/PCStoreTest;SHUTDOWN=TRUE", USER, PASSWORD);
            System.out.println("Database shutdown completed.");
        } catch (SQLException e) {
            System.err.println("Error shutting down database: " + e.getMessage());
        }
    }
}