package GUI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class RoundedButton extends JButton {
    private int radius;
    private boolean isHovered = false; // Biến kiểm tra trạng thái hover
    private static final int OFFSET = 2; // Khoảng cách nhích lên

    public RoundedButton(String text, int radius, Icon icon) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false); // Tắt nền mặc định
        setFocusPainted(false); // Ẩn viền khi focus
        setBorderPainted(false); // Ẩn viền mặc định
        setBorder(new EmptyBorder(OFFSET, OFFSET, OFFSET, OFFSET)); // Tạo border cho nút

         // Điều chỉnh kích thước icon nhỏ lại
        if (icon != null) {
            ImageIcon imageIcon = (ImageIcon) icon;
            Image scaledImage = imageIcon.getImage().getScaledInstance(70,80, Image.SCALE_SMOOTH); // Kích thước icon nhỏ hơn (16x16)
            setIcon(new ImageIcon(scaledImage));
        }

        // Căn chỉnh icon sát bên trái và văn bản bên phải
        setHorizontalTextPosition(SwingConstants.RIGHT); // Văn bản nằm bên phải icon
        setHorizontalAlignment(SwingConstants.LEFT);     // Căn toàn bộ nội dung (icon + text) sang trái
        setIconTextGap(5);                              // Khoảng cách giữa icon và text

        // Tăng kích thước font chữ cho văn bản
        setFont(new Font("Arial", Font.BOLD, 16));      // Font chữ lớn hơn (18pt)

        // Lắng nghe sự kiện rê chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setLocation(getX(), getY() - OFFSET); // Dịch chuyển nút lên một chút
                repaint(); // Chỉ vẽ lại nút
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setLocation(getX(), getY() + OFFSET); // Quay lại vị trí ban đầu
                repaint(); // Chỉ vẽ lại nút
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Đặt màu nền và vẽ bo tròn
        if (isHovered) {
            g2.setColor(new Color(100, 150, 255)); // Màu khi hover
        } else {
            g2.setColor(getBackground()); // Màu khi không hover
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g); // Vẽ chữ lên nút
    }
}
