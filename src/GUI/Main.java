package GUI;

import GUI.Components.MenuLeft;
import GUI.Panel.Trangchu;
import java.awt.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;


public class Main extends JFrame {

    public JPanel MainContent;
    Color MainColor = new Color(250, 250, 250);

    private MenuLeft menuTaskbar;

    public Main() {
        initComponents();
    }

    private void initComponents() {
        // this.setSize(new Dimension(1400, 800));
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
        new Main();
    }
}