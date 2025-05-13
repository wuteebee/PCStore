package BUS;

import java.util.List;

import DAO.AtributeDAO;
import DTO.Brand;
import DTO.Catalog;
import DTO.ThongSoKyThuat;

public class AttributeBUS {
    private AtributeDAO attributeDAO;
    public AttributeBUS() {
        this.attributeDAO = new AtributeDAO();
    }

    public List<Catalog> getAllCatalogs() {
        return attributeDAO.getAllCatalogs();
    }
    public List<Brand> getAllBrand() {
        return attributeDAO.getAllBrand();
    }
    public List<ThongSoKyThuat> getAllThongSoKyThuats(String id) {
        return attributeDAO.getAllTechnicalParameter(id);
    }


}
