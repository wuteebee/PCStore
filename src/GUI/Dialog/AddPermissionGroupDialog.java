package GUI.Dialog;

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

    private String[] hanhDong = {"Xem", "Tạo mới", "Cập nhật", "Xoá"};

    public AddPermissionGroupDialog(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();
    }

    private void initComponents() {
        this.setSize(new Dimension(900, 500));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));

        // Top Panel
        jpTop = new JPanel(new BorderLayout(20, 10));
        jpTop.setBorder(new EmptyBorder(20, 20, 20, 20));
        jpTop.setBackground(Color.WHITE);
        lblTenNhomQuyen = new JLabel("Tên nhóm quyền");
        txtTenNhomQuyen = new JTextField();
        txtTenNhomQuyen.setPreferredSize(new Dimension(150, 35));
        jpTop.add(lblTenNhomQuyen, BorderLayout.WEST);
        jpTop.add(txtTenNhomQuyen, BorderLayout.CENTER);

        // Left Panel
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

        // Center Panel
        sizeHanhDong = hanhDong.length;
        jpCen = new JPanel(new GridLayout(sizeDmCn + 1, sizeHanhDong));
        jpCen.setBackground(Color.WHITE);
        listCheckBox = new JCheckBox[sizeDmCn][sizeHanhDong];

        // Add action headers
        for (String hd : hanhDong) {
            JLabel lblHanhDong = new JLabel(hd);
            lblHanhDong.setHorizontalAlignment(SwingConstants.CENTER);
            lblHanhDong.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
            jpCen.add(lblHanhDong);
        }

        // Add checkboxes
        for (int i = 0; i < sizeDmCn; i++) {
            for (int j = 0; j < sizeHanhDong; j++) {
                listCheckBox[i][j] = new JCheckBox();
                listCheckBox[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                jpCen.add(listCheckBox[i][j]);
            }
        }

        // Bottom Panel
        jpBottom = new JPanel(new FlowLayout());
        jpBottom.setBackground(Color.WHITE);
        jpBottom.setBorder(new EmptyBorder(20, 0, 20, 0));

        btnAddNhomQuyen = new ButtonCustom("Thêm nhóm quyền", "success", 14);
        btnAddNhomQuyen.addActionListener(this);
        jpBottom.add(btnAddNhomQuyen);

        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnHuyBo.addActionListener(this);
        jpBottom.add(btnHuyBo);

        // Add panels to dialog
        this.add(jpTop, BorderLayout.NORTH);
        this.add(jpLeft, BorderLayout.WEST);
        this.add(jpCen, BorderLayout.CENTER);
        this.add(jpBottom, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddNhomQuyen || e.getSource() == btnHuyBo) {
            dispose();
        }
    }

    public static void showAddPermissionGroupDialog(JFrame parent) {
        AddPermissionGroupDialog dialog = new AddPermissionGroupDialog(parent, "Thêm Nhóm Quyền", true);
        dialog.setVisible(true);
    }

    // Class con giả lập ButtonCustom
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

    // ✅ Hàm main để chạy trực tiếp class này
    public static void main(String[] args) {
        // Thiết lập giao diện FlatLaf nếu cần
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame dummyFrame = new JFrame(); // khung ẩn để làm cha của dialog
            dummyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummyFrame.setSize(0, 0); // không hiển thị
            dummyFrame.setLocationRelativeTo(null);
            dummyFrame.setVisible(false); // không hiển thị frame cha

            showAddPermissionGroupDialog(dummyFrame);
        });
    }
}
