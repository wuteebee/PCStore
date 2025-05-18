package GUI.ActionListener;

import BUS.InvoiceBUS;
import GUI.Dialog.PhieuXuatDialog;
import GUI.Main;
import GUI.Panel.SaleInvoicePanel;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaleInvoiceActionListener implements ActionListener {

    private SaleInvoicePanel panel;
    private Main MainFrame;
    PhieuXuatDialog dialog;
    public SaleInvoiceActionListener(SaleInvoicePanel panel, Main MainFrame)
    {
        this.panel = panel;
        this.MainFrame = MainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action)
        {
            case "Thêm":
                System.out.println("Thêm dialog thêm");
                dialog = new PhieuXuatDialog(MainFrame, panel, 0, "-1");
                dialog.setVisible(true);
                break;
            case "Sửa":
                System.out.println("Thêm dialog sửa");
                if (panel.getSelectedPhieuId() != "-1") {
                    dialog = new PhieuXuatDialog(MainFrame, panel, 1, panel.getSelectedPhieuId());
                    dialog.setVisible(true);
                }
                else JOptionPane.showMessageDialog(panel, "Chọn một phiếu trong bảng để sửa");
                break;
            case "Xóa":
                System.out.println("Thêm dialog xóa");
                if (panel.getSelectedPhieuId() != "-1") {
                    int confirm = JOptionPane.showConfirmDialog(panel, "Chắc chắn muốn xóa phiếu này?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == 0) {
                        InvoiceBUS invoiceBUS = new InvoiceBUS();
                        if (invoiceBUS.deleteSalesInvoice(panel.getSelectedPhieuId())) {
                            JOptionPane.showMessageDialog(panel, "Phiếu đã xóa");
                            panel.refreshTable();
                        }
                    }
                }
                else
                    JOptionPane.showMessageDialog(panel, "Chọn một phiếu trong bảng để xóa");
                break;
            case "Chi tiết":
                System.out.println("Coi chi tiet hoa don xuat");
                if (panel.getSelectedPhieuId() != "-1") {
                    dialog = new PhieuXuatDialog(MainFrame, panel, 2, panel.getSelectedPhieuId());
                    dialog.setVisible(true);
                }
                else JOptionPane.showMessageDialog(panel, "Chọn một phiếu để xem chi tiết");
                break;
            default:
                return;
        }
    }
}