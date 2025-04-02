package DTO;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class test {

    private static final String DB_URL = "jdbc:h2:file:./src/DATA/PCStoreTest;DATABASE_TO_UPPER=FALSE;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    public static void deleteDB() {
        try {
            DeleteDbFiles.execute("C:\\Users\\ADMIN", "PCStoreTest", true);

        } catch (Exception e) {
            System.out.println("Can not create database.");
            e.printStackTrace();
        }
    }
    public static void createDB() {
        init();

        try {
            init();
            Server server = Server.createWebServer().start();
            System.out.println("H2 Console started at " + server.getURL());

            // Rest of the code
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void init() {
        // Table Creation SQL (without trailing semicolons)
        String createChucNang = "CREATE TABLE IF NOT EXISTS ChucNang (" +
                "idChucNang VARCHAR PRIMARY KEY, " +
                "tenChucNang VARCHAR(50) NOT NULL" +
                ")";

        String createQuyen = "CREATE TABLE IF NOT EXISTS Quyen (" +
                "idQuyen VARCHAR PRIMARY KEY, " +
                "tenQuyen VARCHAR(50) NOT NULL" +
                ")";

        String createPhanQuyen = "CREATE TABLE IF NOT EXISTS PhanQuyen (" +
                "idQuyen VARCHAR PRIMARY KEY, " +
                "idChucNang VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (idQuyen) REFERENCES Quyen(idQuyen), " +
                "FOREIGN KEY (idChucNang) REFERENCES ChucNang(idChucNang)" +
                ")";

        String createDanhMuc = "CREATE TABLE IF NOT EXISTS DanhMuc (" +
                "idDanhMuc VARCHAR(20) PRIMARY KEY, " +
                "tenDanhMuc VARCHAR(50) NOT NULL" +
                ")";

        String createDanhMucCon = "CREATE TABLE IF NOT EXISTS DanhMucCon (" +
                "idDanhMucCon VARCHAR(20) PRIMARY KEY, " +
                "idDanhMuc VARCHAR(20) NOT NULL, " +
                "tenLoai VARCHAR(50) NOT NULL" +
                ")";

        String createThuongHieu = "CREATE TABLE IF NOT EXISTS ThuongHieu (" +
                "idThuongHieu VARCHAR(20) PRIMARY KEY, " +
                "tenThuongHieu VARCHAR(50) NOT NULL" +
                ")";

        String createNhaCungCap = "CREATE TABLE IF NOT EXISTS NhaCungCap (" +
                "idNhaCungCap VARCHAR(20) PRIMARY KEY, " +
                "tenNhaCungCap VARCHAR(255) NOT NULL, " +
                "diaChi VARCHAR(500), " +
                "soDienThoai VARCHAR(20), " +
                "email VARCHAR(255)" +
                ")";

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
                ")";

        String createGhep = "CREATE TABLE IF NOT EXISTS Ghep (" +
                "idSanPhamGhep VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "FOREIGN KEY (idSanPhamGhep) REFERENCES SanPham(idSanPham), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ")";

        String createKhuyenMai = "CREATE TABLE IF NOT EXISTS KhuyenMai (" +
                "idKhuyenMai VARCHAR(20) PRIMARY KEY, " +
                "giaTri DECIMAL(10,2) NOT NULL" +
                ")";

        String createKhuyenMaiCombo = "CREATE TABLE IF NOT EXISTS KhuyenMaiCombo (" +
                "idKhuyenMai VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "FOREIGN KEY (idKhuyenMai) REFERENCES KhuyenMai(idKhuyenMai), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ")";

        String createNhanVien = "CREATE TABLE IF NOT EXISTS NhanVien (" +
                "idNhanVien VARCHAR(20) PRIMARY KEY, " +
                "hoNhanVien VARCHAR(20) NOT NULL, " +
                "tenNhanVien VARCHAR(50) NOT NULL, " +
                "idChucNang VARCHAR(20) NOT NULL, " +
                "ngaySinh DATE, " +
                "soDienThoai VARCHAR(20), " +
                "email VARCHAR(255), " +
                "ngayVaoLam DATE, " +
                "chucVu VARCHAR(50), " +
                "luong DECIMAL(10,2), " +
                "diaChi VARCHAR(500), " +
                "anhDaiDien BLOB, " +
                "matKhau VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (idChucNang) REFERENCES ChucNang(idChucNang)" +
                ")";

        String createKhachHang = "CREATE TABLE IF NOT EXISTS KhachHang (" +
                "idKhachHang VARCHAR(20) PRIMARY KEY, " +
                "hoKhachHang VARCHAR(255) NOT NULL, " +
                "tenKhachHang VARCHAR(255) NOT NULL, " +
                "soDienThoai VARCHAR(20), " +
                "ngaySinh DATE" +
                ")";

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
                ")";

        String createChiTietHoaDonXuat = "CREATE TABLE IF NOT EXISTS ChiTietHoaDonXuat (" +
                "idChiTietHoaDonXuat VARCHAR(20) PRIMARY KEY, " +
                "idHoaDonXuat VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "soLuong INT NOT NULL, " +
                "thanhTien DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (idHoaDonXuat) REFERENCES HoaDonXuat(idHoaDonXuat), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ")";

        String createHoaDonNhap = "CREATE TABLE IF NOT EXISTS HoaDonNhap (" +
                "idHoaDonNhap VARCHAR(20) PRIMARY KEY, " +
                "idNhanVien VARCHAR(20) NOT NULL, " +
                "idNhaCungCap VARCHAR(20) NOT NULL, " +
                "ngayTao DATE NOT NULL, " +
                "tongTien DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien), " +
                "FOREIGN KEY (idNhaCungCap) REFERENCES NhaCungCap(idNhaCungCap)" +
                ")";

        String createChiTietHoaDonNhap = "CREATE TABLE IF NOT EXISTS ChiTietHoaDonNhap (" +
                "idChiTietHoaDonNhap VARCHAR(20) PRIMARY KEY, " +
                "idHoaDonNhap VARCHAR(20) NOT NULL, " +
                "idSanPham VARCHAR(20) NOT NULL, " +
                "soLuong INT NOT NULL, " +
                "thanhTien DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (idHoaDonNhap) REFERENCES HoaDonNhap(idHoaDonNhap), " +
                "FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)" +
                ")";

        // Combine all table creation SQLs in correct order
        String[] createTables = {
                createChucNang, createQuyen, createPhanQuyen, createDanhMuc, createDanhMucCon, createThuongHieu,
                createNhaCungCap, createSanPham, createGhep, createKhuyenMai,
                createKhuyenMaiCombo, createNhanVien, createKhachHang,
                createHoaDonXuat, createChiTietHoaDonXuat, createHoaDonNhap,
                createChiTietHoaDonNhap
        };

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Execute table creation statements
            for (String sql : createTables) {
                stmt.executeUpdate(sql);
            }

            System.out.println("Database and tables created successfully.");
        } catch (SQLException e1) {
            deleteDB();
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createDB();
    }
}
