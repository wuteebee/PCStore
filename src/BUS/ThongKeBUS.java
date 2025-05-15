package BUS;

import DAO.ThongKeDAO;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTungNgayDTO;
import DTO.ThongKe.ThongKeKhachHangDTO;
import DTO.ThongKe.ThongKeNhaCungCapDTO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import java.util.ArrayList;
import java.util.Date;

public class ThongKeBUS {
    private ThongKeDAO thongKeDAO;

    public ThongKeBUS() {
        thongKeDAO = new ThongKeDAO();
    }

    // Thống kê doanh thu
    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoTungNam(int namBD, int namKT) {
        return thongKeDAO.getDoanhThuTheoTungNam(namBD, namKT);
    }

    public ArrayList<ThongKeTheoThangDTO> getThongKeTheoThang(int nam) {
        return thongKeDAO.getThongKeTheoThang(nam);
    }

    public ArrayList<ThongKeTungNgayDTO> getThongKeTungNgayTrongThang(int thang, int nam) {
        return thongKeDAO.getThongKeTungNgayTrongThang(thang, nam);
    }

    public ArrayList<ThongKeTungNgayDTO> getThongKeTuNgayDenNgay(String start, String end) {
        return thongKeDAO.getThongKeTuNgayDenNgay(start, end);
    }

    // Thống kê khách hàng
    public ArrayList<ThongKeKhachHangDTO> getAllKhachHang() {
        return thongKeDAO.getAllKhachHang();
    }

    public ArrayList<ThongKeKhachHangDTO> FilterKhachHang(String input, Date start, Date end) {
        return thongKeDAO.filterKhachHang(input, start, end);
    }

    // Thống kê nhà cung cấp
    public ArrayList<ThongKeNhaCungCapDTO> getAllNCC() {
        return thongKeDAO.getAllNCC();
    }

    public ArrayList<ThongKeNhaCungCapDTO> FilterNCC(String input, Date start, Date end) {
        return thongKeDAO.filterNCC(input, start, end);
    }

    // Thống kê tồn kho
    public ArrayList<ThongKeTonKhoDTO> getTonKho() {
        return thongKeDAO.getTonKho();
    }

    public ArrayList<ThongKeTonKhoDTO> filterTonKho(String input, Date start, Date end) {
        return thongKeDAO.filterTonKho(input, start, end);
    }
}