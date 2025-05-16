package BUS;

import java.util.ArrayList;
import java.util.List;

import DAO.SupplierDAO;
import DTO.Supplier;

public class SupplierBUS {
    public List<Supplier> getAllSuppliers() {
        SupplierDAO dao = new SupplierDAO();
        List<Supplier> suppliers = dao.getAllSuppliers();
        List<Supplier> active = new ArrayList<>();
        for (Supplier s : suppliers) {
            if (s.getTrangThai()) active.add(s);
        }
        return active;
    }

    public boolean saveSupplier(String name, String email, String phone, String address, Object unused, boolean isEdit, String id) {
        SupplierDAO dao = new SupplierDAO();
        Supplier s = new Supplier();
        s.setName(name);
        s.setEmail(email);
        s.setPhoneNumber(phone);
        s.setAddress(address);
        s.setTrangThai(true);
    
        if (isEdit) {
            s.setId(id);
            return dao.updateSupplier(s);
        } else {
            String nextId = dao.getNextSupplierId(); // ← Mã tăng dần như NCC001, NCC002,...
            s.setId(nextId);
            return dao.insertSupplier(s);
        }
    }
    

    public boolean deleteSupplier(String id) {
        return new SupplierDAO().deleteSupplier(id);
    }

    public List<Supplier> searchSuppliers(String keyword) {
        return new SupplierDAO().searchSuppliers(keyword);
    }

}
