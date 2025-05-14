package GUI.ActionListener;

import GUI.Panel.PermissionPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

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
                JOptionPane.showMessageDialog(null, "Chức năng Xuất Excel đang được phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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