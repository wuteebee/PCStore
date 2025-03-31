package org.PCStore.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ContentPanel {
    public static JPanel getContentPanel(Color color) {
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1440, 1026));
        contentPanel.setLayout(null);
        contentPanel.setBackground(color);
        return contentPanel;
    }
}
