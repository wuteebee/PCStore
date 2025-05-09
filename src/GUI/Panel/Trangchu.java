package GUI.Panel;

import GUI.Components.PanelShadow;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Trangchu extends JPanel {

    JPanel top, center;
    PanelShadow content[];
    String[][] getSt = {
        {"Tính <br><br>tiện <br><br>lợi", "convenient_100px.svg", 
         "<html>Cho phép tìm kiếm nhanh chóng các sản phẩm như laptop, PC, linh kiện<br><br>theo thương hiệu, cấu hình hoặc giá cả một cách dễ dàng.</html>"},
         
        {"Tính <br><br>bảo <br><br>mật", "secure_100px.svg", 
         "<html>Thông tin cá nhân và đơn hàng của khách hàng được bảo mật tuyệt đối,<br><br>chỉ người dùng hoặc nhân viên được ủy quyền mới có quyền truy cập.</html>"},
         
        {"Tính <br><br>hiệu <br><br>quả", "effective_100px.svg", 
         "<html>Hệ thống quản lý sản phẩm thông minh giúp định danh nhanh các mã PC,<br><br>laptop, linh kiện và quản lý tồn kho một cách hiệu quả và chính xác.</html>"}
    };
    
    
    Color BackgroundColor = new Color(255, 255, 255);

    private void initComponent() {
        this.setBackground(new Color(255,255,255));
        this.setPreferredSize(new Dimension(1100, 800));
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Top panel
        top = new JPanel();
        top.setBackground(Color.WHITE);
        top.setPreferredSize(new Dimension(1100, 200));
        top.setLayout(new BorderLayout());

        JLabel slogan = new JLabel();
        try {
            if (getClass().getResource("/img/slogan.png") == null) {
                throw new Exception("Không tìm thấy slogan.png trong /img");
            }
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/slogan.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(1100, 200, Image.SCALE_SMOOTH);
            slogan.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            slogan.setText("Không thể tải hình ảnh slogan");
            slogan.setForeground(Color.RED);
            System.err.println("Lỗi tải slogan.png: " + e.getMessage());
        }
        slogan.setHorizontalAlignment(JLabel.CENTER);
        slogan.setVerticalAlignment(JLabel.CENTER);

        top.add(slogan, BorderLayout.CENTER);
        this.add(top, BorderLayout.NORTH);

        // Center panel
        center = new JPanel();
        center.setBackground(BackgroundColor);
        center.setPreferredSize(new Dimension(1100, 600));
        center.setLayout(new GridLayout(3, 1, 0, 20));
        center.setBorder(new EmptyBorder(100, 110, 30, 110));

        content = new PanelShadow[getSt.length];
        for (int i = 0; i < getSt.length; i++) {
            content[i] = new PanelShadow(getSt[i][1], getSt[i][0], getSt[i][2]);
            center.add(content[i]);
        }

        this.add(center, BorderLayout.CENTER);
    }

    public Trangchu() {
        initComponent();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Trang Chủ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        Trangchu panel = new Trangchu();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}