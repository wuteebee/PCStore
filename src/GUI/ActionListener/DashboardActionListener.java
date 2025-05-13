package GUI.ActionListener;

import GUI.Panel.DashboardPanel;
import GUI.Panel.DashFinance;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class DashboardActionListener implements ActionListener{
    private DashboardPanel panel;
    private DashFinance financePanel;
    private int hoveredSalesIndex = -1;
    private int hoveredPromoIndex = -1;
    private int hoveredComIndex = -1;

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