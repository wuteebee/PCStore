package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

// import GUI.BanHang;
import GUI.Main;
import GUI.Panel.CustomerPanel;
import GUI.Panel.EmployeePanel;
import GUI.Panel.PhieuNhapPanel;
import GUI.Panel.ProductPanel;
import GUI.Panel.PromotionPanel;
import GUI.Panel.SupplierPanel;
import GUI.Panel.Trangchu;
import GUI.Components.MenuChucNang;
import net.miginfocom.swing.MigLayout;

public class MenuLeft extends JPanel {
    private MigLayout layout;
    private Map<Integer, List<JComponent>> menuSubmenus = new HashMap<>();
    private Main mainFrame; // Tham chiếu tới Main

    String[][] menuItems = {
            {"Trang chủ"},
            {"Sản phẩm", "Laptop", "PC", "Linh kiện máy tính"},
            {"Thuộc tính", "Màu sắc", "Thương hiệu", "Xuất xứ"},
            {"Phiếu nhập"},
            {"Phiếu xuất"},
            {"Khách hàng"},
            {"Nhà cung cấp"},
            {"Nhân viên", "Quản lí", "Bán hàng"},
            {"Tài khoản"},
            {"Thống kê"},
            {"Phân quyền"},
            {"Khuyến mãi và ưu đãi"},
            {"Đăng xuất"},
    };

    public MenuLeft(Main main) {
        this.mainFrame = main; // Lưu tham chiếu
        initComponents();
    }

    private void initComponents() {
        layout = new MigLayout("wrap 1, fillx, gapy 0px, inset 0px");
        setLayout(layout);

        for (int i = 0; i < menuItems.length; i++) {
            addMenu(menuItems[i][0], i);
        }
    }

    private void addMenu(String menuName, int index) {
        int length = menuItems[index].length;

        Runnable toggleAction = () -> toggleSubMenu(index);

        MenuItem item = new MenuItem(menuName, index, length > 1, toggleAction);
        item.addActionListener(e -> handleMenuClick(index, menuName)); // Gán sự kiện click
        add(item, "growx");

        List<JComponent> subMenuList = new ArrayList<>();

        if (length > 1) {
            for (int j = 1; j < length; j++) {
                // Lưu giá trị j vào một biến final để sử dụng trong lambda
                final String subMenuText = "  └ " + menuItems[index][j];

                MenuItem subItem = new MenuItem(subMenuText, index, false, null);
                subItem.setVisible(false);

                subItem.addActionListener(e -> handleMenuClick(index, subMenuText));

                subMenuList.add(subItem);
                add(subItem, "growx, hidemode 3");
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

    // Xử lý sự kiện khi click menu
    private void handleMenuClick(int index, String menuName) {
        switch (menuName) {
            case "Trang chủ":
                mainFrame.setMainPanel(new Trangchu());
                break;
                case "Sản phẩm":
                mainFrame.setMainPanel(new ProductPanel(mainFrame));
                break;
            case "Phiếu xuất":
                // mainFrame.setMainPanel(new BanHang());
                break;
            case "Phiếu nhập":
            System.out.println("Phiếu nhập nè");
                mainFrame.setMainPanel(new PhieuNhapPanel(mainFrame));
                break;
            case "PC":
                // case "Linh kiện máy tính":
                //     mainFrame.setMainPanel(new ProductPanel()); // Tạo class ProductPanel để hiển thị danh sách sản phẩm
                break;
            case "Nhân viên":
                mainFrame.setMainPanel(new EmployeePanel(mainFrame));
                break;
            case "Khách hàng":
                mainFrame.setMainPanel(new CustomerPanel(mainFrame));
                break;
            case "Nhà cung cấp":
                mainFrame.setMainPanel(new SupplierPanel(mainFrame));
                break;
            case "Quản lí":
                // case "Bán hàng":
                //     mainFrame.setMainPanel(new EmployeePanel());
                //     break;
                // case "Khách hàng":
                //     mainFrame.setMainPanel(new CustomerPanel());
                //     break;
                // case "Thống kê":
                //     mainFrame.setMainPanel(new StatisticsPanel());
                //     break;
            case "Khuyến mãi và ưu đãi":
                mainFrame.setMainPanel(new PromotionPanel());
                break;
            case "Đăng xuất":
                JOptionPane.showMessageDialog(mainFrame, "Đăng xuất thành công!");
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(mainFrame, "Chức năng đang phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
}
