package GUI.Components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class PanelShadow extends JPanel {

  
    JPanel iconBackground;
    JLabel lblIcon, lblTitle, lblContent;

    Color FontColor = new Color(255, 255, 255);
    Color BackgroundColor = new Color(255, 255, 255);

    public PanelShadow() {
        setOpaque(false);
    }

    public PanelShadow(String linkIcon, String title, String content) {
        this.setPreferredSize(new Dimension(450, 300));
        this.setBackground(new Color(000000));
        this.putClientProperty( FlatClientProperties.STYLE, "arc: 30" );
        this.setLayout(new FlowLayout(0 ,20 , 10));
        this.setBorder(new EmptyBorder(0,10,50,0));

        iconBackground = new JPanel();
        iconBackground.setPreferredSize(new Dimension(200, 125));
        iconBackground.setBackground(BackgroundColor);
        iconBackground.putClientProperty( FlatClientProperties.STYLE, "arc: 30" );
        iconBackground.setLayout(new FlowLayout(1,20,10));

        lblIcon = new JLabel();
        lblIcon.setIcon(new FlatSVGIcon("./icon/" + linkIcon));
        iconBackground.add(lblIcon);

        this.add(iconBackground);

        lblTitle = new JLabel("<html>" + title.toUpperCase() + "</html>");
        lblTitle.setForeground(FontColor);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Đặt font 28pt, đậm
        this.add(lblTitle);

        lblContent = new JLabel(content);
        lblContent.setForeground(Color.WHITE);
        lblContent.putClientProperty("FlatLaf.style", "font: 135% $medium.font");
        this.add(lblContent);
        
    }


}
