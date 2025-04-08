package DTO;

public class ThongSoKyThuat {
    private String idThongSo;
    private String idDanhMuc;
    private String tenThongSo;

    // Default constructor
    public ThongSoKyThuat() {
    }

    // Constructor with parameters
    public ThongSoKyThuat(String idThongSo, String idDanhMuc, String tenThongSo) {
        this.idThongSo = idThongSo;
        this.idDanhMuc = idDanhMuc;
        this.tenThongSo = tenThongSo;
    }

    // Getters and Setters
    public void setIdDanhMuc(String idDanhMuc) {
        this.idDanhMuc = idDanhMuc;
    }

    public void setIdThongSo(String idThongSo) {
        this.idThongSo = idThongSo;
    }

    public void setTenThongSo(String tenThongSo) {
        this.tenThongSo = tenThongSo;
    }

    public String getIdDanhMuc() {
        return idDanhMuc;
    }

    public String getIdThongSo() {
        return idThongSo;
    }

    public String getTenThongSo() {
        return tenThongSo;
    }
}