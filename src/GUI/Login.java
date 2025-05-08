package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import GUI.Components.InputForm;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class Login extends JFrame implements KeyListener {

    // Các thành phần giao diện
    private JPanel loginPanel; // Panel chính chứa các thành phần giao diện
    private JLabel lbTitle, lbForgotPassword, lbRegister, lbImage; // Các nhãn hiển thị
    private InputForm txtUsername, txtPassword; // Các trường nhập liệu (tên đăng nhập và mật khẩu)
    private JButton btnLogin; // Nút đăng nhập

    // Constructor
    public Login() {
        init(); // Khởi tạo giao diện
        txtUsername.setText("admin"); // Giá trị mặc định cho tên đăng nhập
        txtPassword.setPass("123456"); // Giá trị mặc định cho mật khẩu
        this.setVisible(true); // Hiển thị cửa sổ
    }

    // Phương thức kiểm tra đăng nhập
    public void checkLogin() {
        String username = txtUsername.getText(); // Lấy giá trị từ trường tên đăng nhập
        String password = txtPassword.getPass(); // Lấy giá trị từ trường mật khẩu

        // Kiểm tra nếu tên đăng nhập hoặc mật khẩu để trống
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Main.isLoggedIn = true; // Cập nhật trạng thái đăng nhập
            this.dispose(); // Đóng cửa sổ đăng nhập
            new Main(); // Mở cửa sổ chính
        }
    }

    // Phương thức khởi tạo giao diện
    private void init() {
        // Cài đặt giao diện FlatLaf
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true); // Hiển thị nút xem mật khẩu

        // Cài đặt JFrame
        this.setTitle("Đăng nhập"); // Tiêu đề cửa sổ
        this.setSize(new Dimension(900, 500)); // Kích thước cửa sổ
        this.setResizable(false); // Không cho phép thay đổi kích thước
        this.setLocationRelativeTo(null); // Căn giữa cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng chương trình khi tắt cửa sổ
        this.setLayout(new BorderLayout(0, 0)); // Sử dụng BorderLayout

        // Panel hình ảnh
        imgIntro(); // Gọi phương thức để thêm hình ảnh vào giao diện

        // Panel đăng nhập
        loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE); // Màu nền trắng
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Sử dụng FlowLayout
        loginPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); // Khoảng cách viền
        loginPanel.setPreferredSize(new Dimension(500, 740)); // Kích thước panel

        // Tiêu đề
        lbTitle = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 20)); // Font chữ
        lbTitle.setForeground(Color.BLACK); // Màu chữ
        loginPanel.add(lbTitle); // Thêm tiêu đề vào panel

        // Form nhập tài khoản và mật khẩu
        JPanel panelInput = new JPanel();
        panelInput.setBackground(Color.BLACK); // Màu nền đen
        panelInput.setPreferredSize(new Dimension(400, 200)); // Kích thước panel
        panelInput.setLayout(new GridLayout(2, 1)); // Sử dụng GridLayout

        txtUsername = new InputForm("Tên đăng nhập"); // Trường nhập tên đăng nhập
        txtUsername.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Viền màu xám
        panelInput.add(txtUsername); // Thêm vào panel

        txtPassword = new InputForm("Mật khẩu", "password"); // Trường nhập mật khẩu
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Viền màu xám           
        panelInput.add(txtPassword); // Thêm vào panel

        txtUsername.getTxtForm().addKeyListener(this); // Lắng nghe sự kiện phím cho trường tên đăng nhập
        txtPassword.getTxtPass().addKeyListener(this); // Lắng nghe sự kiện phím cho trường mật khẩu

        loginPanel.add(panelInput); // Thêm panel nhập liệu vào panel chính

        // Quên mật khẩu
        lbForgotPassword = new JLabel("<html><u>Quên mật khẩu?</u></html>", JLabel.LEFT);
        lbForgotPassword.setPreferredSize(new Dimension(200, 50)); // Kích thước nhãn
        lbForgotPassword.setForeground(Color.BLACK); // Màu chữ
        lbForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lbForgotPassword.setForeground(new Color(0, 202, 232)); // Đổi màu khi di chuột
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lbForgotPassword.setForeground(Color.BLACK); // Trả lại màu ban đầu
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Chức năng quên mật khẩu chưa được triển khai!");
            }
        });
        loginPanel.add(lbForgotPassword); // Thêm nhãn vào panel

        // Đăng ký tài khoản
        lbRegister = new JLabel("<html><u>Đăng ký tài khoản?</u></html>", JLabel.RIGHT);
        lbRegister.setPreferredSize(new Dimension(200, 50)); // Kích thước nhãn
        lbRegister.setForeground(Color.BLACK); // Màu chữ
        lbRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lbRegister.setForeground(Color.GREEN); // Đổi màu khi di chuột
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lbRegister.setForeground(Color.BLACK); // Trả lại màu ban đầu
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Chức năng đăng ký tài khoản chưa được triển khai!");
            }
        });
        loginPanel.add(lbRegister); // Thêm nhãn vào panel

        // Nút đăng nhập
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE); // Màu nền trắng
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setPreferredSize(new Dimension(300, 40)); // Kích thước nút
        btnLogin.setBackground(Color.BLACK); // Màu nền nút
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16)); // Font chữ
        btnLogin.setForeground(Color.WHITE); // Màu chữ

        // Sự kiện cho nút đăng nhập
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(0, 202, 232)); // Đổi màu khi di chuột
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(Color.BLACK); // Trả lại màu ban đầu
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                checkLogin(); // Gọi phương thức kiểm tra đăng nhập
            }
        });

        buttonPanel.add(btnLogin); // Thêm nút vào panel
        loginPanel.add(buttonPanel); // Thêm panel nút vào panel chính

        this.add(loginPanel, BorderLayout.EAST); // Thêm panel chính vào cửa sổ
    }

    // Phương thức hiển thị hình ảnh
    private void imgIntro() {
        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(new EmptyBorder(3, 10, 5, 5)); // Khoảng cách viền
        imagePanel.setPreferredSize(new Dimension(400, 400)); // Kích thước panel
        imagePanel.setBackground(Color.WHITE); // Màu nền trắng
        imagePanel.setLayout(new BorderLayout()); // Sử dụng BorderLayout

        // Tải và co giãn hình ảnh
        ImageIcon originalIcon = new ImageIcon("./src/img/login.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        lbImage = new JLabel(new ImageIcon(scaledImage));
        lbImage.setHorizontalAlignment(JLabel.CENTER); // Căn giữa hình ảnh
        imagePanel.add(lbImage, BorderLayout.CENTER); // Thêm hình ảnh vào panel

        this.add(imagePanel, BorderLayout.WEST); // Thêm panel hình ảnh vào cửa sổ
    }

    // Lắng nghe sự kiện phím
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            checkLogin(); // Gọi phương thức kiểm tra đăng nhập khi nhấn Enter
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // Phương thức main để chạy chương trình
    public static void main(String[] args) {
        try {
            // Kích hoạt FlatLaf
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        new Login(); // Hiển thị form đăng nhập
    }
}