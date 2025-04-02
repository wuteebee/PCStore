package GUI;

import java.awt.*;
import javax.swing.*;

public class ContentPanel {
    public static JPanel getContentPanel(Color color) { // Thêm tham số Color
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1440, 1026));
        contentPanel.setLayout(null);
        contentPanel.setBackground(color); // Sử dụng tham số color
        return contentPanel;
    }
}