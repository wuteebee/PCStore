package DTO;

public class Brand {
    private String maThuongHieu;
    private String tenThuongHieu;
    private String maDanhMuc;
    private boolean trangThai;
    public Brand(){}


    public Brand(String tenThuongHieu, String maDanhMuc, boolean trangThai) {
        this.tenThuongHieu = tenThuongHieu;
        this.maDanhMuc = maDanhMuc;
        this.trangThai = trangThai;
    }

    public Brand(String maThuongHieu, String tenThuongHieu, String maDanhMuc, boolean trangThai) {
        this.maThuongHieu = maThuongHieu;
        this.tenThuongHieu = tenThuongHieu;
        this.maDanhMuc = maDanhMuc;
        this.trangThai = trangThai;
    }

    public void setMaThuongHieu(String maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public String getMaThuongHieu() {
        return maThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setDanhMuc( String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getmaDanhMuc() {
        return maDanhMuc;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public boolean isTrangThai() {
        return trangThai;
    }
}