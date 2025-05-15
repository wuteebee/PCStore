package DTO.ThongKe;

public class ThongKeTungNgayDTO {
    private String ngay; // Date (YYYY-MM-DD)
    private long chiphi; // Cost
    private long doanhthu; // Revenue
    private long loinhuan; // Profit

    public ThongKeTungNgayDTO() {}

    public ThongKeTungNgayDTO(String ngay, long chiphi, long doanhthu, long loinhuan) {
        this.ngay = ngay;
        this.chiphi = chiphi;
        this.doanhthu = doanhthu;
        this.loinhuan = loinhuan;
    }

    // Getters and Setters
    public String getNgay() { return ngay; }
    public void setNgay(String ngay) { this.ngay = ngay; }
    public long getChiphi() { return chiphi; }
    public void setChiphi(long chiphi) { this.chiphi = chiphi; }
    public long getDoanhthu() { return doanhthu; }
    public void setDoanhthu(long doanhthu) { this.doanhthu = doanhthu; }
    public long getLoinhuan() { return loinhuan; }
    public void setLoinhuan(long loinhuan) { this.loinhuan = loinhuan; }
}