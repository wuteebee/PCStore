package GUI.Dialog;

import BUS.BrandBUS;
import DTO.Brand;
import GUI.Panel.BrandPanel;

import javax.swing.*;
import java.awt.*;

public class ThemThuongHieu extends JDialog {
    private JTextField txtId, txtTen, txtDanhMuc;
    private JCheckBox chkTrangThai;
    private final BrandPanel parent;
    private final boolean isEdit;

    public ThemThuongHieu(Frame owner, BrandPanel parent, boolean isEdit, Brand brand) {
        super(owner, isEdit ? "Cập nhật thương hiệu" : "Thêm thương hiệu", true);
        this.parent = parent;
        this.isEdit = isEdit;
        initUI(brand);
    }

    private void initUI(Brand brand) {
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        String[] labels = {"Mã thương hiệu", "Tên thương hiệu", "Mã danh mục", "Trạng thái"};

        txtId = new JTextField(20);
        txtTen = new JTextField(20);
        txtDanhMuc = new JTextField(20);
        chkTrangThai = new JCheckBox("Hoạt động");
        chkTrangThai.setBackground(Color.WHITE);
        chkTrangThai.setSelected(true);

        JComponent[] components = {txtId, txtTen, txtDanhMuc, chkTrangThai};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.LINE_END;
            add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            add(components[i], gbc);
        }

        if (isEdit && brand != null) {
            txtId.setText(brand.getMaThuongHieu());
            txtId.setEditable(false);
            txtTen.setText(brand.getTenThuongHieu());
            txtDanhMuc.setText(brand.getmaDanhMuc());
            chkTrangThai.setSelected(brand.isTrangThai());
        }

        JButton btnSave = new JButton(isEdit ? "Cập nhật" : "Thêm");
        btnSave.setBackground(new Color(100, 149, 237));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnSave, gbc);

        btnSave.addActionListener(e -> saveBrand());
    }

    private void saveBrand() {
        String id = txtId.getText().trim();
        String ten = txtTen.getText().trim();
        String dm = txtDanhMuc.getText().trim();
        boolean tt = chkTrangThai.isSelected();

        if (id.isEmpty() || ten.isEmpty() || dm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Brand brand = new Brand(id, ten, dm, tt);
        boolean success = isEdit ? new BrandBUS().updateBrand(brand) : new BrandBUS().addBrand(brand);

        if (success) {
            JOptionPane.showMessageDialog(this, isEdit ? "Cập nhật thành công." : "Thêm thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            parent.loadBrandTable();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thao tác thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}