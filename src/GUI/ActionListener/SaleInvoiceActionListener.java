package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.Main;
import GUI.Panel.NhapPhieuXuatPanel;
import GUI.Panel.SaleInvoicePanel;

public class SaleInvoiceActionListener implements ActionListener{
    private SaleInvoicePanel panel;
    private Main MainFrame;
    public SaleInvoiceActionListener(SaleInvoicePanel panel,Main MainFrame){
        this.panel=panel;
        this.MainFrame=MainFrame;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        switch (e.getActionCommand()) {
            case "Thêm":
                MainFrame.setMainPanel(new NhapPhieuXuatPanel(MainFrame));
                break;
        
            default:
                break;
        }
    }


}