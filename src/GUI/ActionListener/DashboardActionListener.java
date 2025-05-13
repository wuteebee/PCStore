package GUI.ActionListener;

import GUI.Panel.DashboardPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DashboardActionListener implements ActionListener {
    private DashboardPanel panel;

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
            case "Sản phẩm":
                panel.productStatistics();
                break;
            case "Nhân viên":
                panel.employeeStatistics();
                break;
            default:
                break;
        }
    }

}
