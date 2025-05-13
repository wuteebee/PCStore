package BUS;

import DAO.PhieuNhapDAO;
import DTO.ChiTietDonNhap;
import DTO.HoaDonNhap;

import java.util.List;

public class PhieuNhapBUS {
    private PhieuNhapDAO dao;

    public PhieuNhapBUS() {
        dao = new PhieuNhapDAO();
    }

    public PhieuNhapBUS(PhieuNhapDAO dao) {
        this.dao = dao;
    }

    // Lấy tất cả hóa đơn nhập
    public List<HoaDonNhap> getAllPhieuNhap() {
        return dao.getAll();
    }

    // Thêm mới hóa đơn nhập
    public boolean themPhieuNhap(HoaDonNhap hdn) {
        return dao.insert(hdn);
    }

    // Xóa hóa đơn nhập
    public boolean xoaPhieuNhap(String id) {
        return dao.delete(id);
    }

    // // Lấy theo ID
    // public HoaDonNhap getPhieuNhapById(String id) {
    //     return dao.getById(id);
    // }

    public List<ChiTietDonNhap> getAll_ChiTietDonNhap(){
        return dao.getAll_CTDonNhap();
    }
}
