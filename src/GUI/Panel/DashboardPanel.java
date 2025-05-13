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
//        this.salesInvoiceBUS = new InvoiceBUS(new InvoiceDAO());
//        this.comInvoiceBUS = new PhieuNhapBUS(new PhieuNhapDAO());
//        this.productBUS = new ProductBUS();
//        this.employeeBUS = new EmployeeBUS(new EmployeeDAO());
//        this.promotionBUS = new PromotionBUS();

        this.mainFrame = mainFrame;
        this.statistics = new JPanel();
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        int height = dm.getHeight();
        int width = dm.getWidth();
        statistics.setPreferredSize(new Dimension(width - 250,height - 100));
        initComponent();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createDashboardToolbar(), BorderLayout.NORTH);
        this.statistics.setLayout(new BorderLayout());
        this.add(statistics);
        financeStatistics();
        System.out.println(statistics.getSize());


    }

    public JPanel createDashboardToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));
        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));
        return toolbar;
    }

    public void financeStatistics() {
        DashFinance financeStatistics = new DashFinance(salesInvoiceBUS, comInvoiceBUS, promotionBUS);


        setStatistics(financeStatistics);
    }

    public void productStatistics() {
        DashProduct productStatistics = new DashProduct();
        setStatistics(productStatistics);
    }

    public void employeeStatistics() {
        DashEmployee employeeStatistics = new DashEmployee();
        setStatistics(employeeStatistics);
    }

    public void setStatistics(JPanel panel) {
        statistics.removeAll();
        statistics.add(panel, BorderLayout.CENTER);
        statistics.revalidate();
        statistics.repaint();
    }

}
