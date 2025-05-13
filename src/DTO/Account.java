package DTO;

public class Account {
    private String idTaiKhoan;
    private String idNhanVien;
    private byte[] anhDaiDien;
    private String idNhomQuyen;
    private String tenDangNhap;
    private String matKhau;
    private int trangThai;
    private String maOTP;

    // Constructors
    public Account() {}

    public Account(String idTaiKhoan, String idNhanVien, byte[] anhDaiDien, String idNhomQuyen, 
                   String tenDangNhap, String matKhau, int trangThai, String maOTP) {
        this.idTaiKhoan = idTaiKhoan;
        this.idNhanVien = idNhanVien;
        this.anhDaiDien = anhDaiDien;
        this.idNhomQuyen = idNhomQuyen;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.trangThai = trangThai;
        this.maOTP = maOTP;
    }

    // Getters and Setters
    public String getIdTaiKhoan() { return idTaiKhoan; }
    public void setIdTaiKhoan(String idTaiKhoan) { this.idTaiKhoan = idTaiKhoan; }
    public String getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(String idNhanVien) { this.idNhanVien = idNhanVien; }
    public byte[] getAnhDaiDien() { return anhDaiDien; }
    public void setAnhDaiDien(byte[] anhDaiDien) { this.anhDaiDien = anhDaiDien; }
    public String getIdNhomQuyen() { return idNhomQuyen; }
    public void setIdNhomQuyen(String idNhomQuyen) { this.idNhomQuyen = idNhomQuyen; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
    public String getMaOTP() { return maOTP; }
    public void setMaOTP(String maOTP) { this.maOTP = maOTP; }
}
