package DTO;

public class DanhMuc {
    private String idDanhMuc;
    private String tenDanhMuc;
    private String idDanhMucCha;
    private int trangThai;

    public DanhMuc() {}

    public DanhMuc(String idDanhMuc, String tenDanhMuc, String idDanhMucCha, int trangThai) {
        this.idDanhMuc = idDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.idDanhMucCha = idDanhMucCha;
        this.trangThai = trangThai;
    }

    public String getIdDanhMuc() {
        return idDanhMuc;
    }

    public void setIdDanhMuc(String idDanhMuc) {
        this.idDanhMuc = idDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getIdDanhMucCha() {
        return idDanhMucCha;
    }

    public void setIdDanhMucCha(String idDanhMucCha) {
        this.idDanhMucCha = idDanhMucCha;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
