package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DTO.Product;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import DTO.Product;
import GUI.Main;
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
                switch (e.getActionCommand()) {
            case "Thêm":
            try {
                      
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                Product sp=new Product();
                ThemSanPham tsp=new ThemSanPham(panel);
                tsp.formThemSanPham(sp);

                
                break;
            case "Sửa":
            // String id = panel.getSelectedCustomerId();
            // if(id != "-1") {
            //     System.out.println(id);
            //     panel.openEditCustomerDialog(id);

                System.out.println("Sửa khách hàng nèww");
            // } else {
            //     // System.out.println("Vui lòng chọn khách hàng cần chỉnh sửa!");
            // JOptionPane.showMessageDialog(panel, "Vui lòng chọn nhân viên cần chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);

            // }
                break;
            case "Xóa":
            System.out.println("Xoá nèee");
            //  id = panel.getSelectedCustomerId();
            // if(id != "-1") {
            //     int result = JOptionPane.showConfirmDialog(
            //         panel,
            //         "Bạn có chắc chắn muốn xoá thông tin khách hàng " + id + "?",
            //         "Xác nhận xoá",
            //         JOptionPane.YES_NO_CANCEL_OPTION,
            //         JOptionPane.QUESTION_MESSAGE
            //     );
            //     if(result==JOptionPane.YES_OPTION){
            //         panel.openRemoveEmployeeDialog(id);
                     
            //     }


            // } else {
           
            // JOptionPane.showMessageDialog(panel, "Vui lòng chọn nhân viên cần xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);

            // }
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
                     String id = panel.getSelectedCustomerId();
                     if(id.equals("-1")){
                          JOptionPane.showMessageDialog(panel, "Vui lòng chọn sản phẩm cần xem chi tiết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                     }
                     else{
                        MainFrame.setMainPanel(new ProductDetailPanel(MainFrame,id));
                     }
                break;
            case "Xuất Excel":
                System.out.println("Xuất Excel khách hàng nè");
                break;
            case "Nhập Excel":  
                System.out.println("Nhập Excel khách hàng nè");
                break;
            
            default:
                break;
        } 
    }
}
