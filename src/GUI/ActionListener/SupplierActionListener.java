package GUI.ActionListener;

import GUI.Components.Excel;
import GUI.Panel.SupplierPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import BUS.ExcelBUS;

public class SupplierActionListener implements ActionListener {
    private final SupplierPanel supplierPanel;

    public SupplierActionListener(SupplierPanel supplierPanel) {
        this.supplierPanel = supplierPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Thêm":
                supplierPanel.openAddSupplierDialog();
                break;
            case "Sửa":
                String editId = supplierPanel.getSelectedSupplierId();
                if (!editId.equals("-1")) {
                    supplierPanel.openEditSupplierDialog(editId);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhà cung cấp để sửa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Xóa":
                String deleteId = supplierPanel.getSelectedSupplierId();
                if (!deleteId.equals("-1")) {
                    supplierPanel.openRemoveSupplierDialog(deleteId);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhà cung cấp để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Xuất Excel":
                System.out.println("Xuất Excel");
                Excel panelExcel = new Excel();
                String filePath = panelExcel.ChooseFile();
                if (filePath != null) {
                    ExcelBUS excelBUS = new ExcelBUS();
                    try {
                        excelBUS.ExcelListSuppliers(filePath);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                break;
            case "Tìm kiếm":
                String keyword = supplierPanel.getSearchText();
                if (!keyword.isEmpty()) {
                    supplierPanel.loadSupplierTable(
                        supplierPanel.getSupplierBUS().searchSuppliers(keyword)
                    );
                }
                break;
            case "Làm mới":
                supplierPanel.clearSearchText();
                supplierPanel.loadSupplierTable();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Hành động không hợp lệ: " + command);
        }
    }
}