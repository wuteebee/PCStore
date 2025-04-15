package BUS;

import DAO.PhieuNhapDAO;
import DTO.HoaDonNhap;

import java.util.List;

public class PhieuNhapBUS {
    private PhieuNhapDAO dao;

    public PhieuNhapBUS() {
        dao = new PhieuNhapDAO();
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

    // Lấy theo ID
    public HoaDonNhap getPhieuNhapById(String id) {
        return dao.getById(id);
    }
}
