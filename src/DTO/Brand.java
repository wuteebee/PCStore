package DTO;

public class Brand {
    private String maThuongHieu;
    private String tenThuongHieu;
    private Catalog danhMuc;
    private boolean trangThai;



    public Brand(String tenThuongHieu, Catalog danhMuc, boolean trangThai) {
        this.tenThuongHieu = tenThuongHieu;
        this.danhMuc = danhMuc;
        this.trangThai = trangThai;
    }

    public Brand(String maThuongHieu, String tenThuongHieu, Catalog danhMuc, boolean trangThai) {
        this.maThuongHieu = maThuongHieu;
        this.tenThuongHieu = tenThuongHieu;
        this.danhMuc = danhMuc;
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

    public void setDanhMuc(Catalog danhMuc) {
        this.danhMuc = danhMuc;
    }

    public Catalog getDanhMuc() {
        return danhMuc;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public boolean isTrangThai() {
        return trangThai;
    }
}