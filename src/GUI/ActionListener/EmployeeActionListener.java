package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import BUS.ExcelBUS;
import GUI.Components.Excel;
import GUI.Panel.EmployeePanel;

public class EmployeeActionListener implements ActionListener {
    private EmployeePanel panel;

    public EmployeeActionListener(EmployeePanel panel) {
        this.panel = panel;
    }

 

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
  
        switch (command) {
            case "Thêm":
                panel.openAddEmployeeDialog();
                break;
            case "Sửa":
   
            String id = panel.getSelectedEmployeeId();
            // System.out.println("id nè"+ id);
            if (id != "-1") {
                panel.openEditEmployeeDialog(id);
            } else {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn nhân viên cần chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
            break;

            
    
            case "Xóa":
            id = panel.getSelectedEmployeeId();
            // System.out.println("id nè"+ id);
            if (id == "-1") {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn nhân viên cần xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
            else{
                int result = JOptionPane.showConfirmDialog(
                    panel,
                    "Bạn có chắc chắn muốn xoá thông tin nhân viên " + id + "?",
                    "Xác nhận xoá",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (result == JOptionPane.YES_OPTION) {
                    panel.openRemoveEmployeeDialog(id);
                }
            }
            
                break;
            case "Tìm kiếm":
                System.out.println("Tìm");
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
        }
    }
}
