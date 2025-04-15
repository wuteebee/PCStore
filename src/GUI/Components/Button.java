package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public class Button extends JButton {

    public JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);

        // Kiểm tra iconPath không phải null và không rỗng
        if (iconPath != null && !iconPath.isEmpty()) {
            System.out.println("Icon path: " + iconPath);

            // Kiểm tra nếu icon là .svg
            if (iconPath.endsWith(".svg")) {
                // Xử lý SVG
                FlatSVGIcon icon = new FlatSVGIcon(iconPath, 32, 32);
                button.setIcon(icon);
                System.out.println("Đã set icon SVG");

            } else if (iconPath.endsWith(".png")) {
                java.net.URL imageURL = getClass().getResource(iconPath.startsWith("/") ? iconPath : "/" + iconPath);
                if (imageURL != null) {
                    ImageIcon icon = new ImageIcon(imageURL);
                    Image img = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(img));
                    System.out.println("Load PNG thành công: " + imageURL);
                } else {
                    System.out.println("Không tìm thấy PNG icon tại: " + iconPath);
                }
            }
             else {
                System.out.println("Không hỗ trợ định dạng này");
            }
        } else {
            // Nếu không có icon, set icon null
            button.setIcon(null);
            System.out.println("Không có icon");
        }

        // Thiết lập vị trí của text và icon
        button.setVerticalTextPosition(SwingConstants.BOTTOM); 
        button.setHorizontalTextPosition(SwingConstants.CENTER); 

        // Thiết lập kích thước và các thuộc tính của button
        button.setPreferredSize(new Dimension(100, 100)); 
        button.setFont(new Font("Arial", Font.BOLD, 14)); 
        button.setFocusable(false); 
        button.setBackground(Color.WHITE); 
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        return button;
    }
}
