package BUS;

import DAO.PhieuNhapDAO;
import DAO.ProductDAO;
import DTO.ChiTietDonNhap;
import DTO.HoaDonNhap;
import DTO.ProductDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String insertHoaDonNhap(HoaDonNhap hdn) {
        return dao.insertHoaDonNhap(hdn);

    }

    public boolean insertChitietSP(ProductDetail productDetail){
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        return phieuNhapDAO.insertChitietSP(productDetail);
    }
    public boolean insertChitietPhieuNhap(ProductDetail productDetail){
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        return phieuNhapDAO.insertChitietPhieuNhap(productDetail);    }

    public boolean ktraXuatHang(String id){
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        return phieuNhapDAO.ktraXuatHang(id);
    }

    public boolean deleteFull(String id){
        boolean deleted=false;
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        
        
        // update phanloaisp
        ProductDAO productDAO=new ProductDAO();
 
        deleted=phieuNhapDAO.xoaChiTietPhieuNhap(id);
        
        deleted=phieuNhapDAO.xoaChiTietSPTheoPhieuNhap(id);
        deleted=phieuNhapDAO.xoaHoaDonNhap(id);
       productDAO.updateTatCaSoLuongTonKho();

        return deleted;

        

    }

    public boolean updateSLTK(String id,int soluong){
        return dao.updateSTTK(id, soluong);
    }


    public HoaDonNhap getHoaDonNhapbyId(String id){
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        

        return phieuNhapDAO.getPhieuNhapbyId(id);
    }

    public Map<String,Integer> getSoLuongTheoPhanLoai (String maPhieuNhap){
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        return phieuNhapDAO.getSoLuongTheoPhanLoai(maPhieuNhap);
    }

    public double getGiabySN(String id){
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        return phieuNhapDAO.getGiabySN(id);
    }
   
}
