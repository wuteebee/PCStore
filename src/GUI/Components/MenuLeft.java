package GUI.Components;

import GUI.Main;
import GUI.Panel.AccountPanel;
import GUI.Panel.CustomerPanel;
import GUI.Panel.EmployeePanel;
import GUI.Panel.PhieuNhapPanel;
import GUI.Panel.ProductPanel;
import GUI.Panel.PromotionPanel;
import GUI.Panel.SaleInvoicePanel;
import GUI.Panel.SupplierPanel;
import GUI.Panel.Trangchu;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuLeft extends JPanel {
    private final Map<Integer, List<JComponent>> menuSubmenus = new HashMap<>();
    private final Main mainFrame;
    private final ArrayList<itemTaskbar> listItems = new ArrayList<>();
    private final JPanel pnlTop;
    private final JPanel pnlCenter;
    private final JPanel pnlBottom;
    private final JPanel bar1;
    private final JPanel bar2;
    private final JPanel bar3;
    private final JPanel bar4;
    private final JScrollPane scrollPane;
    private final Color FontColor = new Color(96, 125, 139);
    private final Color DefaultColor = new Color(255, 255, 255);
    private final Color HowerBackgroundColor = new Color(193, 237, 220);
    private final Color HowerFontColor = new Color(1, 87, 155);

    private final String[][] menuItems = {
            {"Trang chủ", "home.svg"},
            {"Sản phẩm", "book.svg", "Laptop", "PC", "Linh kiện máy tính"},
            {"Thuộc tính", "khu_vuc.svg", "Màu sắc", "Thương hiệu", "Xuất xứ"},
            {"Phiếu nhập", "import.svg"},
            {"Phiếu xuất", "export.svg"},
            {"Khách hàng", "customer.svg"},
            {"Nhà cung cấp", "supplier.svg"},
            {"Nhân viên", "staff_1.svg", "Quản lí", "Bán hàng"},
            {"Tài khoản", "account.svg"},
            {"Thống kê", "statistical_1.svg"},
            {"Phân quyền", "protect.svg"},
            {"Khuyến mãi và ưu đãi", "sale.svg"},
            {"Đăng xuất", "log_out.svg"},
    };

    public MenuLeft(Main main) {
        this.mainFrame = main;
        setOpaque(true);
        setBackground(DefaultColor);
        setLayout(new BorderLayout(0, 0));

        pnlTop = new JPanel();
        pnlTop.setPreferredSize(new Dimension(250, 80));
        pnlTop.setBackground(DefaultColor);
        pnlTop.setLayout(new BorderLayout(0, 0));
        add(pnlTop, BorderLayout.NORTH);
        
        bar1 = new JPanel();
        bar1.setBackground(new Color(204, 214, 219));
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);

        bar2 = new JPanel();
        bar2.setBackground(new Color(204, 214, 219));
        bar2.setPreferredSize(new Dimension(0, 1));
        pnlTop.add(bar2, BorderLayout.SOUTH);

        pnlCenter = new JPanel();
        pnlCenter.setBackground(DefaultColor);
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        bar3 = new JPanel();
        bar3.setBackground(new Color(204, 214, 219));
        bar3.setPreferredSize(new Dimension(1, 1));
        add(bar3, BorderLayout.EAST);

        pnlBottom = new JPanel();
        pnlBottom.setPreferredSize(new Dimension(250, 60));
        pnlBottom.setBackground(DefaultColor);
        pnlBottom.setLayout(new BorderLayout(0, 0));
        add(pnlBottom, BorderLayout.SOUTH);

        bar4 = new JPanel();
        bar4.setBackground(new Color(204, 214, 219));
        bar4.setPreferredSize(new Dimension(1, 1));
        pnlBottom.add(bar4, BorderLayout.EAST);

        for (int i = 0; i < menuItems.length; i++) {
            addMenu(menuItems[i], i);
        }

        if (!listItems.isEmpty()) {
            listItems.get(0).isSelected = true;
            listItems.get(0).setBackground(HowerBackgroundColor);
            listItems.get(0).pnlContent.setForeground(HowerFontColor);
        }
    }

    private void addMenu(String[] menuData, int index) {
        int length = menuData.length;
        String menuName = menuData[0];
        String iconPath = menuData[1];

        itemTaskbar item = new itemTaskbar(iconPath, menuName);
        item.setPreferredSize(new Dimension(230, 45));
        item.setMaximumSize(new Dimension(230, 45));
        listItems.add(item);

        Runnable toggleAction = length > 2 ? () -> toggleSubMenu(index) : null;
        if (toggleAction != null) {
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    toggleAction.run();
                }
            });
        }

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Menu clicked: " + menuName); // Thêm log
                handleMenuClick(menuName);
                updateSelection(item);
            }
        });

        if (index == menuItems.length - 1) {
            pnlBottom.add(item, BorderLayout.CENTER);
        } else {
            pnlCenter.add(item);
        }

        List<JComponent> subMenuList = new ArrayList<>();
        if (length > 2) {
            for (int j = 2; j < length; j++) {
                String subMenuText = "  └ " + menuData[j];
                itemTaskbar subItem = new itemTaskbar("", subMenuText);
                subItem.setPreferredSize(new Dimension(230, 45));
                subItem.setMaximumSize(new Dimension(230, 45));
                subItem.setVisible(false);
                listItems.add(subItem);

                subItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        System.out.println("Submenu clicked: " + subMenuText); // Thêm log
                        handleMenuClick(subMenuText);
                        updateSelection(subItem);
                    }
                });

                subMenuList.add(subItem);
                pnlCenter.add(subItem);
            }
        }

        menuSubmenus.put(index, subMenuList);
        revalidate();
        repaint();
    }

    private void toggleSubMenu(int index) {
        List<JComponent> subMenuList = menuSubmenus.get(index);
        for (JComponent subMenu : subMenuList) {
            subMenu.setVisible(!subMenu.isVisible());
        }
        revalidate();
        repaint();
    }

    private void handleMenuClick(String menuName) {
        switch (menuName) {
            case "Trang chủ" -> mainFrame.setMainPanel(new Trangchu());
            case "Sản phẩm" -> mainFrame.setMainPanel(new ProductPanel(mainFrame));
            case "Phiếu xuất" -> mainFrame.setMainPanel(new SaleInvoicePanel(mainFrame));
            case "Phiếu nhập" -> mainFrame.setMainPanel(new PhieuNhapPanel(mainFrame));
            case "Nhân viên" -> mainFrame.setMainPanel(new EmployeePanel(mainFrame));
            case "Khách hàng" -> mainFrame.setMainPanel(new CustomerPanel(mainFrame));
            case "Nhà cung cấp" -> mainFrame.setMainPanel(new SupplierPanel(mainFrame));
            case "Tài khoản" -> mainFrame.setMainPanel(new AccountPanel(mainFrame));
            case "Khuyến mãi và ưu đãi" -> mainFrame.setMainPanel(new PromotionPanel(mainFrame));
            case "Đăng xuất" -> {
                JOptionPane.showMessageDialog(mainFrame, "Đăng xuất thành công!");
                System.exit(0);
            }
            default -> JOptionPane.showMessageDialog(mainFrame, "Chức năng đang phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateSelection(itemTaskbar selectedItem) {
        for (itemTaskbar item : listItems) {
            if (item == selectedItem) {
                item.setSelected(true);
                item.pnlContent.setForeground(HowerFontColor);
            } else {
                item.setSelected(false);
                item.pnlContent.setForeground(FontColor);
            }
        }
    }
}