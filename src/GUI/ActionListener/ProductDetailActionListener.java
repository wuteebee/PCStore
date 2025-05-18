package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.Main;
import GUI.Dialog.DanhSachChiTietSP;
import GUI.Dialog.ThemCauHinh;
import GUI.Panel.ProductDetailPanel;

public class ProductDetailActionListener implements ActionListener{
    private ProductDetailPanel productPanel;
    private Main mainFrame;
    
    public ProductDetailActionListener(ProductDetailPanel productPanel, Main mainFrame) {
        this.productPanel = productPanel;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       switch (e.getActionCommand()) {
        case "Thêm":
            System.out.println("Thêm cấu hình mới nè");
            ThemCauHinh tch=new ThemCauHinh(productPanel,null);
            // tch.formThemCauhinh();
            // panel.openAddCustomerDialog();
            break;
        case "Sửa":
        System.out.println("Sửa cấu hình nè");
            System.out.println("ID cấu hình nè"+ productPanel.getPhienban());
              ThemCauHinh tch1=new ThemCauHinh(productPanel,productPanel.getProduct().getDanhSachPhienBan().get(productPanel.getPhienban()-1));
            break;
        case "Xóa":
              productPanel.xoaCauHinh(productPanel.getPhienban());
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
            break;

        case "Xem DS":
            System.out.println("Xem danh sách cấu hình nè");
            System.out.println(productPanel.getPhienban());
            DanhSachChiTietSP tmp=new DanhSachChiTietSP(productPanel);
            
            break;
    
        default:
            break;
       }
        
    }
    
}
