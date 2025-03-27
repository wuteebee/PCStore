package org.PCStore.view;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame{
    JPanel titleBar = new JPanel();
    JPanel contentPanel = new JPanel();
    JPanel menuPanel = new JPanel();
    JPanel NhapHang = new JPanel();
    JPanel BanHang = new JPanel();
    JPanel QuanLyNhanVien = new JPanel();

    BanHang banHang = new BanHang();


    public void init() {

        setUndecorated(true);
        setSize(1440, 810 );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        titleBar = TitleBar();
        menuPanel = MenuPanel();
        contentPanel = ContentPanel.getContentPanel();

        add(titleBar, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public JPanel TitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setPreferredSize(new Dimension(1440, 40));
        titleBar.setBorder(BorderFactory.createEmptyBorder());
        titleBar.setBackground(Color.BLUE);
        return titleBar;
    }

    public JPanel MenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(360,500));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(-5, -200, -5, -200));
        menuPanel.setBackground(Color.RED);
        menuPanel.setLayout(null);

//        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5, 38));

        class menuButton extends JButton {
            menuButton(String title) {
                super(title);
                setPreferredSize(new Dimension(226,60));
                setMargin(new Insets(0,0,0,0));
            }
        }

        JTabbedPane menuTab = new JTabbedPane();
        menuButton bNhapHang = new menuButton("Nhập hàng");
        menuButton bBanHang = new menuButton("Bán hàng");
        menuButton bKhachHang = new menuButton("Quản lý khách hàng");
        menuButton bNhanVien = new menuButton("Quản lý nhân viên");
        menuButton bSanPham = new menuButton("Sản phẩm");
        menuButton bKhuyenMaiUuDai = new menuButton("Quản lý khuyến mãi và ưu đãi");

        menuTab.addTab("Quan ly ban hang", bBanHang);
        menuTab.addTab("Quan ly nhan vien", bNhanVien);
        menuTab.addTab("Quan ly san pham", bSanPham);
        menuTab.addTab("Quan ly khuyen mai uu dai", bKhuyenMaiUuDai);
        menuTab.addTab("Khach hang", bKhachHang);
        menuTab.addTab("Nhap hang", bNhapHang);

        JPanel component = new JPanel();
        component.setPreferredSize(new Dimension(360, 6 * 60 + 7 * 38));
        component.setBackground(Color.GREEN);
        component.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 38));
        component.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        component.add(bNhapHang);
        component.add(bBanHang);
        component.add(bKhachHang);
        component.add(bNhanVien);
        component.add(bSanPham);
        component.add(bKhuyenMaiUuDai);

        JScrollPane scrollPane = new JScrollPane(component, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0,270,360,500);

        JPanel Avatar = new JPanel();
        Avatar.setBounds(0,0,360,270);
        Avatar.setPreferredSize(new Dimension(360, 270));

        menuPanel.add(Avatar);
        menuPanel.add(scrollPane);



        return menuPanel;
    }

    public static void main(String[] args) {
        MainFrame a = new MainFrame();
        a.init();
    }
}
