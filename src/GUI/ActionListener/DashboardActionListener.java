package GUI.ActionListener;

import GUI.Panel.DashboardPanel;
import GUI.Panel.DashFinance;

import java.awt.event.*;
import java.util.List;

public class DashboardActionListener implements ActionListener {
    private DashboardPanel panel;
    private DashFinance financePanel;

    public DashboardActionListener(DashboardPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Tài chính":
                panel.financeStatistics();
                break;
            case "Thống kê":
                panel.financeStatistics();
                break;
            case "Sản phẩm":
                panel.productStatistics();
                break;
            default:
                break;
        }
    }
}