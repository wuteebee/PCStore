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
                // NhomQuyen
                "INSERT INTO NhomQuyen (idNhomQuyen, tenNhomQuyen) VALUES ('NQ001', 'Admin');",
                "INSERT INTO NhomQuyen (idNhomQuyen, tenNhomQuyen) VALUES ('NQ002', 'Staff');",
                "INSERT INTO NhomQuyen (idNhomQuyen, tenNhomQuyen) VALUES ('NQ003', 'User');",
                // ThuongHieu
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH001', 'Apple', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH002', 'Dell', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH003', 'HP', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH004', 'Lenovo', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH005', 'Asus', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH006', 'Corsair', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH007', 'NZXT', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH008', 'Fractal Design', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH009', 'Cooler Master', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH010', 'Phanteks', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH011', 'DeepCool', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH012', 'Seasonic', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH013', 'EVGA', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH014', 'Intel', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH015', 'AMD', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH016', 'NVIDIA', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH017', 'G.Skill', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH018', 'Kingston', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH019', 'Western Digital', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH020', 'Seagate', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH021', 'Crucial', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH022', 'Samsung', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH023', 'LG', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH024', 'Acer', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH025', 'MSI', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH026', 'Gigabyte', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH027', 'ASRock', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH028', 'Noctua', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH029', 'Creative', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH030', 'BlasterX', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH031', 'FiiO', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH032', 'TP-Link', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH033', 'D-Link', NULL, 1);",
                "INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc, trangThai) VALUES ('TH034', 'Custom', NULL, 1);",

                // DanhMuc
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('Laptop', 'Laptop', NULL, 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM002', 'PC', NULL, 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM003', 'Linh kiện', NULL, 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM004', 'CPU', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM005', 'GPU', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM006', 'RAM', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM007', 'Storage', 'DM003', 1);",
                "INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai) VALUES ('DM008', 'ManHinh', 'DM003', 1);",
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

                // ThongTinKyThuat
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'CPU', 'Bộ vi xử lý');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'GPU', 'Card đồ họa');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'RAM', 'Bộ nhớ RAM');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'Storage', 'Bộ nhớ lưu trữ');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'ManHinh', 'Màn hình');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'Pin', 'Dung lượng pin');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'CanNang', 'Trọng lượng');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'CongKetNoi', 'Cổng kết nối');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'WiFi', 'Chuẩn WiFi');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'Bluetooth', 'Phiên bản Bluetooth');",
                "INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES ('Laptop', 'HeDieuHanh', 'Hệ điều hành');",

                // SanPham
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP001', 'Laptop', 'Apple MacBook Pro 13-inch', 'TH001', 29999000, 'Laptop Apple MacBook Pro 13-inch với chip Intel Core i5, màn hình Retina 13-inch', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP002', 'Laptop', 'Dell XPS 13', 'TH002', 25199000, 'Laptop Dell XPS 13 với màn hình InfinityEdge, vi xử lý Intel Core i5', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP003', 'Laptop', 'HP Spectre x360', 'TH003', 27199000, 'Laptop HP Spectre x360 với màn hình cảm ứng, vi xử lý Intel Core i5', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP004', 'Laptop', 'Lenovo ThinkPad X1 Carbon', 'TH004', 28599000, 'Laptop Lenovo ThinkPad X1 Carbon, màn hình 14-inch, vi xử lý Intel Core i5', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP005', 'Laptop', 'Asus ZenBook 13', 'TH005', 19199000, 'Laptop Asus ZenBook 13, vi xử lý Intel Core i5, màn hình 13-inch', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP001', 'DM004', 'Intel Core i7-4790K', 'TH014', 7990000, 'Bộ vi xử lý Intel Core i7-4790K, 4 lõi, 8 luồng, xung nhịp tối đa 4.0GHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP002', 'DM004', 'AMD FX-8350', 'TH015', 3990000, 'Bộ vi xử lý AMD FX-8350, 8 lõi, 8 luồng, xung nhịp tối đa 4.0GHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP003', 'DM004', 'Intel Core i5-4690K', 'TH014', 5990000, 'Bộ vi xử lý Intel Core i5-4690K, 4 lõi, 4 luồng, xung nhịp tối đa 3.9GHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP004', 'DM005', 'NVIDIA GeForce GTX 980', 'TH016', 12990000, 'Card đồ họa NVIDIA GeForce GTX 980, 4GB GDDR5', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP005', 'DM005', 'AMD Radeon R9 390', 'TH015', 11990000, 'Card đồ họa AMD Radeon R9 390, 8GB GDDR5', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('PC001', 'DM002', 'PC Gaming High-End', 'TH006', 25000000, 'Máy tính chơi game cao cấp với cấu hình mạnh mẽ', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('PC002', 'DM002', 'PC Workstation', 'TH002', 20000000, 'Máy tính làm việc chuyên nghiệp, ổn định', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('PC003', 'DM002', 'PC Văn Phòng', 'TH003', 12000000, 'Máy tính cho công việc văn phòng, cấu hình vừa phải', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('PC004', 'DM002', 'PC Mini ITX', 'TH007', 18000000, 'PC nhỏ gọn nhưng hiệu năng cao cho không gian hạn chế', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('PC005', 'DM002', 'PC Custom Build', 'TH034', 30000000, 'Máy tính được lắp ráp theo yêu cầu, tối ưu hóa hiệu suất', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP006', 'Laptop', 'Apple MacBook Pro 15-inch', 'TH001', 39999000, 'Laptop Apple MacBook Pro 15-inch với chip Intel Core i7, màn hình Retina 15-inch', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP007', 'Laptop', 'Dell XPS 15', 'TH002', 35199000, 'Laptop Dell XPS 15 với màn hình InfinityEdge, vi xử lý Intel Core i7', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP008', 'Laptop', 'HP Spectre x360 15', 'TH003', 37199000, 'Laptop HP Spectre x360 15 với màn hình cảm ứng, vi xử lý Intel Core i7', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP009', 'Laptop', 'Lenovo ThinkPad P50', 'TH004', 42599000, 'Laptop Lenovo ThinkPad P50, màn hình 15.6-inch, vi xử lý Intel Core i7', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('LAP010', 'Laptop', 'Asus ZenBook 15', 'TH005', 27199000, 'Laptop Asus ZenBook 15, vi xử lý Intel Core i7, màn hình 15-inch', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP006', 'DM004', 'Intel Core i7-6700K', 'TH014', 9990000, 'Bộ vi xử lý Intel Core i7-6700K, 4 lõi, 8 luồng, xung nhịp tối đa 4.2GHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP007', 'DM004', 'AMD Ryzen 7 1700', 'TH015', 6990000, 'Bộ vi xử lý AMD Ryzen 7 1700, 8 lõi, 16 luồng, xung nhịp tối đa 3.7GHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP008', 'DM005', 'NVIDIA GeForce GTX 1080', 'TH016', 16990000, 'Card đồ họa NVIDIA GeForce GTX 1080, 8GB GDDR5', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP009', 'DM005', 'AMD Radeon RX 480', 'TH015', 8990000, 'Card đồ họa AMD Radeon RX 480, 8GB GDDR5', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP010', 'DM006', 'Corsair Vengeance LPX 16GB', 'TH006', 2990000, 'RAM Corsair Vengeance LPX 16GB, DDR4 2400MHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP011', 'DM006', 'G.Skill Ripjaws V 16GB', 'TH017', 3490000, 'RAM G.Skill Ripjaws V 16GB, DDR4 2666MHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP012', 'DM006', 'Kingston HyperX Fury 8GB', 'TH018', 1990000, 'RAM Kingston HyperX Fury 8GB, DDR4 2400MHz', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP013', 'DM007', 'Samsung 860 EVO 1TB', 'TH022', 3290000, 'SSD Samsung 860 EVO 1TB, SATA III, 2.5-inch', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP014', 'DM007', 'Seagate Barracuda 2TB', 'TH020', 2490000, 'HDD Seagate Barracuda 2TB, SATA III, 3.5-inch', 1);",
                "INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham, trangThai) VALUES ('SP015', 'DM008', 'LG UltraGear 27GL850', 'TH023', 12990000, 'Màn hình LG UltraGear 27-inch, 144Hz, 1ms', 1);",

                // CauHinhLaptop
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP001', 'CPU', 'Intel Core i5-4258U', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP001', 'RAM', '8GB RAM', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP001', 'Storage', '256GB SSD', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP001', 'ManHinh', '13.3-inch Retina Display', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP001', 'Pin', '10 hours battery life', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP002', 'CPU', 'Intel Core i5-4200U', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP002', 'RAM', '8GB RAM', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP002', 'Storage', '128GB SSD', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP002', 'ManHinh', '13.3-inch Full HD', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP002', 'Pin', '9 hours battery life', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP006', 'CPU', 'Intel Core i7-6700HQ', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP006', 'RAM', '16GB RAM', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP006', 'Storage', '512GB SSD', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP006', 'ManHinh', '15.4-inch Retina Display', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP006', 'Pin', '10 hours battery life', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP007', 'CPU', 'Intel Core i7-6500U', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP007', 'RAM', '16GB RAM', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP007', 'Storage', '512GB SSD', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP007', 'ManHinh', '15.6-inch Full HD', 0);",
                "INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin, STTPL) VALUES ('LAP007', 'Pin', '10 hours battery life', 0);",

                // CauHinhPC
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC001', 'CPU', 'SP001', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC001', 'GPU', 'SP004', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC001', 'RAM', 'SP011', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC002', 'CPU', 'SP003', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC002', 'GPU', 'SP005', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC002', 'RAM', 'SP012', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC003', 'CPU', 'SP006', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC003', 'GPU', 'SP008', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC003', 'RAM', 'SP013', 0);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC004', 'CPU', 'SP007', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC004', 'GPU', 'SP009', 1);",
                "INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL) VALUES ('PC004', 'RAM', 'SP014', 1);",

                // NhaCungCap
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC001', 'Công ty Linh Kiện ABC', '123 Lê Lợi, Q.1, TP.HCM', '0909123456', 'abc@linhkien.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC002', 'Thiết Bị Điện Tử Hưng Phát', '56 Nguyễn Trãi, Q.5, TP.HCM', '0912345678', 'hungphat@tb.com', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC003', 'Phát Triển Công Nghệ Việt', '789 Trường Chinh, Hà Nội', '0988777666', 'info@viettech.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC004', 'Vinatech Solutions', '45 Lạc Long Quân, Đà Nẵng', '0933555777', 'sales@vinatech.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC005', 'Linh Kiện Sài Gòn', '101 Cách Mạng Tháng Tám, Q.3, TP.HCM', '0909888777', 'contact@lksaigon.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC006', 'Công ty Linh Kiện XYZ', '123 Nguyen Van Cu, Q.1, TP.HCM', '0909123457', 'xyz@linhkien.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC007', 'Thiết Bị Điện Tử SaigonTech', '56 Nguyen Hue, Q.1, TP.HCM', '0912345679', 'saigontech@tb.com', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC008', 'Phát Triển Công Nghệ Sao', '789 Le Loi, Hà Nội', '0988777667', 'info@saotech.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC009', 'Vinatech Group', '45 Nguyen Van Linh, Đà Nẵng', '0933555778', 'sales@vinatechgroup.vn', 1);",
                "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, trangThai) VALUES ('NCC010', 'Linh Kiện Việt Nam', '101 Tran Dang Ninh, Q.3, TP.HCM', '0909888778', 'contact@linhkien.vn', 1);",

                // PhanLoaiSP
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (1, 'LAP001', 0, 29999000, 10);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (2, 'LAP002', 0, 25199000, 15);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (3, 'LAP003', 0, 27199000, 8);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (4, 'LAP004', 0, 28599000, 5);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (5, 'LAP005', 0, 19199000, 12);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (6, 'LAP006', 0, 39999000, 8);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (7, 'LAP007', 0, 35199000, 7);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (8, 'LAP008', 0, 37199000, 6);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (9, 'LAP009', 0, 42599000, 5);",
                "INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES (10, 'LAP010', 0, 27199000, 9);",

                // ChiTietSP
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN001', 1, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN002', 1, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN003', 2, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN004', 2, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN005', 3, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN006', 6, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN007', 6, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN008', 7, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN009', 7, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN010', 8, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN011', 8, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN012', 9, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN013', 9, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN014', 10, 1);",
                "INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, trangThai) VALUES ('SN015', 10, 1);",

                // KhuyenMai
                "INSERT INTO KhuyenMai (idKhuyenMai, giaTri) VALUES ('KM001', 1000000);",
                "INSERT INTO KhuyenMai (idKhuyenMai, giaTri) VALUES ('KM002', 2000000);",
                "INSERT INTO KhuyenMai (idKhuyenMai, giaTri) VALUES ('KM003', 500000);",
                "INSERT INTO KhuyenMai (idKhuyenMai, giaTri) VALUES ('KM004', 1500000);",
                "INSERT INTO KhuyenMai (idKhuyenMai, giaTri) VALUES ('KM005', 3000000);",

                // KhuyenMaiCombo
                "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES ('KM001', 'LAP001');",
                "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES ('KM002', 'LAP002');",
                "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES ('KM003', 'LAP003');",
                "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES ('KM004', 'LAP006');",
                "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES ('KM005', 'LAP007');",
                "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES ('KM004', 'LAP008');",
                "INSERT INTO KhuyenMaiCombo (idKhuyenMai, idSanPham) VALUES ('KM005', 'LAP009');",

                // NhanVien
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV001', 'Nguyen Van A', '0987654321', 'nva@example.com', '2015-01-15', 'Manager', 15000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV002', 'Tran Thi B', '0912345678', 'ttb@example.com', '2015-03-22', 'Salesperson', 10000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV003', 'Le Van C', '0987654322', 'lvc@example.com', '2015-05-10', 'Technician', 9000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV004', 'Pham Thi D', '0912345679', 'ptd@example.com', '2015-07-18', 'Cashier', 8000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV005', 'Hoang Van E', '0987654323', 'hve@example.com', '2015-09-25', 'Marketing', 11000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV006', 'Tran Van B', '0987654324', 'tvb@example.com', '2016-01-10', 'Manager', 16000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV007', 'Le Thi C', '0912345674', 'ltc@example.com', '2016-03-15', 'Salesperson', 11000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV008', 'Pham Van D', '0987654325', 'pvd@example.com', '2016-05-20', 'Technician', 10000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV009', 'Hoang Thi E', '0912345675', 'hte@example.com', '2016-07-25', 'Cashier', 9000000, 1);",
                "INSERT INTO NhanVien (idNhanVien, TenNhanVien, SDT, Mail, NgayVaoLam, ViTri, Luong, trangThai) VALUES ('NV010', 'Nguyen Van F', '0987654326', 'nvf@example.com', '2016-09-30', 'Marketing', 12000000, 1);",

                // TaiKhoan
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK001', 'NV001', NULL, 'NQ001', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK002', 'NV002', NULL, 'NQ002', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK003', 'NV003', NULL, 'NQ003', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK004', 'NV004', NULL, 'NQ003', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK005', 'NV005', NULL, 'NQ002', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK006', 'NV006', NULL, 'NQ001', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK007', 'NV007', NULL, 'NQ002', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK008', 'NV008', NULL, 'NQ003', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK009', 'NV009', NULL, 'NQ003', 'password123', 1);",
                "INSERT INTO TaiKhoan (idTaiKhoan, idNhanVien, anhDaiDien, idNhomQuyen, matKhau, trangThai) VALUES ('TK010', 'NV010', NULL, 'NQ002', 'password123', 1);",

                // KhachHang
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (1, ' Tran Van A', '0987654320', 'tva@example.com', '2015-02-10', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (2, 'Nguyen Thi B', '0912345670', 'ntb@example.com', '2015-04-15', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (3, 'Le Van C', '0987654324', 'lvcustomer@example.com', '2015-06-20', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (4, 'Pham Thi D', '0912345674', 'ptd@example.com', '2015-08-25', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (5, 'Hoang Van E', '0987654325', 'hve@example.com', '2015-10-30', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (6, 'Nguyen Van F', '0987654326', 'nvf@example.com', '2016-01-10', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (7, 'Tran Thi G', '0912345676', 'ttg@example.com', '2016-03-15', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (8, 'Le Van H', '0987654327', 'lvh@example.com', '2016-05-20', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (9, 'Pham Thi I', '0912345677', 'pti@example.com', '2016-07-25', 1);",
                "INSERT INTO KhachHang (idKhachHang, tenKhachHang, soDienThoai, Mail, NgayThamGia, trangThai) VALUES (10, 'Hoang Van J', '0987654328', 'hvj@example.com', '2016-09-30', 1);",

                // HoaDonXuat
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT001', 'NV002', 1, '2015-02-15', 29999000, 'KM001');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT002', 'NV002', 2, '2015-04-20', 25199000, 'KM002');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT003', 'NV004', 3, '2015-06-25', 27199000, 'KM003');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT004', 'NV004', 4, '2015-08-30', 28599000, NULL);",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT005', 'NV002', 5, '2015-10-31', 19199000, NULL);",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT006', 'NV002', 6, '2016-01-15', 39999000, 'KM004');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT007', 'NV004', 7, '2016-03-20', 35199000, 'KM005');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT008', 'NV007', 8, '2016-05-25', 37199000, 'KM004');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT009', 'NV009', 9, '2016-07-30', 42599000, NULL);",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT010', 'NV002', 10, '2016-09-30', 27199000, NULL);",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT011', 'NV007', 1, '2016-02-15', 29999000, 'KM001');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT012', 'NV009', 2, '2016-04-20', 25199000, 'KM002');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT013', 'NV002', 3, '2016-06-25', 27199000, 'KM003');",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT014', 'NV004', 4, '2016-08-30', 28599000, NULL);",
                "INSERT INTO HoaDonXuat (idHoaDonXuat, idNhanVien, idKhachHang, ngayTao, tongTien, idKhuyenMai) VALUES ('HDXT015', 'NV007', 5, '2016-10-31', 19199000, NULL);",

                // ChiTietHoaDonXuat
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT001', 'HDXT001', 'SN001', 29999000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT002', 'HDXT002', 'SN003', 25199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT003', 'HDXT003', 'SN005', 27199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT004', 'HDXT004', 'SN002', 28599000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT005', 'HDXT005', 'SN004', 19199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT006', 'HDXT006', 'SN006', 39999000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT007', 'HDXT007', 'SN008', 35199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT008', 'HDXT008', 'SN010', 37199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT009', 'HDXT009', 'SN012', 42599000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT010', 'HDXT010', 'SN014', 27199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT011', 'HDXT011', 'SN001', 29999000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT012', 'HDXT012', 'SN003', 25199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT013', 'HDXT013', 'SN005', 27199000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT014', 'HDXT014', 'SN002', 28599000);",
                "INSERT INTO ChiTietHoaDonXuat (idChiTietHoaDonXuat, idHoaDonXuat, SN, donGia) VALUES ('CTHDXT015', 'HDXT015', 'SN004', 19199000);",

                // HoaDonNhap
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN001', 'NV003', 'NCC001', '2015-01-20', 10000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN002', 'NV003', 'NCC002', '2015-03-25', 15000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN003', 'NV005', 'NCC003', '2015-05-30', 20000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN004', 'NV005', 'NCC004', '2015-07-31', 18000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN005', 'NV003', 'NCC005', '2015-09-15', 12000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN006', 'NV003', 'NCC006', '2016-01-25', 15000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN007', 'NV005', 'NCC007', '2016-03-30', 20000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN008', 'NV008', 'NCC008', '2016-05-31', 18000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN009', 'NV003', 'NCC009', '2016-07-15', 12000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN010', 'NV005', 'NCC010', '2016-09-20', 16000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN011', 'NV008', 'NCC001', '2016-02-20', 10000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN012', 'NV003', 'NCC002', '2016-04-25', 15000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN013', 'NV005', 'NCC003', '2016-06-30', 20000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN014', 'NV008', 'NCC004', '2016-08-31', 18000000);",
                "INSERT INTO HoaDonNhap (idHoaDonNhap, idNhanVien, idNhaCungCap, ngayTao, tongTien) VALUES ('HDN015', 'NV003', 'NCC005', '2016-10-15', 12000000);",

                // ChiTietDonNhap
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (1, 'SN001', 29999000, 29999000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (2, 'SN003', 25199000, 25199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (3, 'SN005', 27199000, 27199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (4, 'SN002', 28599000, 28599000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (5, 'SN004', 19199000, 19199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (6, 'SN006', 39999000, 39999000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (7, 'SN008', 35199000, 35199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (8, 'SN010', 37199000, 37199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (9, 'SN012', 42599000, 42599000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (10, 'SN014', 27199000, 27199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (11, 'SN001', 29999000, 29999000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (12, 'SN003', 25199000, 25199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (13, 'SN005', 27199000, 27199000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (14, 'SN002', 28599000, 28599000);",
                "INSERT INTO ChiTietDonNhap (idDonHang, SN, donGia, thanhTien) VALUES (15, 'SN004', 19199000, 19199000);"
        }; // okay kimi AI, i want you to remember this format for ongoing insertion which will be added to the String[] above.

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