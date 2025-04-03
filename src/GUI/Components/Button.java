package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public class Button extends JButton{

    public JButton createStyledButton(String text, String iconPath) {
    JButton button = new JButton(text);
    
    // Tạo icon từ file SVG
    FlatSVGIcon icon = new FlatSVGIcon(iconPath, 32, 32);
    button.setIcon(icon);
    

    button.setVerticalTextPosition(SwingConstants.BOTTOM); 
    button.setHorizontalTextPosition(SwingConstants.CENTER); 

    // Thiết lập kích thước button
    button.setPreferredSize(new Dimension(100, 100)); 
    button.setFont(new Font("Arial", Font.BOLD, 14)); 
    button.setFocusable(false); 
    button.setBackground(Color.WHITE); 
    button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

    return button;
}

}
