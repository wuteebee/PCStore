package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame {

    // Khai báo các thành phần của giao diện
    JPanel titleBar = new JPanel();
    JPanel contentPanel = new JPanel();     // Panel chứa nội dung khi đổi tab
    JPanel menuPanel = new JPanel();        // Panel chứa menu chức năng
    CardLayout cardLayout;

    // Các panel hiển thị nội dung theo chức năng
    JPanel nhapHang = ContentPanel.getContentPanel(Color.RED);
    JPanel banHang = ContentPanel.getContentPanel(Color.ORANGE);
    JPanel quanLyNhanVien = ContentPanel.getContentPanel(Color.GREEN);
    JPanel quanLyKhachHang = ContentPanel.getContentPanel(Color.BLUE);
    JPanel quanLySanPham = ContentPanel.getContentPanel(Color.MAGENTA);
    JPanel quanLyKhuyenMaiVaUuDai = new QuanLyKhuyenMaiVaUuDai(); // Thay bằng QuanLyKhuyenMaiVaUuDai
    JPanel dashboard = ContentPanel.getContentPanel(Color.BLACK);

    // Khởi tạo trang giao diện chính
    public void init() {
        setUndecorated(true);
        setSize(1440, 810);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        titleBar = TitleBar();
        menuPanel = MenuPanel();
        contentPanel();

        add(titleBar, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Tùy chỉnh thanh title bar
    public JPanel TitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setPreferredSize(new Dimension(1440, 40));
        titleBar.setBorder(BorderFactory.createEmptyBorder());
        titleBar.setBackground(Color.BLUE);
        return titleBar;
    }

    // Tùy chỉnh menu chức năng
    public JPanel MenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(360, 500));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(-5, -200, -5, -200));
        menuPanel.setBackground(Color.RED);
        menuPanel.setLayout(null);

        // Tùy chỉnh chung của các nút chức năng
        class menuButton extends JButton {
            menuButton(String title) {
                super(title);
                setPreferredSize(new Dimension(226, 60));
                setMargin(new Insets(0, 0, 0, 0));
            }
        }

        // Khởi tạo các nút chức năng
        menuButton bDashboard = new menuButton("Dashboard");
        menuButton bNhapHang = new menuButton("Nhập hàng");
        menuButton bBanHang = new menuButton("Bán hàng");
        menuButton bKhachHang = new menuButton("Quản lý khách hàng");
        menuButton bNhanVien = new menuButton("Quản lý nhân viên");
        menuButton bSanPham = new menuButton("Sản phẩm");
        menuButton bKhuyenMaiUuDai = new menuButton("Quản lý khuyến mãi và ưu đãi");

        // Thêm ActionListener cho các nút
        bDashboard.addActionListener(new ButtonListener("Dashboard", this));
        bNhapHang.addActionListener(new ButtonListener("Quan ly nhap hang", this));
        bBanHang.addActionListener(new ButtonListener("Quan ly ban hang", this));
        bKhachHang.addActionListener(new ButtonListener("Quan ly khach hang", this));
        bNhanVien.addActionListener(new ButtonListener("Quan ly nhan vien", this));
        bSanPham.addActionListener(new ButtonListener("Quan ly san pham", this));
        bKhuyenMaiUuDai.addActionListener(new ButtonListener("Quan ly khuyen mai va uu dai", this));

        // Tạo panel cho các nút
        JPanel component = new JPanel();
        component.setPreferredSize(new Dimension(360, 7 * 60 + 8 * 38));
        component.setBackground(Color.GREEN);
        component.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 38));
        component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Thêm các nút
        component.add(bDashboard);
        component.add(bNhapHang);
        component.add(bBanHang);
        component.add(bKhachHang);
        component.add(bNhanVien);
        component.add(bSanPham);
        component.add(bKhuyenMaiUuDai);

        // Tạo scrollpane cho panel chứa các nút
        JScrollPane scrollPane = new JScrollPane(component, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 270, 360, 500);

        // Tạo panel chứa avatar cho người dùng
        JPanel Avatar = new JPanel();
        Avatar.setBounds(0, 0, 360, 270);
        Avatar.setPreferredSize(new Dimension(360, 270));

        menuPanel.add(Avatar);
        menuPanel.add(scrollPane);

        return menuPanel;
    }

    // Tùy chỉnh panel hiện nội dung
    public void contentPanel() {
        JPanel contentPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;

        // THÊM CÁC CHỨC NĂNG CẦN HIỂN THỊ
        contentPanel.add("Dashboard", dashboard);
        contentPanel.add("Quan ly ban hang", banHang);
        contentPanel.add("Quan ly nhap hang", nhapHang);
        contentPanel.add("Quan ly nhan vien", quanLyNhanVien);
        contentPanel.add("Quan ly khach hang", quanLyKhachHang);
        contentPanel.add("Quan ly san pham", quanLySanPham);
        contentPanel.add("Quan ly khuyen mai va uu dai", quanLyKhuyenMaiVaUuDai);
    }

    // Hàm main
    public static void main(String[] args) {
        MainFrame a = new MainFrame();
        a.init();
    }
}

class ButtonListener implements ActionListener {
    String panelName;
    MainFrame obj;

    public ButtonListener(String targetPanel, MainFrame obj) {
        panelName = targetPanel;
        this.obj = obj;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        obj.cardLayout.show(obj.contentPanel, panelName);
    }
}

