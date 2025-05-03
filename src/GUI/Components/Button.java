package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public class Button extends JButton {

    public JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(110, 40));
        // Kiểm tra iconPath không phải null và không rỗng
        if (iconPath != null && !iconPath.isEmpty()) {

            // Kiểm tra nếu icon là .svg
            if (iconPath.endsWith(".svg")) {
                // Xử lý SVG
                FlatSVGIcon icon = new FlatSVGIcon(iconPath, 32, 32);
                button.setIcon(icon);
    

            } else if (iconPath.endsWith(".png")) {
                java.net.URL imageURL = getClass().getResource(iconPath.startsWith("/") ? iconPath : "/" + iconPath);
                if (imageURL != null) {
                    ImageIcon icon = new ImageIcon(imageURL);
                    Image img = icon.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(img));
              
                } else {
            
                }
            }
             else {
                System.out.println("Không hỗ trợ định dạng này");
            }
        } else {
            // Nếu không có icon, set icon null
            button.setIcon(null);
 
        }
 // Vị trí icon và text
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(3); // khoảng cách giữa icon và text


        button.setMargin(new Insets(2, 3, 2, 3));

        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusable(false);
        button.setBackground(Color.WHITE);
        // button.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));
        // button.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        button.setOpaque(true);
        return button;
    }
}
