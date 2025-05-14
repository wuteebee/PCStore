package GUI.Panel;

import BUS.*;
import DAO.*;
import DTO.*;
import GUI.Components.MenuChucNang;
import GUI.Main;

import javax.swing.*;
import java.util.List;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private Main mainFrame;
    private InvoiceBUS salesInvoiceBUS;
    private PhieuNhapBUS comInvoiceBUS;
    private EmployeeBUS employeeBUS;
    private ProductBUS productBUS;
    private PromotionBUS promotionBUS;
    private JPanel statistics;


    public DashboardPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.statistics = new JPanel();
        this.statistics.setPreferredSize(new Dimension(1670,890));
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        initComponent();
        financeStatistics();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setSize(1920,890);
        add(createDashboardToolbar(), BorderLayout.NORTH);
        this.statistics.setLayout(new BorderLayout());
        this.add(statistics);
    }

    public JPanel createDashboardToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));
        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));
        return toolbar;
    }

    public DashFinance financeStatistics() {
        DashFinance financeStatistics = new DashFinance();
        setStatistics(financeStatistics);
        return financeStatistics;
    }

    public void productStatistics() {
        DashProduct productStatistics = new DashProduct();
        setStatistics(productStatistics);
    }

    public void setStatistics(JPanel panel) {
        statistics.removeAll();
        statistics.add(panel, BorderLayout.CENTER);
        statistics.revalidate();
        statistics.repaint();
    }
}
