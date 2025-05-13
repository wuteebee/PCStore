package GUI.Panel;

import javax.swing.*;
import java.awt.*;

public class DashEmployee extends JPanel {
    public DashEmployee() {
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        int height = dm.getHeight();
        int width = dm.getWidth();
        setPreferredSize(new Dimension(width - 250,height - 100));
        this.setOpaque(true);
        this.setBackground(Color.YELLOW);
    }
}
