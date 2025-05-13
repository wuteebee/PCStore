package GUI;

import java.awt.*;
import javax.swing.*;

import GUI.Components.MenuLeft;
import GUI.Panel.EmployeePanel;

import GUI.Panel.Trangchu;

public class Main extends JFrame {

    public JPanel MainContent;
    Color MainColor = new Color(250, 250, 250);

    private MenuLeft menuTaskbar;

    public Main() {
        initComponents();
    }

    private void initComponents() {
//        this.setSize(new Dimension(1920, 1080));
        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));
        this.setTitle("Hệ thống quản lý cửa hàng máy tính");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        // Truyền `this` vào `MenuLeft`
        menuTaskbar = new MenuLeft(this);

        JScrollPane scrollPane = new JScrollPane(menuTaskbar);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(250, 800));
    
        this.add(scrollPane, BorderLayout.WEST);
    
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
        new Main();
    }
}
