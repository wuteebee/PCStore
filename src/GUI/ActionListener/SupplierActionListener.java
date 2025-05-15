package GUI.ActionListener;

import GUI.Panel.SupplierPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

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
            default:
                JOptionPane.showMessageDialog(null, "Hành động không hợp lệ: " + command);
        }
    }
}