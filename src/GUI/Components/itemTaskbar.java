package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

public class itemTaskbar extends JPanel {
    private final Color FontColor = new Color(96, 125, 139);
    private final Color ColorBlack = new Color(26, 26, 26);
    private final Color DefaultColor = new Color(255, 255, 255);
    public final JLabel lblIcon;
    public final JLabel pnlContent;
    private final JLabel pnlContent1;
    public boolean isSelected;

    public itemTaskbar(String linkIcon, String content) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 7));
        setPreferredSize(new Dimension(230, 45));
        setBackground(DefaultColor);
        putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        lblIcon = new JLabel();
        lblIcon.setBorder(new EmptyBorder(0, 10, 0, 0));
        lblIcon.setPreferredSize(new Dimension(40, 40));
        lblIcon.setHorizontalAlignment(JLabel.CENTER);
        if (!linkIcon.isEmpty()) {
            try {
                lblIcon.setIcon(new FlatSVGIcon("./icon/" + linkIcon));
            } catch (Exception e) {
                // Không hiển thị gì nếu icon không load được
            }
        }
        add(lblIcon);

        pnlContent = new JLabel(content);
        pnlContent.setPreferredSize(new Dimension(160, 30));
        pnlContent.putClientProperty("FlatLaf.style", "font: 145% $medium.font");
        pnlContent.setForeground(ColorBlack);
        add(pnlContent);

        pnlContent1 = null;
    }

    public itemTaskbar(String linkIcon, String content1, String content2) {
        setLayout(new FlowLayout(0, 20, 50));
        setBackground(DefaultColor);
        putClientProperty(FlatClientProperties.STYLE, "arc: 15");

        lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(110, 110));
        lblIcon.setHorizontalAlignment(JLabel.CENTER);
        try {
            lblIcon.setIcon(new FlatSVGIcon("./icon/" + linkIcon));
        } catch (Exception e) {
            // Không hiển thị gì nếu icon không load được
        }
        add(lblIcon);

        pnlContent = new JLabel(content1);
        pnlContent.setPreferredSize(new Dimension(170, 30));
        pnlContent.putClientProperty("FlatLaf.style", "font: 200% $medium.font");
        pnlContent.setForeground(FontColor);
        add(pnlContent);

        pnlContent1 = null;
    }

    public itemTaskbar(String linkIcon, String content, String content2, int n) {
        setLayout(new BorderLayout(0, 0));
        setBackground(DefaultColor);

        lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(100, 100));
        lblIcon.setBorder(new EmptyBorder(0, 20, 0, 0));
        lblIcon.setHorizontalAlignment(JLabel.CENTER);
        try {
lblIcon.setIcon(new FlatSVGIcon("./icon/" + linkIcon));
        } catch (Exception e) {
            // Không hiển thị gì nếu icon không load được
        }
        add(lblIcon, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setLayout(new FlowLayout(0, 10, 0));
        center.setBorder(new EmptyBorder(20, 0, 0, 0));
        center.setOpaque(false);
        add(center);

        pnlContent = new JLabel(content);
        pnlContent.setPreferredSize(new Dimension(250, 30));
        pnlContent.putClientProperty("FlatLaf.style", "font: 300% $semibold.font");
        pnlContent.setForeground(FontColor);
        center.add(pnlContent);

        pnlContent1 = new JLabel(content2);
        pnlContent1.setPreferredSize(new Dimension(250, 30));
        pnlContent1.putClientProperty("FlatLaf.style", "font: 150% $medium.font");
        pnlContent1.setForeground(FontColor);
        center.add(pnlContent1);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (isSelected) {
            setBackground(new Color(193, 237, 220));
        } else {
            setBackground(DefaultColor);
        }
    }
}