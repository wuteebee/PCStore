package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import BUS.ExcelBUS;
import BUS.ProductBUS;
import DTO.Product;
import GUI.Main;
import GUI.Components.Excel;
import GUI.Dialog.CTNhap;
import GUI.Dialog.ThemSanPham;
import GUI.Panel.NhapHoaDonPanel;
import GUI.Panel.PhieuNhapPanel;
import GUI.Panel.ProductDetailPanel;
import GUI.Panel.Trangchu;

public class PhieuNhapActionListener implements ActionListener{
    private PhieuNhapPanel panel;
    private Main MainFrame;
     private String id;


    public PhieuNhapActionListener(PhieuNhapPanel panel, Main MainFrame) {
        this.panel = panel;
        this.MainFrame = MainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
              String id;
                switch (e.getActionCommand()) {
            case "Thêm":
              MainFrame.setMainPanel(new NhapHoaDonPanel(MainFrame,false));
                
                break;
            case "Sửa":
              System.out.println("Sửa nè");
              id = panel.getSelectedPhieuId();
                  if(id.equals("-1")){
                          JOptionPane.showMessageDialog(panel, "Vui lòng chọn hoá đơn nhập cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                     }
                     else{
                       if (panel.fixHoadon()) {
                            JOptionPane.showMessageDialog(null, "Sửa phiếu nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            MainFrame.setMainPanel(new PhieuNhapPanel(MainFrame)); // hoặc panel phù hợp bạn muốn quay về
                        } else {
                            JOptionPane.showMessageDialog(null, "Xoá thất bại! Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                     }
             
        
                break;
            case "Xóa":
              System.out.println("Xoá nè");
              id = panel.getSelectedPhieuId();
                  if(id.equals("-1")){
                          JOptionPane.showMessageDialog(panel, "Vui lòng chọn hoá đơn nhập cần xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                     }
                     else{
                       if (panel.deleteHoaDon()) {
                            JOptionPane.showMessageDialog(null, "Xoá phiếu nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            MainFrame.setMainPanel(new PhieuNhapPanel(MainFrame)); // hoặc panel phù hợp bạn muốn quay về
                        } else {
                            JOptionPane.showMessageDialog(null, "Xoá thất bại! Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                     }
             
                break;
            case "Chi tiết":
                System.out.println("Chi tiết nè");
                  id = panel.getSelectedPhieuId();

                               if(id.equals("-1")){
                          JOptionPane.showMessageDialog(panel, "Vui lòng chọn hoá đơn nhập cần xem chi tiết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                     }
                     else{
                                 CTNhap cTNhap=new CTNhap(panel,MainFrame,id);

                     }
      
                break;
            case "Xuất Excel":
               System.out.println("Excel");
                    Excel panelExcel = new Excel();
                String filePath = panelExcel.ChooseFile();
                if(filePath != null) {
                    ExcelBUS excelBUS = new ExcelBUS();
                    try {
                        excelBUS.ExcelListEmployee(filePath);
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
