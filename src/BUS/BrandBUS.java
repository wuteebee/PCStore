package BUS;

import DAO.BrandDAO;
import DTO.Brand;
import DAO.DanhMucDAO;
import java.util.List;

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
}