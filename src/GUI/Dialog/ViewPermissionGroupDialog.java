package GUI.Dialog;

import BUS.PermissionBUS;
import DTO.PermissionGroup;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ViewPermissionGroupDialog extends JDialog {
    private JLabel lblIdNhomQuyen, lblTenNhomQuyen;
    private JTextField txtIdNhomQuyen, txtTenNhomQuyen;
    private JPanel jpTop, jpLeft, jpCen, jpBottom;
    private JCheckBox[][] listCheckBox;
    private ButtonCustom btnClose;
    private int sizeDmCn, sizeHanhDong;
    private PermissionBUS permissionBUS;
    private PermissionGroup group;

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

    public ViewPermissionGroupDialog(JFrame owner, PermissionGroup group) {
        super(owner, "Chi tiết Nhóm Quyền", true);
        this.permissionBUS = new PermissionBUS();
        this.group = group;
        initComponents();
        loadGroupData();
    }

    private void initComponents() {
        this.setSize(new Dimension(950, 500)); // Tăng chiều rộng để chứa cột mới
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));

        // Panel trên
        jpTop = new JPanel(new GridLayout(2, 2, 20, 10));
        jpTop.setBorder(new EmptyBorder(20, 20, 20, 20));
        jpTop.setBackground(Color.WHITE);
        lblIdNhomQuyen = new JLabel("Mã nhóm quyền");
        txtIdNhomQuyen = new JTextField();
        txtIdNhomQuyen.setPreferredSize(new Dimension(150, 35));
        txtIdNhomQuyen.setEditable(false);
        lblTenNhomQuyen = new JLabel("Tên nhóm quyền");
        txtTenNhomQuyen = new JTextField();
        txtTenNhomQuyen.setPreferredSize(new Dimension(150, 35));
        txtTenNhomQuyen.setEditable(false);
        jpTop.add(lblIdNhomQuyen);
        jpTop.add(txtIdNhomQuyen);
        jpTop.add(lblTenNhomQuyen);
        jpTop.add(txtTenNhomQuyen);

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
                listCheckBox[i][j].setEnabled(false); // Chỉ đọc
                jpCen.add(listCheckBox[i][j]);
            }
        }

        // Panel dưới
        jpBottom = new JPanel(new FlowLayout());
        jpBottom.setBackground(Color.WHITE);
        jpBottom.setBorder(new EmptyBorder(20, 0, 20, 0));

        btnClose = new ButtonCustom("Đóng", "danger", 14);
        btnClose.addActionListener(e -> dispose());
        jpBottom.add(btnClose);

        // Thêm các panel vào dialog
        this.add(jpTop, BorderLayout.NORTH);
        this.add(jpLeft, BorderLayout.WEST);
        this.add(jpCen, BorderLayout.CENTER);
        this.add(jpBottom, BorderLayout.SOUTH);
    }

    private void loadGroupData() {
        txtIdNhomQuyen.setText(group.getIdNhomQuyen());
        txtTenNhomQuyen.setText(group.getTenNhomQuyen());
        for (int i = 0; i < sizeDmCn; i++) {
            String chucNang = danhMucChucNang[i];
            String idChucNang = permissionBUS.getChucNangIdByName(chucNang);
            for (int j = 0; j < sizeHanhDong; j++) {
                String hanhDong = this.hanhDong[j];
                boolean hasPermission = permissionBUS.hasPermission(group.getIdNhomQuyen(), idChucNang, hanhDong);
                listCheckBox[i][j].setSelected(hasPermission);
            }
        }
    }

    public static void showViewPermissionGroupDialog(JFrame parent, PermissionGroup group) {
        ViewPermissionGroupDialog dialog = new ViewPermissionGroupDialog(parent, group);
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