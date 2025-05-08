package GUI;

import GUI.Components.MenuLeft;
import GUI.Panel.Trangchu;
import java.awt.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Main extends JFrame {

    public static boolean isLoggedIn = false; // Biến trạng thái đăng nhập
    public JPanel MainContent;
    Color MainColor = new Color(250, 250, 250);

    private MenuLeft menuTaskbar;

    public Main() {
        if (!isLoggedIn) { // Kiểm tra trạng thái đăng nhập
            JOptionPane.showMessageDialog(null, "Bạn cần đăng nhập trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            new Login(); // Chuyển sang form Login
            this.dispose(); // Đóng Main nếu chưa đăng nhập
            return;
        }
        initComponents();
    }

    private void initComponents() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));
        this.setTitle("Hệ thống quản lý cửa hàng máy tính");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Truyền `this` vào `MenuLeft`
        menuTaskbar = new MenuLeft(this);
        menuTaskbar.setPreferredSize(new Dimension(250, 800));
        this.add(menuTaskbar, BorderLayout.WEST);

        MainContent = new JPanel(new BorderLayout());
        MainContent.setBackground(MainColor);
        this.add(MainContent, BorderLayout.CENTER);

        setMainPanel(new Trangchu()); // Mặc định mở Trang chủ

        this.setVisible(true);
    }

    public void setMainPanel(JPanel panel) {
        MainContent.removeAll();
        MainContent.add(panel, BorderLayout.CENTER);
        MainContent.revalidate();
        MainContent.repaint();
    }

    public static void main(String[] args) {
        try {
            // Kích hoạt FlatLaf
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Kiểm tra trạng thái đăng nhập
        if (!isLoggedIn) {
            new Login(); // Hiển thị form Login
        } else {
            new Main(); // Hiển thị form Main nếu đã đăng nhập
        }
    }
}