package BUS;

import DAO.PromotionDAO;
import DTO.Promotion;
import DTO.ComboProduct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class PromotionBUS {
    private PromotionDAO promotionDAO;

    public PromotionBUS() {
        promotionDAO = new PromotionDAO();
    }

    public List<Promotion> getAllPromotions() {
        return promotionDAO.getAllPromotions();
    }

    public boolean savePromotion(String id, String tenKM, String giaTri, String ngayBD, String ngayKT, String loai, 
                                boolean isEdit, List<ComboProduct> comboProducts) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            double giaTriValue = Double.parseDouble(giaTri);
            Promotion promotion = new Promotion();
            promotion.setIdKhuyenMai(id);
            promotion.setTenKhuyenMai(tenKM);
            promotion.setGiaTri(giaTriValue);
            promotion.setNgayBatDau(ngayBD.isEmpty() ? null : LocalDate.parse(ngayBD, formatter));
            promotion.setNgayKetThuc(ngayKT.isEmpty() ? null : LocalDate.parse(ngayKT, formatter));
            promotion.setLoai(loai);
            promotion.setTrangThai(1);

            if (isEdit) {
                boolean success = promotionDAO.updatePromotion(promotion);
                if ("Combo".equals(loai) && success) {
                    promotionDAO.deleteComboProducts(id); // Xóa sản phẩm cũ
                    for (ComboProduct product : comboProducts) {
                        product.setIdKhuyenMai(id);
                        promotionDAO.insertComboProduct(product);
                    }
                }
                return success;
            } else {
                boolean success = promotionDAO.insertPromotion(promotion);
                if ("Combo".equals(loai) && success) {
                    for (ComboProduct product : comboProducts) {
                        product.setIdKhuyenMai(id);
                        promotionDAO.insertComboProduct(product);
                    }
                }
                return success;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePromotion(String id) {
        promotionDAO.deleteComboProducts(id); // Xóa sản phẩm combo trước
        return promotionDAO.deletePromotion(id);
    }

    public List<ComboProduct> getComboProducts(String idKhuyenMai) {
        return promotionDAO.getComboProducts(idKhuyenMai);
    }

    public List<ComboProduct> getAllProducts() {
        return promotionDAO.getAllProducts();
    }

    public String generateUniqueId() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        do {
            id.setLength(0); // Clear the StringBuilder
            id.append("KM").append(String.format("%03d", random.nextInt(1000)));
        } while (promotionDAO.getAllPromotions().stream().anyMatch(p -> p.getIdKhuyenMai().equals(id.toString())));
        return id.toString();
    }
}