package config;

import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:h2:file:./src/DTO/PCStoreTest;DATABASE_TO_UPPER=FALSE;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";

    public static void deleteDB() {
        try {
            DeleteDbFiles.execute("src\\DTO", "PCStoreTest", true);
            System.out.println("Xóa database thành công");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] createTableScript() {
        // Script cho table
        String createChucNang = "CREATE TABLE IF NOT EXISTS ChucNang (" +
                "idChucNang VARCHAR(20) PRIMARY KEY, " +
                "tenChucNang VARCHAR(50) NOT NULL" +
                ");";

        String createNhomQuyen = "CREATE TABLE IF NOT EXISTS NhomQuyen (" +
                "idNhomQuyen VARCHAR(20) PRIMARY KEY, " +
                "tenNhomQuyen VARCHAR(50) NOT NULL" +
                ");";

        String createChiTietQuyen = "CREATE TABLE IF NOT EXISTS ChiTietQuyen (" +
                "idNhomQuyen VARCHAR(20) PRIMARY KEY, " +
                "idChucNang VARCHAR(50) NOT NULL, " +
                "hanhDong VARCHAR(5) NOT NULL, " +
                "FOREIGN KEY (idNhomQuyen) REFERENCES NhomQuyen(idNhomQuyen), " +
                "FOREIGN KEY (idChucNang) REFERENCES ChucNang(idChucNang)" +
                ");";

        String createDanhMuc = "CREATE TABLE IF NOT EXISTS DanhMuc (" +
                "idDanhMuc VARCHAR(20) PRIMARY KEY, " +
                "tenDanhMuc VARCHAR(50) NOT NULL" +
                ");";

        String createDanhMucCon = "CREATE TABLE IF NOT EXISTS DanhMucCon (" +
                "idDanhMucCon VARCHAR(20) PRIMARY KEY, " +
                "idDanhMuc VARCHAR(20) NOT NULL, " +
                "tenDanhMucCon VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc)" +
                ");";

        String createThuongHieu = "CREATE TABLE IF NOT EXISTS ThuongHieu (" +
                "idThuongHieu VARCHAR(20) PRIMARY KEY, " +
                "tenThuongHieu VARCHAR(50) NOT NULL" +
                ");";

        String createNhaCungCap = "CREATE TABLE IF NOT EXISTS NhaCungCap (" +
                "idNhaCungCap VARCHAR(20) PRIMARY KEY, " +
                "tenNhaCungCap VARCHAR(255) NOT NULL, " +
                "diaChi VARCHAR(500), " +
                "soDienThoai VARCHAR(20), " +
                "email VARCHAR(255)" +
                ");";

        String createSanPham = "CREATE TABLE IF NOT EXISTS SanPham (" +
                "idSanPham VARCHAR(20) PRIMARY KEY, " +
                "idDanhMucCon VARCHAR(20) NOT NULL, " +
                "tenSanPham VARCHAR(255) NOT NULL, " +
                "idThuongHieu VARCHAR(20) NOT NULL, " +
                "gia INT NOT NULL, " +
                "moTaSanPham VARCHAR(1000), " +
                "anhSanPham BLOB, " +
                "FOREIGN KEY (idDanhMucCon) REFERENCES DanhMucCon(idDanhMucCon), " +
                "FOREIGN KEY (idThuongHieu) REFERENCES ThuongHieu(idThuongHieu)" +
                ");";

        String createThongTinKyThuat = "CREATE TABLE IF NOT EXISTS ThongTinKyThuat (" +
                "idThongTin VARCHAR(20) PRIMARY KEY, " +
                "tenThongTIn VARCHAR(50) NOT NULL" +
                ");";

        String createCauHinhPC = "CREATE TABLE IF NOT EXISTS CauHinhPC (" +
                "idSanPham VARCHAR(20), " +
                "idThongTin VARCHAR(20), " +
                "idLinkKien VARCHAR(20), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham), " +
                "FOREIGN KEY (idThongTin) REFERENCES ThongTinKyThuat(idThongTin)" +
                ");";

        String createGhep = "CREATE TABLE IF NOT EXISTS Ghep (" +
                "idSanPhamGhep VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "FOREIGN KEY (idSanPhamGhep) REFERENCES SanPham(idSanPham), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ");";

        String createKhuyenMai = "CREATE TABLE IF NOT EXISTS KhuyenMai (" +
                "idKhuyenMai VARCHAR(20) PRIMARY KEY, " +
                "giaTri DECIMAL(10,2) NOT NULL" +
                ");";

        String createKhuyenMaiCombo = "CREATE TABLE IF NOT EXISTS KhuyenMaiCombo (" +
                "idKhuyenMai VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "FOREIGN KEY (idKhuyenMai) REFERENCES KhuyenMai(idKhuyenMai), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ");";

        String createNhanVien = "CREATE TABLE IF NOT EXISTS NhanVien (" +
                "idNhanVien VARCHAR(20) PRIMARY KEY, " +
                "hoNhanVien VARCHAR(20) NOT NULL, " +
                "tenNhanVien VARCHAR(50) NOT NULL, " +
                "ngaySinh DATE, " +
                "soDienThoai VARCHAR(20), " +
                "email VARCHAR(255), " +
                "ngayVaoLam DATE, " +
                "chucVu VARCHAR(50), " +
                "luong DECIMAL(10,2), " +
                "diaChi VARCHAR(500), " +
                "anhDaiDien BLOB" +
                ");";

        String createTaiKhoan = "CREATE TABLE IF NOT EXISTS TaiKhoan (" +
                "idTaiKhoan VARCHAR(20) PRIMARY KEY, " +
                "idNhanVien VARCHAR(20) NOT NULL, " +
                "idNhomQuyen VARCHAR(20) NOT NULL, " +
                "matKhau VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (idNhomQuyen) REFERENCES NhomQuyen(idNhomQuyen), " +
                "FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien)" +
                ");";

        String createKhachHang = "CREATE TABLE IF NOT EXISTS KhachHang (" +
                "idKhachHang VARCHAR(20) PRIMARY KEY, " +
                "hoKhachHang VARCHAR(255) NOT NULL, " +
                "tenKhachHang VARCHAR(255) NOT NULL, " +
                "soDienThoai VARCHAR(20), " +
                "ngaySinh DATE" +
                ");";

        String createHoaDonXuat = "CREATE TABLE IF NOT EXISTS HoaDonXuat (" +
                "idHoaDonXuat VARCHAR(20) PRIMARY KEY, " +
                "idNhanVien VARCHAR(20) NOT NULL, " +
                "idKhachHang VARCHAR(20) NOT NULL, " +
                "ngayTao DATE NOT NULL, " +
                "tongTien DECIMAL(10,2) NOT NULL, " +
                "idKhuyenMai VARCHAR(20), " +
                "FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien), " +
                "FOREIGN KEY (idKhachHang) REFERENCES KhachHang(idKhachHang), " +
                "FOREIGN KEY (idKhuyenMai) REFERENCES KhuyenMai(idKhuyenMai)" +
                ");";

        String createChiTietHoaDonXuat = "CREATE TABLE IF NOT EXISTS ChiTietHoaDonXuat (" +
                "idChiTietHoaDonXuat VARCHAR(20) PRIMARY KEY, " +
                "idHoaDonXuat VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "soLuong INT NOT NULL, " +
                "thanhTien DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (idHoaDonXuat) REFERENCES HoaDonXuat(idHoaDonXuat), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ");";

        String createHoaDonNhap = "CREATE TABLE IF NOT EXISTS HoaDonNhap (" +
                "idHoaDonNhap VARCHAR(20) PRIMARY KEY, " +
                "idNhanVien VARCHAR(20) NOT NULL, " +
                "idNhaCungCap VARCHAR(20) NOT NULL, " +
                "ngayTao DATE NOT NULL, " +
                "tongTien DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien), " +
                "FOREIGN KEY (idNhaCungCap) REFERENCES NhaCungCap(idNhaCungCap)" +
                ");";

        String createChiTietHoaDonNhap = "CREATE TABLE IF NOT EXISTS ChiTietHoaDonNhap (" +
                "idChiTietHoaDonNhap VARCHAR(20) PRIMARY KEY, " +
                "idHoaDonNhap VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "soLuong INT NOT NULL, " +
                "thanhTien DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (idHoaDonNhap) REFERENCES HoaDonNhap(idHoaDonNhap), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ");";

        return new String[]{
                createChucNang,
                createNhomQuyen,
                createChiTietQuyen,
                createDanhMuc,
                createDanhMucCon,
                createThuongHieu,
                createNhaCungCap,
                createSanPham,
                createThongTinKyThuat,
                createCauHinhPC,
                createGhep,
                createKhuyenMai,
                createKhuyenMaiCombo,
                createNhanVien,
                createTaiKhoan,
                createKhachHang,
                createHoaDonXuat,
                createChiTietHoaDonXuat,
                createHoaDonNhap,
                createChiTietHoaDonNhap
        };
    }

    public static String[] dropTableScript() {
        String dropChucNang = "DROP TABLE IF EXISTS ChucNang";
        String dropQuyen = "DROP TABLE IF EXISTS Quyen";
        String dropPhanQuyen = "DROP TABLE IF EXISTS PhanQuyen";
        String dropDanhMuc = "DROP TABLE IF EXISTS DanhMuc";
        String dropDanhMucCon = "DROP TABLE IF EXISTS DanhMucCon";
        String dropThuongHieu = "DROP TABLE IF EXISTS ThuongHieu";
        String dropNhaCungCap = "DROP TABLE IF EXISTS NhaCungCap";
        String dropSanPham = "DROP TABLE IF EXISTS SanPham";
        String dropGhep = "DROP TABLE IF EXISTS Ghep";
        String dropKhuyenMai = "DROP TABLE IF EXISTS KhuyenMai";
        String dropKhuyenMaiCombo = "DROP TABLE IF EXISTS KhuyenMaiCombo";
        String dropNhanVien = "DROP TABLE IF EXISTS NhanVien";
        String dropKhachHang = "DROP TABLE IF EXISTS KhachHang";
        String dropHoaDonXuat = "DROP TABLE IF EXISTS HoaDonXuat";
        String dropChiTietHoaDonXuat = "DROP TABLE IF EXISTS ChiTietHoaDonXuat";
        String dropHoaDonNhap = "DROP TABLE IF EXISTS HoaDonNhap";
        String dropChiTietHoaDonNhap = "DROP TABLE IF EXISTS ChiTietHoaDonNhap";

        return new String[]{
                dropChucNang, dropQuyen, dropPhanQuyen, dropDanhMuc, dropDanhMucCon, dropThuongHieu,
                dropNhaCungCap, dropSanPham, dropGhep, dropKhuyenMai,
                dropKhuyenMaiCombo, dropNhanVien, dropKhachHang,
                dropHoaDonXuat, dropChiTietHoaDonXuat, dropHoaDonNhap,
                dropChiTietHoaDonNhap
        };
    }

    public static void init(Connection conn) {
        try (Statement stmt1 = conn.createStatement();) {
            // Tạo table
            for (String sql : createTableScript()) {
                stmt1.executeUpdate(sql);
            }
            System.out.println("Tạo database thành công.");
        } catch (SQLException e1) {  // Nếu không tạo được thì thử xóa dữ liệu database và tạo lại table

            try (Statement stmt2 = conn.createStatement();) {
                stmt2.executeUpdate("DROP ALL OBJECT");

                // Tạo lại table
                for (String sql : createTableScript()) {
                    stmt2.executeUpdate(sql);
                }
                System.out.println("Tạo database thành công.");
            } catch (SQLException e2) {
                throw new RuntimeException("Không thể tạo database (e2).\n", e2); // Nếu vẫn không thể tạo được thì ném ra lỗi
            }
            throw new RuntimeException("Không thể tạo database (e2).\n", e1); // Nếu có lỗi nào khác thì ném ra e2 thay vì e1
        }
    }

    public static Connection ConnectDB() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        init(conn);
        Server server = Server.createWebServer("-webPort", "9092").start();
        System.out.println(server.getURL());
        return conn;

//        if(ifExist()) {
//            connectDB();
//        } else {
//            deleteDB();
//            init();
//        }
//
//        try {
//            Server server = Server.createWebServer().start();
//            System.out.println(server.getURL());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void main(String[] args) throws SQLException {
        Database.ConnectDB();
    }
}
