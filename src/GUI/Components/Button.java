package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class Button extends JButton {

    public JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));

        // Kiểm tra iconPath không phải null và không rỗng
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                if (iconPath.endsWith(".svg")) {
                    // Xử lý SVG
                    FlatSVGIcon icon = new FlatSVGIcon(iconPath, 32, 32);
                    button.setIcon(icon);
                } else if (iconPath.endsWith(".png")) {
                    java.net.URL imageURL = getClass().getResource(iconPath.startsWith("/") ? iconPath : "/" + iconPath);
                    if (imageURL != null) {
                        ImageIcon icon = new ImageIcon(imageURL);
                        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // Đồng nhất kích thước icon
                        button.setIcon(new ImageIcon(img));
                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy icon: " + iconPath, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Định dạng icon không được hỗ trợ: " + iconPath, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tải icon: " + iconPath, "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            // Nếu không có icon, set icon null
            button.setIcon(null);
        }

        // Vị trí icon và text
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(2); // khoảng cách giữa icon và text

        button.setMargin(new Insets(2, 1, 2, 1));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusable(false);
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        return button;
    }
}