package BUS;

import DAO.BrandDAO;
import DTO.Brand;
import DAO.DanhMucDAO;
import java.util.List;
import java.util.ArrayList;

public class BrandBUS {
    private BrandDAO dao = new BrandDAO();

    public List<Brand> getAllBrands() {
        return dao.getAllBrands();
    }

    public Brand getBrandById(String id) {
        return dao.getById(id);
    }

    public boolean addBrand(Brand brand) {
        return dao.insert(brand);
    }

    public boolean updateBrand(Brand brand) {
        return dao.update(brand);
    }

    public boolean deleteBrand(String id) {
        return dao.delete(id);
    }

    public String getNextBrandId() {
        return dao.getNextBrandId();
    }

    public List<String> getAllDanhMucIds() {
        return new DanhMucDAO().getAllDanhMucIds();
    }

    public List<Brand> searchBrands(String keyword) {
        keyword = keyword.toLowerCase();
        List<Brand> all = getAllBrands();
        List<Brand> result = new ArrayList<>();

        for (Brand b : all) {
            if ((b.getMaThuongHieu() != null && b.getMaThuongHieu().toLowerCase().contains(keyword)) ||
                (b.getTenThuongHieu() != null && b.getTenThuongHieu().toLowerCase().contains(keyword)) ||
                (b.getmaDanhMuc() != null && b.getmaDanhMuc().toLowerCase().contains(keyword))) {
                result.add(b);
            }
        }

        return result;
    }

}