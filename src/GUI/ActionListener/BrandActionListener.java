package GUI.ActionListener;

import GUI.Panel.BrandPanel;
import GUI.Main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrandActionListener implements ActionListener {
    private final BrandPanel panel;
    private final Main mainFrame;

    public BrandActionListener(BrandPanel panel, Main mainFrame) {
        this.panel = panel;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Thêm":
                panel.openAddBrandDialog();
                break;
            case "Sửa":
                String idEdit = panel.getSelectedBrandId();
                if (!idEdit.equals("-1")) {
                    panel.openEditBrandDialog(idEdit);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một thương hiệu để sửa.");
                }
                break;
            case "Xóa":
                String idDelete = panel.getSelectedBrandId();
                if (!idDelete.equals("-1")) {
                    panel.openRemoveBrandDialog(idDelete);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một thương hiệu để xóa.");
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Hành động không hợp lệ: " + command);
        }
    }
}