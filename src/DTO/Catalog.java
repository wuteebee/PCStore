package DTO;

public class Catalog {
    private String maDanhMuc;
    private String tenDanhMuc;
    private Catalog danhMucCha;
    private boolean trangThai;


    public Catalog() {
        maDanhMuc = "";
        tenDanhMuc = "";
        danhMucCha = null;
        trangThai = true;
    }

    public Catalog(String maDanhMuc, String tenDanhMuc, String idDanhMucCha, boolean trangThai) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;

        if (!idDanhMucCha.equals("")) {
            Catalog tmp = new Catalog();
            tmp.setMaDanhMuc(idDanhMucCha);
            this.danhMucCha = tmp;
        } else {
            this.danhMucCha = null;
        }

        this.trangThai = trangThai;
    }

    public void setDanhMucCha(Catalog danhMucCha) {
        this.danhMucCha = danhMucCha;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public Catalog getDanhMucCha() {
        return danhMucCha;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}