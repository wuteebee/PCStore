package BUS;

import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.Product;

public class ProductBUS {
    // Xử lí chi tiết sản phẩm
    // Truyền id vào
    // Lấy thông tin cơ bản của sản phẩm
    public Product getProductDetail(String id) {
        Product product = new Product();
        ProductDAO productDAO = new ProductDAO();
        product = productDAO.getProductByIdFull(id);
        
        return product;
    }

    public void getProductInfoconfig(String id, int version) {
        Product product = new Product();
        ProductDAO productDAO = new ProductDAO();
        product = productDAO.getProductByIdFull(id);

        if (product.getDanhMuc().getMaDanhMuc().equals("DM002")) {
            product.getDanhSachPhienBan().get(0).getChitiet().forEach(item -> {
                 if(item instanceof CauHinhPC){
                    CauHinhPC item1 = (CauHinhPC) item;
                    System.out.println("ID: " + item1.getLinhKien().getMaSp());
                    System.out.println("Tên: " + item1.getLinhKien().getTenSp());
                    
                 }
            });
            
        } 
        else{
            product.getDanhSachPhienBan().get(0).getChitiet().forEach(item -> {
                if(item instanceof CauHinhLaptop){
                    CauHinhLaptop item1 = (CauHinhLaptop) item;
                    System.out.println("ID: " + item1.getIdThongTin());
                    System.out.println("Tên: " + item1.getThongTin());
                    
                 }
            });
        }
        



    }                            


    // Trả ra hình ảnh sp + cấu hình chi tiết mắc định của sản phẩm


    // truyền id vào trả số lượng phiên bản cấu hình 
    // truyền id+ phiên bản, xử lí cấu hình mặc định + cấu hình đó




}
