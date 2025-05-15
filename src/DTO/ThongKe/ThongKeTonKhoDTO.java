package DTO.ThongKe;

public class ThongKeTonKhoDTO {
    String masp; // Đổi từ int sang String
    String tensanpham;
    int tondauky;
    int nhaptrongky;
    int xuattrongky;
    int toncuoiky;

    public ThongKeTonKhoDTO() {
    }

    public ThongKeTonKhoDTO(String masp, String tensanpham, int tondauky, int nhaptrongky, int xuattrongky, int toncuoiky) {
        this.masp = masp;
        this.tensanpham = tensanpham;
        this.tondauky = tondauky;
        this.nhaptrongky = nhaptrongky;
        this.xuattrongky = xuattrongky;
        this.toncuoiky = toncuoiky;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public int getTondauky() {
        return tondauky;
    }

    public void setTondauky(int tondauky) {
        this.tondauky = tondauky;
    }

    public int getNhaptrongky() {
        return nhaptrongky;
    }

    public void setNhaptrongky(int nhaptrongky) {
        this.nhaptrongky = nhaptrongky;
    }

    public int getXuattrongky() {
        return xuattrongky;
    }

    public void setXuattrongky(int xuattrongky) {
        this.xuattrongky = xuattrongky;
    }

    public int getToncuoiky() {
        return toncuoiky;
    }

    public void setToncuoiky(int toncuoiky) {
        this.toncuoiky = toncuoiky;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((masp == null) ? 0 : masp.hashCode());
        result = prime * result + ((tensanpham == null) ? 0 : tensanpham.hashCode());
        result = prime * result + tondauky;
        result = prime * result + nhaptrongky;
        result = prime * result + xuattrongky;
        result = prime * result + toncuoiky;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ThongKeTonKhoDTO other = (ThongKeTonKhoDTO) obj;
        if (masp == null) {
            if (other.masp != null)
                return false;
        } else if (!masp.equals(other.masp))
            return false;
        if (tensanpham == null) {
            if (other.tensanpham != null)
                return false;
        } else if (!tensanpham.equals(other.tensanpham))
            return false;
        return tondauky == other.tondauky &&
               nhaptrongky == other.nhaptrongky &&
               xuattrongky == other.xuattrongky &&
               toncuoiky == other.toncuoiky;
    }

    @Override
    public String toString() {
        return "ThongKeTonKhoDTO [masp=" + masp + ", tensanpham=" + tensanpham + ", tondauky=" + tondauky
                + ", nhaptrongky=" + nhaptrongky + ", xuattrongky=" + xuattrongky + ", toncuoiky=" + toncuoiky + "]";
    }
}