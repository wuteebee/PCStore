package GUI;

import DTO.Account;
import GUI.Components.MenuLeft;
import GUI.Panel.Trangchu;
import java.awt.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class Main extends JFrame {
    private final Account user;
    public JPanel MainContent;
    Color MainColor = new Color(250, 250, 250);
    private MenuLeft menuTaskbar;

    public Main(Account user) {
        this.user = user;
        initComponents();
    }

    public Account getCurrentAccount() {
        return user;
    }

    private void initComponents() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));
        this.setTitle("Hệ thống quản lý cửa hàng máy tính");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuTaskbar = new MenuLeft(this, user);
        menuTaskbar.setPreferredSize(new Dimension(250, 800));
        this.add(menuTaskbar, BorderLayout.WEST);

        MainContent = new JPanel(new BorderLayout());
        MainContent.setBackground(MainColor);
        this.add(MainContent, BorderLayout.CENTER);

        setMainPanel(new Trangchu());

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
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        UIManager.put("Component.opaque", true);

        new Login();
    }
}   