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
        setLayout(new GridBagLayout());

        titleBar = TitleBar();
        menuPanel = MenuPanel();
        contentPanel = ContentPanel.getContentPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 1440;
        gbc.ipady = 100;
        gbc.gridwidth = 2;
        add(titleBar, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipadx = 360;
        gbc.ipady = 770;
        gbc.gridwidth = 1;
        add(menuPanel, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipadx = 1080;
        gbc.ipady = 770;
        gbc.gridwidth = 1;
        add(contentPanel, gbc);

        setVisible(true);
    }

    public JPanel TitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setPreferredSize(new Dimension(1440, 600));
//        titleBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        titleBar.setBackground(Color.BLUE);
        return titleBar;
    }

    public JPanel MenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(480, 1026));
//        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        menuPanel.setBackground(Color.RED);
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5, 38));

        class menuButton extends JButton {
            menuButton(String title) {
                super(title);
                setPreferredSize(new Dimension(330,60));
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

        

        menuPanel.add(bNhapHang);
        menuPanel.add(bBanHang);
        menuPanel.add(bKhachHang);
        menuPanel.add(bNhanVien);
        menuPanel.add(bSanPham);
        menuPanel.add(bKhuyenMaiUuDai);



        return menuPanel;
    }

    public static void main(String[] args) {
        MainFrame a = new MainFrame();
        a.init();
    }
}
