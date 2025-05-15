package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import BUS.ExcelBUS;
import GUI.Components.Excel;
import GUI.Panel.CustomerPanel;

public class CustomerActionListener implements ActionListener{
    private CustomerPanel panel;

    public CustomerActionListener(CustomerPanel panel) {
        this.panel = panel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Thêm":
                System.out.println("Thêm khách hàng nè");
                panel.openAddCustomerDialog();
                break;
            case "Sửa":
            String id = panel.getSelectedCustomerId();
            if(id != "-1") {
                System.out.println(id);
                panel.openEditCustomerDialog(id);

                // System.out.println("Sửa khách hàng nè");
            } else {
                // System.out.println("Vui lòng chọn khách hàng cần chỉnh sửa!");
            JOptionPane.showMessageDialog(panel, "Vui lòng chọn nhân viên cần chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);

            }
                break;
            case "Xóa":
             id = panel.getSelectedCustomerId();
            if(id != "-1") {
                int result = JOptionPane.showConfirmDialog(
                    panel,
                    "Bạn có chắc chắn muốn xoá thông tin khách hàng " + id + "?",
                    "Xác nhận xoá",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if(result==JOptionPane.YES_OPTION){
                    panel.openRemoveEmployeeDialog(id);
                     
                }
            } else {
           
            JOptionPane.showMessageDialog(panel, "Vui lòng chọn nhân viên cần xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);

            }
                break;
            case "Tìm":
                System.out.println("Tìm khách hàng nè");
                break;
            case "Xuất Excel":
                System.out.println("Xuất Excel");
                Excel panelExcel = new Excel();
                String filePath = panelExcel.ChooseFile();
                if (filePath != null) {
                    ExcelBUS excelBUS = new ExcelBUS();
                    try {
                        excelBUS.ExcelListCustomer(filePath);
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
