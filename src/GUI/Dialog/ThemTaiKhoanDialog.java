package GUI.Dialog;

import BUS.AccountBUS;
import DTO.Account;
import GUI.Components.Button;
import GUI.Panel.AccountPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ThemTaiKhoanDialog extends JDialog {
    private JTextField txtMaTK, txtTenDangNhap, txtMatKhau;
    private JComboBox<String> cbNhanVien, cbNhomQuyen;
    private JLabel lblAnhDaiDien;
    private JButton btnChonAnh;
    private AccountBUS accountBUS;
    private boolean isEditMode;
    private AccountPanel parentPanel;
    private byte[] anhDaiDien;
    private String selectedNhanVien;
    private String selectedNhomQuyen;

    public ThemTaiKhoanDialog(AccountPanel parent, Account account) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), account == null ? "Thêm tài khoản" : "Sửa tài khoản", true);
        this.parentPanel = parent;
        this.accountBUS = new AccountBUS();
        this.isEditMode = account != null;
        this.anhDaiDien = isEditMode ? account.getAnhDaiDien() : null;
        this.selectedNhanVien = isEditMode ? account.getIdNhanVien() : null;
        this.selectedNhomQuyen = isEditMode ? account.getIdNhomQuyen() : null;
        initComponents(account);
        setSize(700, 450);
        setLocationRelativeTo(parent);
    }

    private void initComponents(Account account) {
        setLayout(new BorderLayout(10, 10));

        // Nội dung chính: Chia thành hai nửa trái-phải
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Bên trái: Ảnh đại diện và nút chọn ảnh
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        lblAnhDaiDien = new JLabel();
        lblAnhDaiDien.setHorizontalAlignment(JLabel.CENTER);
        lblAnhDaiDien.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblAnhDaiDien.setPreferredSize(new Dimension(200, 200));
        if (isEditMode && anhDaiDien != null) {
            try {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(anhDaiDien));
                Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblAnhDaiDien.setIcon(new ImageIcon(scaledImg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        btnChonAnh = new Button().createStyledButton("Chọn ảnh", null);
        btnChonAnh.setPreferredSize(new Dimension(100, 30));
        btnChonAnh.addActionListener(e -> chooseImage());
        leftPanel.add(lblAnhDaiDien, BorderLayout.CENTER);
        leftPanel.add(btnChonAnh, BorderLayout.SOUTH);
        mainPanel.add(leftPanel);

        // Bên phải: Các trường thông tin
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã tài khoản
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(new JLabel("Mã TK:"), gbc);
        gbc.gridx = 1;
        txtMaTK = new JTextField(isEditMode ? account.getIdTaiKhoan() : accountBUS.generateAccountId());
        txtMaTK.setEditable(false);
        txtMaTK.setPreferredSize(new Dimension(200, 30));
        rightPanel.add(txtMaTK, gbc);

        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 1;
        rightPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtTenDangNhap = new JTextField(isEditMode ? account.getTenDangNhap() : "");
        txtTenDangNhap.setPreferredSize(new Dimension(200, 30));
        rightPanel.add(txtTenDangNhap, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        txtMatKhau = new JTextField(isEditMode ? account.getMatKhau() : "");
        

        txtMaTK.setToolTipText("Nhập mã tài khoản và nhấn Enter để tiếp tục");
        txtMaTK.addActionListener(e -> txtTenDangNhap.requestFocus());

        txtTenDangNhap.setToolTipText("Nhập tên đăng nhập và nhấn Enter để tiếp tục");
        txtTenDangNhap.addActionListener(e -> txtMatKhau.requestFocus());

        txtMatKhau.setToolTipText("Nhập mật khẩu và nhấn Enter để kết thúc");
txtMatKhau.setPreferredSize(new Dimension(200, 30));
        rightPanel.add(txtMatKhau, gbc);

        // Nhân viên (Combobox)
        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1;
        cbNhanVien = new JComboBox<>();
        cbNhanVien.setPreferredSize(new Dimension(200, 30));
        if (isEditMode) {
            cbNhanVien.addItem(account.getIdNhanVien() + " - " + accountBUS.getEmployeeName(account.getIdNhanVien()));
            cbNhanVien.setEnabled(false);
        } else {
            accountBUS.getAvailableEmployees().forEach(cbNhanVien::addItem);
            cbNhanVien.addActionListener(e -> {
                String selected = (String) cbNhanVien.getSelectedItem();
                if (selected != null) {
                    selectedNhanVien = selected.split(" - ")[0];
                }
            });
            if (cbNhanVien.getItemCount() > 0) {
                cbNhanVien.setSelectedIndex(0);
            }
        }
        rightPanel.add(cbNhanVien, gbc);

        // Nhóm quyền (Combobox nhỏ hơn)
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(new JLabel("Nhóm quyền:"), gbc);
        gbc.gridx = 1;
        cbNhomQuyen = new JComboBox<>();
        cbNhomQuyen.setPreferredSize(new Dimension(150, 25));
        accountBUS.getPermissionGroups().forEach(cbNhomQuyen::addItem);
        if (isEditMode) {
            cbNhomQuyen.setSelectedItem(account.getIdNhomQuyen() + " - " + accountBUS.getPermissionGroupName(account.getIdNhomQuyen()));
        }
        cbNhomQuyen.addActionListener(e -> {
            String selected = (String) cbNhomQuyen.getSelectedItem();
            if (selected != null) {
                selectedNhomQuyen = selected.split(" - ")[0];
            }
        });
        if (cbNhomQuyen.getItemCount() > 0) {
            cbNhomQuyen.setSelectedIndex(0);
        }
        rightPanel.add(cbNhomQuyen, gbc);

        mainPanel.add(rightPanel);

        // Nút Hoàn tất và Hủy
        Button buttonFactory = new Button();
        JButton btnHoanTat = buttonFactory.createStyledButton("Hoàn tất", "./resources/icon/complete.png");
        JButton btnHuy = buttonFactory.createStyledButton("Hủy", "./resources/icon/exit.png");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnHoanTat);
        buttonPanel.add(btnHuy);

        btnHoanTat.addActionListener(e -> {
            if (selectedNhanVien == null || selectedNhomQuyen == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên và nhóm quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtTenDangNhap.getText().isEmpty() || txtMatKhau.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maTK = txtMaTK.getText();
            String tenDangNhap = txtTenDangNhap.getText();
            String matKhau = txtMatKhau.getText();
            if (accountBUS.saveAccount(maTK, selectedNhanVien, anhDaiDien, selectedNhomQuyen, tenDangNhap, matKhau, null, isEditMode)) {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Sửa" : "Thêm") + " tài khoản thành công!");
                parentPanel.loadData();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Sửa" : "Thêm") + " tài khoản thất bại!");
            }
        });

        btnHuy.addActionListener(e -> dispose());

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage img = ImageIO.read(file);
                Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblAnhDaiDien.setIcon(new ImageIcon(scaledImg));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "png", baos);
                anhDaiDien = baos.toByteArray();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải ảnh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}