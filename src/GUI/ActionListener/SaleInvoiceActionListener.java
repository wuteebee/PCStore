package GUI.ActionListener;

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
                break;
            case "Xóa":
                System.out.println("Thêm dialog xóa");
                break;
            case "Chi tiết":
                System.out.println("Thêm DS chi tiết");
                if (panel.getSelectedPhieuId() != "-1") {
                    dialog = new PhieuXuatDialog(MainFrame, panel, 2, panel.getSelectedPhieuId());
                    dialog.setVisible(true);
                }
                break;
            default:
                return;
        }
    }
}