package GUI.ActionListener;

import GUI.Components.Excel;
import GUI.Panel.PermissionPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import BUS.ExcelBUS;

public class PermissionActionListener implements ActionListener {
    private PermissionPanel permissionPanel;

    public PermissionActionListener(PermissionPanel permissionPanel) {
        this.permissionPanel = permissionPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Thêm":
                permissionPanel.openAddPermissionGroupDialog();
                break;
            case "Sửa":
                String id = permissionPanel.getSelectedPermissionGroupId();
                if (!id.equals("-1")) {
                    permissionPanel.openEditPermissionGroupDialog(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhóm quyền để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "Xóa":
                id = permissionPanel.getSelectedPermissionGroupId();
                if (!id.equals("-1")) {
                    permissionPanel.openRemovePermissionGroupDialog(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhóm quyền để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "Xuất Excel":
                System.out.println("Xuất Excel");
                Excel panelExcel = new Excel();
                String filePath = panelExcel.ChooseFile();
                if (filePath != null) {
                    ExcelBUS excelBUS = new ExcelBUS();
                    try {
                        excelBUS.ExcelListPermissionGroup(filePath);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                break;
            case "Chi tiết":
                id = permissionPanel.getSelectedPermissionGroupId();
                if (!id.equals("-1")) {
                    permissionPanel.openViewPermissionGroupDialog(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhóm quyền để xem chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }
}