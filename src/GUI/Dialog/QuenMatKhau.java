package GUI.Dialog;

import DAO.AccountDAO;
import DTO.Account;
import GUI.Components.InputForm;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.Random;

public class QuenMatKhau extends JDialog {
    private JPanel contentPane;
    private JLabel lblTitle, lbImage;
    private InputForm txtUsername, txtOTP, txtNewPassword, txtConfirmPassword;
    private JButton btnSendCode, btnConfirm, btnCancel;
    private AccountDAO accountDAO;
    private String generatedOTP;

    public QuenMatKhau(Frame parent, boolean modal) {
        super(parent, modal);
        accountDAO = new AccountDAO();
        initComponents();
    }

    private void initComponents() {
        // Thiết lập FlatLaf và phông chữ Roboto
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true);

        setTitle("Quên Mật Khẩu");
        setSize(new Dimension(900, 500));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        // Thêm panel hình ảnh bên trái
        imgIntro();

        // Panel nội dung chính
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        contentPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentPane.setPreferredSize(new Dimension(500, 740));

        // Tiêu đề
        lblTitle = new JLabel("ĐẶT LẠI MẬT KHẨU");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(Color.BLACK);
        contentPane.add(lblTitle);

        // Panel nhập liệu
        JPanel panelInput = new JPanel();
        panelInput.setBackground(Color.WHITE);
        panelInput.setPreferredSize(new Dimension(400, 300));
        panelInput.setLayout(new GridLayout(4, 1, 0, 10));

        // Tên đăng nhập
        txtUsername = new InputForm("Tên đăng nhập");
        panelInput.add(txtUsername);

        // Mã OTP + Nút gửi mã
        JPanel otpPanel = new JPanel(new BorderLayout(10, 0));
        otpPanel.setBackground(Color.WHITE);
        txtOTP = new InputForm("Mã OTP");
        btnSendCode = new JButton("Gửi mã");
        btnSendCode.setPreferredSize(new Dimension(120, 20));
        btnSendCode.setBackground(Color.BLACK);
        btnSendCode.setForeground(Color.WHITE);
        btnSendCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSendCode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSendCode.setBackground(new Color(0, 202, 232));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSendCode.setBackground(Color.BLACK);
            }
        });
        btnSendCode.addActionListener(e -> sendOTP());
        otpPanel.add(txtOTP, BorderLayout.CENTER);
        otpPanel.add(btnSendCode, BorderLayout.EAST);
        panelInput.add(otpPanel);

        // Mật khẩu mới
        txtNewPassword = new InputForm("Mật khẩu mới", "password");
        panelInput.add(txtNewPassword);

        // Xác nhận mật khẩu
        txtConfirmPassword = new InputForm("Xác nhận mật khẩu", "password");
        panelInput.add(txtConfirmPassword);

        contentPane.add(panelInput);

        // Panel nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Nút xác nhận
        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setPreferredSize(new Dimension(140, 40));
        btnConfirm.setBackground(Color.BLACK);
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnConfirm.setBackground(new Color(0, 202, 232));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnConfirm.setBackground(Color.BLACK);
            }
        });
        btnConfirm.addActionListener(e -> confirmResetPassword());
        buttonPanel.add(btnConfirm);

        // Nút hủy
        btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(140, 40));
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancel.setBackground(new Color(0, 202, 232));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCancel.setBackground(Color.BLACK);
            }
        });
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnCancel);

        contentPane.add(buttonPanel);

        add(contentPane, BorderLayout.EAST);

        // Thêm lắng nghe phím Enter cho các trường nhập liệu
        txtUsername.getTxtForm().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendOTP();
                }
            }
        });
        txtOTP.getTxtForm().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmResetPassword();
                }
            }
        });
        txtNewPassword.getTxtPass().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmResetPassword();
                }
            }
        });
        txtConfirmPassword.getTxtPass().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmResetPassword();
                }
            }
        });
    }

    private void imgIntro() {
        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(new EmptyBorder(3, 10, 5, 5));
        imagePanel.setPreferredSize(new Dimension(400, 400));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setLayout(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon("./src/img/reset1.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        lbImage = new JLabel(new ImageIcon(scaledImage));
        lbImage.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(lbImage, BorderLayout.CENTER);

        add(imagePanel, BorderLayout.WEST);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private boolean validateOTP(String inputOTP) {
        return inputOTP != null && inputOTP.equals(generatedOTP);
    }

    private boolean validateNewPassword(String newPassword, String confirmPassword) {
        if (newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            return false;
        }
        return newPassword.equals(confirmPassword);
    }

    private void sendOTP() {
        String username = txtUsername.getText();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account account = accountDAO.getAccountByUsername(username);
        if (account == null) {
            JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (account.getTrangThai() == 0) {
            JOptionPane.showMessageDialog(this, "Tài khoản của bạn đang bị khóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo và lưu OTP vào cơ sở dữ liệu
        generatedOTP = generateOTP();
        accountDAO.updateOTP(account.getIdTaiKhoan(), generatedOTP);
        JOptionPane.showMessageDialog(this, "Mã OTP đã được lưu vào cơ sở dữ liệu. Vui lòng lấy mã OTP từ bảng TaiKhoan.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        txtOTP.getTxtForm().requestFocus();
    }

    private void confirmResetPassword() {
        String username = txtUsername.getText();
        String otp = txtOTP.getText();
        String newPassword = txtNewPassword.getPass();
        String confirmPassword = txtConfirmPassword.getPass();

        if (username.isEmpty() || otp.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateOTP(otp)) {
            JOptionPane.showMessageDialog(this, "Mã OTP không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validateNewPassword(newPassword, confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Account account = accountDAO.getAccountByUsername(username);
        if (account != null) {
            accountDAO.updatePassword(account.getIdTaiKhoan(), newPassword);
            accountDAO.updateOTP(account.getIdTaiKhoan(), null);
            JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}