package GUI.ActionListener;

import GUI.Panel.BrandPanel;
import GUI.Main;
import GUI.Components.Excel;

import javax.swing.*;

import BUS.ExcelBUS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
            case "Xuất Excel":
                System.out.println("Xuất Excel");
                Excel panelExcel = new Excel();
                String filePath = panelExcel.ChooseFile();
                if (filePath != null) {
                    ExcelBUS excelBUS = new ExcelBUS();
                    try {
                        excelBUS.ExcelListBrand(filePath);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                break;
                
            default:
                JOptionPane.showMessageDialog(null, "Hành động không hợp lệ: " + command);
        }
    }
}