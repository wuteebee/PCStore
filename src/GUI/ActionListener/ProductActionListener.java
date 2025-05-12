package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import DTO.Product;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import BUS.ExcelBUS;
import BUS.ProductBUS;
import DTO.Product;
import GUI.Main;
import GUI.Components.Excel;
import GUI.Dialog.ThemSanPham;
import GUI.Panel.ProductDetailPanel;
import GUI.Panel.ProductPanel;

public class ProductActionListener implements ActionListener{
    private ProductPanel panel;
    private Main MainFrame;

    public ProductActionListener(ProductPanel panel,Main MainFrame){

        this.panel=panel;
        this.MainFrame=MainFrame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String id;
                switch (e.getActionCommand()) {
            case "Thêm":
            try {
                      
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                Product sp=new Product();
                ThemSanPham tsp=new ThemSanPham(panel,null);
 
                
                break;
            case "Sửa":
           System.out.println("Sửa nè");
                      id = panel.getSelectedProductId();
                     if(id.equals("-1")){
                          JOptionPane.showMessageDialog(panel, "Vui lòng chọn sản phẩm cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                     }
                     else{
                              ProductBUS productBUS=new ProductBUS();
                              Product sp1=productBUS.getProductDetail(id);
                        ThemSanPham tsp1=new ThemSanPham(panel,sp1);
                      
            
                        
                        // Truyền id sản phẩm vào
                        // Lấy ra thông tin sản phẩm từ id
                        // truyền vào form sửa
             
                     }
                break;
            case "Xóa":
              System.out.println("Xoá nè");
            //   hiện thông báo xác nhận xoá
            // chỉnh trạng thái sản phẩm thành false
            //chỉ hiện thị sản phẩm có trạng thái là true
                      id = panel.getSelectedProductId();
                     if(id.equals("-1")){
                          JOptionPane.showMessageDialog(panel, "Vui lòng chọn sản phẩm cần xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                     }
                     else{
            int result = JOptionPane.showConfirmDialog(
                panel,
                "Bạn có chắc muốn xoá sản phẩm này?",
                "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                ProductBUS productBUS=new ProductBUS();
                boolean deleted = productBUS.deleteProduct(id);
                if (deleted) {
                    JOptionPane.showMessageDialog(panel, "Xoá sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    panel.reloadPanel();
                } else {
                    JOptionPane.showMessageDialog(panel, "Xoá sản phẩm thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            }          
             
                     }
                break;
            case "search":
                
                System.out.println(panel.getTextField().getText().trim());
                // panel.printTableData();
                break;
            case "Làm mới":
                System.out.println("Làm mới nè");
                // panel.getTextField().setText("");
                // panel.getTable().setRowCount(0);
                // panel.loadDataToTable();
                break;
            case "Chi tiết":
                System.out.println("Chi tiết nè");
                      id = panel.getSelectedProductId();
                     if(id.equals("-1")){
                          JOptionPane.showMessageDialog(panel, "Vui lòng chọn sản phẩm cần xem chi tiết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                     }
                     else{
                        MainFrame.setMainPanel(new ProductDetailPanel(MainFrame,id));
                     }
                break;
            case "Xuất Excel":
               System.out.println("Excel");
                Excel panelExcel = new Excel();
                String filePath = panelExcel.ChooseFile();
                if(filePath != null) {
                    ExcelBUS excelBUS = new ExcelBUS();
                    try {
                        excelBUS.ExcelListProduct(filePath);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                break;
            case "Nhập Excel":  
                System.out.println("Nhập Excel khách hàng nè");
                break;
            
            default:
                break;
        } 
    }
}
