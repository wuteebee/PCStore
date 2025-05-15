package DTO.ThongKe;

public class ThongKeDoanhThuDTO {
    private int thoigian; // Year
    private long von; // Cost
    private long doanhthu; // Revenue
    private long loinhuan; // Profit

    public ThongKeDoanhThuDTO() {}

    public ThongKeDoanhThuDTO(int thoigian, long von, long doanhthu, long loinhuan) {
        this.thoigian = thoigian;
        this.von = von;
        this.doanhthu = doanhthu;
        this.loinhuan = loinhuan;
    }

    // Getters and Setters
    public int getThoigian() { return thoigian; }
    public void setThoigian(int thoigian) { this.thoigian = thoigian; }
    public long getVon() { return von; }
    public void setVon(long von) { this.von = von; }
    public long getDoanhthu() { return doanhthu; }
    public void setDoanhthu(long doanhthu) { this.doanhthu = doanhthu; }
    public long getLoinhuan() { return loinhuan; }
    public void setLoinhuan(long loinhuan) { this.loinhuan = loinhuan; }
}