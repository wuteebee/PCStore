package GUI.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuItem extends JButton {
    private boolean isExpanded = false;

    public MenuItem(String name, int index, boolean hasSubmenu, Runnable toggleAction) {
        super(name);
        setFont(new Font("Arial", Font.BOLD, 14));
        setHorizontalAlignment(SwingConstants.LEFT);
        setPreferredSize(new Dimension(150, 40));
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(new Color(230, 230, 230));
        setOpaque(true);

        if (hasSubmenu) {
            setBackground(new Color(220, 220, 220));
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(200, 200, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(230, 230, 230));
            }
        });

        // Bổ sung ActionListener để có thể gọi addActionListener
        addActionListener(e -> {
            if (hasSubmenu) {
                isExpanded = !isExpanded;
                toggleAction.run();
            }
        });
    }

    public boolean isExpanded() {
        return isExpanded;
    }
}
