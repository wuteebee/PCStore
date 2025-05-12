package GUI;

import DAO.AccountDAO;
import DTO.Account;
import GUI.Components.InputForm;
import GUI.Dialog.QuenMatKhau;
import GUI.Dialog.RegisterDialog;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame implements KeyListener {
    private JPanel loginPanel;
    private JLabel lbTitle, lbForgotPassword, lbRegister, lbImage;
    private InputForm txtUsername, txtPassword;
    private JButton btnLogin;
    private AccountDAO accountDAO;

    public Login() {
        accountDAO = new AccountDAO();
        init();
        txtUsername.setText("admin");
        txtPassword.setPass("123456");
        this.setVisible(true);
    }

    public void checkLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getPass();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin đầy đủ", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account account = accountDAO.getAccountByUsername(username);
        if (account == null) {
            JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else if (account.getTrangThai() == 0) {
            JOptionPane.showMessageDialog(this, "Tài khoản của bạn đang bị khóa", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else if (password.equals(account.getMatKhau())) {
            this.dispose();
            new Main(account);
        } else {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init() {
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true);

        this.setTitle("Đăng nhập");
        this.setSize(new Dimension(900, 500));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(0, 0));
        JFrame jf = this;

        imgIntro();

        loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        loginPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        loginPanel.setPreferredSize(new Dimension(500, 740));

        lbTitle = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lbTitle.setForeground(Color.BLACK);
        loginPanel.add(lbTitle);

        JPanel panelInput = new JPanel();
        panelInput.setBackground(Color.BLACK);
        panelInput.setPreferredSize(new Dimension(400, 200));
        panelInput.setLayout(new GridLayout(2, 1));

        txtUsername = new InputForm("Tên đăng nhập");
        panelInput.add(txtUsername);

        txtPassword = new InputForm("Mật khẩu", "password");
        txtPassword.setPreferredSize(new Dimension(300, 40));
        panelInput.add(txtPassword);

        txtUsername.getTxtForm().addKeyListener(this);
        txtPassword.getTxtPass().addKeyListener(this);

        loginPanel.add(panelInput);

        lbForgotPassword = new JLabel("<html><u><i style='font-size: 12px;'>Quên mật khẩu?</i></u></html>", JLabel.LEFT);

        
        lbForgotPassword.setPreferredSize(new Dimension(200, 50));
        lbForgotPassword.setForeground(Color.BLACK);
        lbForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lbForgotPassword.setForeground(new Color(0, 202, 232));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lbForgotPassword.setForeground(Color.BLACK);
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                QuenMatKhau qmk = new QuenMatKhau(jf, true);
                qmk.setVisible(true);
            }
        });
        loginPanel.add(lbForgotPassword);

        

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setPreferredSize(new Dimension(300, 40));
        btnLogin.setBackground(Color.BLACK);
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(0, 202, 232));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(Color.BLACK);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                checkLogin();
            }
        });

        buttonPanel.add(btnLogin);
        loginPanel.add(buttonPanel);

        this.add(loginPanel, BorderLayout.EAST);
    }

    private void imgIntro() {
        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(new EmptyBorder(3, 10, 5, 5));
        imagePanel.setPreferredSize(new Dimension(400, 400));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setLayout(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon("./src/img/login.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        lbImage = new JLabel(new ImageIcon(scaledImage));
        lbImage.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(lbImage, BorderLayout.CENTER);

        this.add(imagePanel, BorderLayout.WEST);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            checkLogin();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        new Login();
    }
}