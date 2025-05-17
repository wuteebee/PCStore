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

    public ArrayList<Supplier> searchSuppliers(String keyword) {
        keyword = keyword.toLowerCase();
        ArrayList<Supplier> result = new ArrayList<>();
        for (Supplier s : getAllSuppliers()) {
            if (s.getId().toLowerCase().contains(keyword) ||
                s.getName().toLowerCase().contains(keyword) ||
                s.getPhoneNumber().toLowerCase().contains(keyword) ||
                s.getEmail().toLowerCase().contains(keyword) ||
                s.getAddress().toLowerCase().contains(keyword) ) {
                result.add(s);
            }
        }
        return result;
    }

    public void search(String keyword) {
        dsSupplier = searchSuppliers(keyword);
    }

    public ArrayList<Supplier> getDsSupplier() {
        return dsSupplier;
    }

    public void docDanhSach() {
        dsSupplier = new ArrayList<>(getAllSuppliers());
    }

    // Biến danh sách lưu kết quả hiện tại
    private ArrayList<Supplier> dsSupplier = new ArrayList<>();

}
