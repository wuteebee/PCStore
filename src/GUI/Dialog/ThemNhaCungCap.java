package GUI.Dialog;

import java.awt.*;
import javax.swing.*;
import BUS.SupplierBUS;
import DTO.Supplier;
import GUI.Panel.SupplierPanel;

public class ThemNhaCungCap {
    private JTextField txtName, txtEmail, txtPhone, txtAddress;
    private SupplierBUS supplierBUS;
    private SupplierPanel supplierPanel;

    public ThemNhaCungCap(SupplierBUS bus, SupplierPanel supplierPanel) {
        this.supplierBUS = bus;
        this.supplierPanel = supplierPanel;
    }

    public void FormThemNCC(String title, String buttonText, Supplier supplier) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(500, 400);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        String[] labels = {"Tên nhà cung cấp", "Số điện thoại", "Email", "Địa chỉ"};

        txtName = new JTextField(20);
        txtName.setToolTipText("Nhập tên và nhấn Enter để tiếp tục");
        txtPhone = new JTextField(20);
        txtPhone.setToolTipText("Nhập số điện thoại và nhấn Enter để tiếp tục");
        txtEmail = new JTextField(20);
        txtEmail.setToolTipText("Nhập email và nhấn Enter để tiếp tục");
        txtAddress = new JTextField(20);
        txtAddress.setToolTipText("Nhập địa chỉ và nhấn Enter để hoàn tất");

        JComponent[] components = {txtName, txtPhone, txtEmail, txtAddress};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.LINE_END;
            dialog.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            dialog.add(components[i], gbc);
        }

        if (supplier != null) {
            txtName.setText(supplier.getName());
            txtPhone.setText(supplier.getPhoneNumber());
            txtEmail.setText(supplier.getEmail());
            txtAddress.setText(supplier.getAddress());
        }


        JButton btnSubmit = new JButton(buttonText);
        btnSubmit.setBackground(new Color(100, 149, 237));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridy++;

        gbc.gridwidth = 2;
        dialog.add(btnSubmit, gbc);

        btnSubmit.addActionListener(e -> {
            String name = txtName.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            String address = txtAddress.getText();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = supplierBUS.saveSupplier(name, email, phone, address, null, supplier != null, supplier != null ? supplier.getId() : null);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                supplierPanel.loadSupplierTable();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi lưu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        // Focus chuột vào ô đầu tiên khi mở form
        SwingUtilities.invokeLater(() -> txtName.requestFocusInWindow());

        // Tự động chuyển focus khi nhấn Enter
        txtName.addActionListener(e -> txtPhone.requestFocusInWindow());
        txtPhone.addActionListener(e -> txtEmail.requestFocusInWindow());
        txtEmail.addActionListener(e -> txtAddress.requestFocusInWindow());

        dialog.setVisible(true);
    }

}
