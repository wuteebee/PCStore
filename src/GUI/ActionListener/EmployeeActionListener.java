package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

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
            if (id != "-1") {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn nhân viên cần xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
                break;
            case "Tìm":
                System.out.println("Tìm");
                break;
            case "Xuất Excel":
                System.out.println("Excel");
                break;
        }
    }
}
