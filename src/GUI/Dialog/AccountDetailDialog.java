package GUI.Dialog;

import BUS.AccountBUS;
import DTO.Account;
import GUI.Components.Button;
import GUI.Panel.AccountPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AccountDetailDialog extends JDialog {
    private AccountBUS accountBUS;

    public AccountDetailDialog(AccountPanel parent, String idTaiKhoan) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Chi tiết tài khoản", true);
        accountBUS = new AccountBUS();
        initComponents(idTaiKhoan);
        setSize(800, 450); // Tăng chiều rộng để có thêm không gian cho thông tin
        setLocationRelativeTo(parent);
    }

    private void initComponents(String idTaiKhoan) {
        Account account = accountBUS.getAllAccounts().stream()
                .filter(a -> a.getIdTaiKhoan().equals(idTaiKhoan)).findFirst().orElse(null);
        if (account == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setLayout(new BorderLayout(10, 10));

        // Nội dung chính: Sử dụng BorderLayout để phân chia không gian
        JPanel mainPanel = new JPanel(new BorderLayout(20, 10)); // Tăng khoảng cách ngang giữa leftPanel và rightPanel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Bên trái: Ảnh đại diện
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        JLabel lblAnhDaiDien = new JLabel();
        lblAnhDaiDien.setHorizontalAlignment(JLabel.CENTER);
        lblAnhDaiDien.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblAnhDaiDien.setPreferredSize(new Dimension(200, 200)); // Kích thước cố định 200x200
        if (account.getAnhDaiDien() != null) {
            try {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(account.getAnhDaiDien()));
                Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblAnhDaiDien.setIcon(new ImageIcon(scaledImg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        leftPanel.add(lblAnhDaiDien, BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(200, 200)); // Đảm bảo phần ảnh không bị kéo giãn
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Bên phải: Các trường thông tin
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Giảm khoảng cách giữa nhãn và JTextField
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Cho phép rightPanel mở rộng tự nhiên

        // Mã tài khoản
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(new JLabel("Mã TK:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaTK = new JTextField(account.getIdTaiKhoan());
        txtMaTK.setEditable(false);
        txtMaTK.setPreferredSize(new Dimension(200, 30)); // Giữ chiều rộng 200
        rightPanel.add(txtMaTK, gbc);

        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 1;
        rightPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        JTextField txtTenDangNhap = new JTextField(account.getTenDangNhap());
        txtTenDangNhap.setEditable(false);
        txtTenDangNhap.setPreferredSize(new Dimension(200, 30)); // Giữ chiều rộng 200
        rightPanel.add(txtTenDangNhap, gbc);


        // Nhân viên
        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1;
        JTextField txtNhanVien = new JTextField(accountBUS.getEmployeeName(account.getIdNhanVien()));
        txtNhanVien.setEditable(false);
        txtNhanVien.setPreferredSize(new Dimension(200, 30)); // Giữ chiều rộng 200
        rightPanel.add(txtNhanVien, gbc);

        // Nhóm quyền
        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(new JLabel("Nhóm quyền:"), gbc);
        gbc.gridx = 1;
        JTextField txtNhomQuyen = new JTextField(accountBUS.getPermissionGroupName(account.getIdNhomQuyen()));
        txtNhomQuyen.setEditable(false);
        txtNhomQuyen.setPreferredSize(new Dimension(200, 30)); // Giữ chiều rộng 200
        rightPanel.add(txtNhomQuyen, gbc);

      

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Nút Đóng
        Button buttonFactory = new Button();
        JButton btnDong = buttonFactory.createStyledButton("Đóng", "./resources/icon/exit.png");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnDong);

        btnDong.addActionListener(e -> dispose());

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}