package BUS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.ChiTietCauHinh;
import DTO.Product;
import DTO.ProductDetail;
import DTO.Variant;

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

    public List<ChiTietCauHinh> getProductInfoconfig(String id, String version) {
     
        List <ChiTietCauHinh> danhsach= new ArrayList<>();
        Product product = new Product();
        ProductDAO productDAO = new ProductDAO();
        product = productDAO.getProductByIdFull(id);


        if (product.getDanhMuc().getMaDanhMuc().equals("DM002")) {
            product.getDanhSachPhienBan().forEach(item -> {
                if(item.getPhienBan()==Integer.parseInt(version)){
                    item.getChitiet().forEach(item1->{
                         danhsach.add(item1);
                    });
                }
                

            });
            
        } 
        else{
            product.getDanhSachPhienBan().forEach(item->{

                if(item.getPhienBan()==Integer.parseInt(version)){
                    item.getChitiet().forEach(item1->{
                            // if(item1 instanceof CauHinhLaptop){
                            //     CauHinhLaptop item2 = (CauHinhLaptop) item1;
                            //     System.out.println("ID: " + item2.getIdThongTin()+ " " +"Thông tin "+item2.getThongTin());
                            //     danhsach.add(item2);
                            // }  
                            danhsach.add(item1);
                    });
                }
          
            });

   
        }
    
        


return danhsach;
    }                            


    public List<ProductDetail> getProductDetailList(int id) {
        List<ProductDetail> productDetails = new ArrayList<>();
        ProductDAO productDAO = new ProductDAO();
        productDetails = productDAO.getAllProductDetails(id);
        // Lấy danh sách chi tiết sản phẩm
        // Từ đó lấy mã phiếu nhập mã phiếu xuất


        // ProductDAO productDAO = new ProductDAO();
        // productDetails = productDAO.getProductDetailById(id);
        return productDetails;
    }



    // public String insertSP(Product sp) {
    //     if (sp.getTenSp() == null || sp.getTenSp().trim().isEmpty()) {
    //         return "Vui lòng nhập tên sản phẩm!";
    //     }
    //     if (sp.getGiasp() <= 0) {
    //         return "Giá sản phẩm phải lớn hơn 0!";
    //     }
    //     if (sp.getDanhMuc() == null) {
    //         return "Chưa chọn danh mục!";
    //     }
    //     if (sp.getThuongHieu() == null) {
    //         return "Chưa chọn thương hiệu!";
    //     }
    //     if (sp.getAnhSanPham() == null) {
    //         return "Chưa chọn ảnh sản phẩm!";
    //     }
    //     if (sp.getMoTaSanPham() == null || sp.getMoTaSanPham().trim().isEmpty()) {
    //         return "Vui lòng nhập mô tả sản phẩm!";
    //     }

    //    ProductDAO productDAO = new ProductDAO();
    //     boolean success = productDAO.insertSP(sp);
    //     return success ? null : "Lỗi khi thêm sản phẩm vào CSDL!";
    // }

    public List<Product> getSPbyCatalog(String id){
        ProductDAO productDAO=new ProductDAO();
        return productDAO.getAllProductsbyCT(id);
    }

    public boolean insertCauHinh(int STTPL,String idSP,String idThongTin,String idLinhKien,boolean isPc){
        ProductDAO productDAO=new ProductDAO();
        boolean Save=false;
        if (isPc){
          Save= productDAO.insertCauHinhPC(idSP,idThongTin,idLinhKien,STTPL);
        }
        else{
            Save= productDAO.insertCauHinh(idSP,idThongTin,idLinhKien,STTPL);
        }



        return Save;

    }
 public String saveProduct(Product sp) {
    if (sp.getTenSp() == null || sp.getTenSp().trim().isEmpty()) {
        return "Vui lòng nhập tên sản phẩm!";
    }
    if (sp.getGiasp() <= 0) {
        return "Giá sản phẩm phải lớn hơn 0!";
    }
    if (sp.getDanhMuc() == null) {
        return "Chưa chọn danh mục!";
    }
    if (sp.getThuongHieu() == null) {
        return "Chưa chọn thương hiệu!";
    }
    if (sp.getAnhSanPham() == null) {
        return "Chưa chọn ảnh sản phẩm!";
    }
    if (sp.getMoTaSanPham() == null || sp.getMoTaSanPham().trim().isEmpty()) {
        return "Vui lòng nhập mô tả sản phẩm!";
    }

    ProductDAO productDAO = new ProductDAO();
    boolean success;

    if (sp.getMaSp() == "" || sp.getMaSp() == null) {
        // Thêm mới
        success = productDAO.insertSP(sp);
        return success ? null : "Lỗi khi thêm sản phẩm vào CSDL!";
    } else {
        // Cập nhật
        success = productDAO.updateProduct(sp);
        return success ? null : "Lỗi khi cập nhật sản phẩm!";
    }
}

public boolean deleteProduct(String id) {
    ProductDAO productDAO = new ProductDAO();
    return productDAO.deleteProduct(id);

}
public Product getProductByIdHthi(String id) {
    ProductDAO productDAO = new ProductDAO();
    Product product = productDAO.getProductByIdFull(id);

    Iterator<Variant> iterator = product.getDanhSachPhienBan().iterator();
    while (iterator.hasNext()) {
        Variant item = iterator.next();
        if (!item.isTrangThai()) {
            iterator.remove(); // Xóa an toàn khi đang lặp
        }
    }

    System.out.println("Số item: " + product.getDanhSachPhienBan().size());
    return product;
}

   public String getmaSPbyIdPL(int id){
    ProductDAO productDAO=new ProductDAO();
    return productDAO.getProductIDbyMaPhanLoai(id);
   }
   public int getphienbanbyIdPL(int id){
    ProductDAO productDAO=new ProductDAO();
    return productDAO.getphienbanbyIdPL(id);
   }
}