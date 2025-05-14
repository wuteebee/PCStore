package GUI.Dialog;

import BUS.PermissionBUS;
import DTO.PermissionGroup;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddPermissionGroupDialog extends JDialog implements ActionListener {
    private JLabel lblTenNhomQuyen;
    private JTextField txtTenNhomQuyen;
    private JPanel jpTop, jpLeft, jpCen, jpBottom;
    private JCheckBox[][] listCheckBox;
    private ButtonCustom btnAddNhomQuyen, btnHuyBo;
    private int sizeDmCn, sizeHanhDong;
    private PermissionBUS permissionBUS;
    private PermissionGroup existingGroup; // Dùng khi sửa
    private boolean isEditMode;

    private String[] danhMucChucNang = {
        "Sản phẩm",
        "Thuộc tính",
        "Phiếu nhập",
        "Phiếu xuất",
        "Khách hàng",
        "Nhà cung cấp",
        "Nhân viên",
        "Tài khoản",
        "Thống kê",
        "Phân quyền",
        "Khuyến mãi và ưu đãi"
    };

    private String[] hanhDong = {"Xem", "Tao", "Sua", "Xoa", "Xuat"};

    public AddPermissionGroupDialog(JFrame owner, String title, boolean modal, PermissionGroup group) {
        super(owner, title, modal);
        this.permissionBUS = new PermissionBUS();
        this.existingGroup = group;
        this.isEditMode = group != null;
        initComponents();
        if (isEditMode) {
            loadExistingGroupData();
        }
    }

    private void initComponents() {
        this.setSize(new Dimension(950, 500)); // Tăng chiều rộng để chứa cột mới
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));

        // Panel trên
        jpTop = new JPanel(new BorderLayout(20, 10));
        jpTop.setBorder(new EmptyBorder(20, 20, 20, 20));
        jpTop.setBackground(Color.WHITE);
        lblTenNhomQuyen = new JLabel("Tên nhóm quyền");
        txtTenNhomQuyen = new JTextField();
        txtTenNhomQuyen.setPreferredSize(new Dimension(150, 35));
        jpTop.add(lblTenNhomQuyen, BorderLayout.WEST);
        jpTop.add(txtTenNhomQuyen, BorderLayout.CENTER);

        // Panel trái
        sizeDmCn = danhMucChucNang.length;
        jpLeft = new JPanel(new GridLayout(sizeDmCn + 1, 1));
        jpLeft.setBackground(Color.WHITE);
        jpLeft.setBorder(new EmptyBorder(0, 20, 0, 14));
        JLabel dmcnLabel = new JLabel("Danh mục chức năng");
        dmcnLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
        jpLeft.add(dmcnLabel);
        for (String chucNang : danhMucChucNang) {
            JLabel lblChucNang = new JLabel(chucNang);
            jpLeft.add(lblChucNang);
        }

        // Panel giữa
        sizeHanhDong = hanhDong.length;
        jpCen = new JPanel(new GridLayout(sizeDmCn + 1, sizeHanhDong));
        jpCen.setBackground(Color.WHITE);
        listCheckBox = new JCheckBox[sizeDmCn][sizeHanhDong];

        // Thêm tiêu đề hành động
        for (String hd : hanhDong) {
            JLabel lblHanhDong = new JLabel(hd.equals("Xuat") ? "Xuất Excel" : hd);
            lblHanhDong.setHorizontalAlignment(SwingConstants.CENTER);
            lblHanhDong.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
            jpCen.add(lblHanhDong);
        }

        // Thêm checkbox
        for (int i = 0; i < sizeDmCn; i++) {
            for (int j = 0; j < sizeHanhDong; j++) {
                listCheckBox[i][j] = new JCheckBox();
                listCheckBox[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                jpCen.add(listCheckBox[i][j]);
            }
        }

        // Panel dưới
        jpBottom = new JPanel(new FlowLayout());
        jpBottom.setBackground(Color.WHITE);
        jpBottom.setBorder(new EmptyBorder(20, 0, 20, 0));

        btnAddNhomQuyen = new ButtonCustom(isEditMode ? "Cập nhật" : "Thêm nhóm quyền", "success", 14);
        btnAddNhomQuyen.addActionListener(this);
        jpBottom.add(btnAddNhomQuyen);

        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnHuyBo.addActionListener(this);
        jpBottom.add(btnHuyBo);

        // Thêm các panel vào dialog
        this.add(jpTop, BorderLayout.NORTH);
        this.add(jpLeft, BorderLayout.WEST);
        this.add(jpCen, BorderLayout.CENTER);
        this.add(jpBottom, BorderLayout.SOUTH);
    }

    private void loadExistingGroupData() {
        txtTenNhomQuyen.setText(existingGroup.getTenNhomQuyen());
        for (int i = 0; i < sizeDmCn; i++) {
            String chucNang = danhMucChucNang[i];
            String idChucNang = permissionBUS.getChucNangIdByName(chucNang);
            for (int j = 0; j < sizeHanhDong; j++) {
                String hanhDong = this.hanhDong[j];
                boolean hasPermission = permissionBUS.hasPermission(existingGroup.getIdNhomQuyen(), idChucNang, hanhDong);
                listCheckBox[i][j].setSelected(hasPermission);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddNhomQuyen) {
            String tenNhomQuyen = txtTenNhomQuyen.getText().trim();
            if (tenNhomQuyen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhóm quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PermissionGroup group = new PermissionGroup();
            group.setIdNhomQuyen(isEditMode ? existingGroup.getIdNhomQuyen() : permissionBUS.generateSequentialId());
            group.setTenNhomQuyen(tenNhomQuyen);
            group.setTrangThai(1);

            // Thu thập quyền
            for (int i = 0; i < sizeDmCn; i++) {
                String chucNang = danhMucChucNang[i];
                String idChucNang = permissionBUS.getChucNangIdByName(chucNang);
                for (int j = 0; j < sizeHanhDong; j++) {
                    if (listCheckBox[i][j].isSelected()) {
                        group.addPermission(idChucNang, hanhDong[j]);
                    }
                }
            }

            boolean success;
            if (isEditMode) {
                success = permissionBUS.updatePermissionGroup(group);
            } else {
                success = permissionBUS.savePermissionGroup(group);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Cập nhật" : "Thêm") + " nhóm quyền thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Cập nhật" : "Thêm") + " nhóm quyền thất bại!");
            }
        } else if (e.getSource() == btnHuyBo) {
            dispose();
        }
    }

    public static void showAddPermissionGroupDialog(JFrame parent, PermissionGroup group) {
        AddPermissionGroupDialog dialog = new AddPermissionGroupDialog(parent, group == null ? "Thêm Nhóm Quyền" : "Sửa Nhóm Quyền", true, group);
        dialog.setVisible(true);
    }

    private class ButtonCustom extends JButton {
        public ButtonCustom(String text, String type, int fontSize) {
            super(text);
            setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, fontSize));
            if ("success".equals(type)) {
                setBackground(new Color(46, 204, 113));
            } else if ("danger".equals(type)) {
                setBackground(new Color(231, 76, 60));
            }
            setForeground(Color.WHITE);
            setFocusPainted(false);
        }
    }
}