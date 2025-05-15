package DTO.ThongKe;

public class ThongKeTheoThangDTO {
    private int thang; // Month
    private long chiphi; // Cost
    private long doanhthu; // Revenue
    private long loinhuan; // Profit

    public ThongKeTheoThangDTO() {}

    public ThongKeTheoThangDTO(int thang, long chiphi, long doanhthu, long loinhuan) {
        this.thang = thang;
        this.chiphi = chiphi;
        this.doanhthu = doanhthu;
        this.loinhuan = loinhuan;
    }

    // Getters and Setters
    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }
    public long getChiphi() { return chiphi; }
    public void setChiphi(long chiphi) { this.chiphi = chiphi; }
    public long getDoanhthu() { return doanhthu; }
    public void setDoanhthu(long doanhthu) { this.doanhthu = doanhthu; }
    public long getLoinhuan() { return loinhuan; }
    public void setLoinhuan(long loinhuan) { this.loinhuan = loinhuan; }
}