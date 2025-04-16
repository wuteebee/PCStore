package GUI.Dialog;

import BUS.PromotionBUS;
import DTO.ComboProduct;
import DTO.Promotion;
import GUI.Components.RoundedButton;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class QuanLyComboDialog extends JDialog {
    private JTextField txtMaKM, txtTenKM, txtGiaTri, txtNgayBD, txtNgayKT;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private PromotionBUS promotionBUS;
    private String idKhuyenMai;
    private List<ComboProduct> selectedProducts = new ArrayList<>();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private RoundedButton btnSuaCombo;
    private RoundedButton btnChonSanPham;

    public QuanLyComboDialog(Frame parent, String idKhuyenMai) {
        super(parent, "Danh sách Combo", true);
        this.promotionBUS = new PromotionBUS();
        this.idKhuyenMai = idKhuyenMai;
        initComponents();
        setSize(593, 666);
        setLocationRelativeTo(parent);
        loadData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Phần trên: Mã KM
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        JLabel lblMaKM = new JLabel("Mã KM:");
        lblMaKM.setPreferredSize(new Dimension(50, 41));
        txtMaKM = new JTextField();
        txtMaKM.setPreferredSize(new Dimension(510, 41));
        txtMaKM.setEditable(false);
        topPanel.add(lblMaKM, BorderLayout.WEST);
        topPanel.add(txtMaKM, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Phần dưới: Chia đôi
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));

        // Bên trái: Thông tin KM và bảng sản phẩm
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel leftFieldsPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        txtTenKM = new JTextField();
        txtTenKM.setPreferredSize(new Dimension(206, 41));
        txtTenKM.setBackground(Color.LIGHT_GRAY);
        txtTenKM.setFont(new Font("Arial", Font.PLAIN, 13));
        txtTenKM.setBorder(BorderFactory.createTitledBorder("Tên KM"));
        txtTenKM.setEditable(false);

        txtGiaTri = new JTextField();
        txtGiaTri.setPreferredSize(new Dimension(206, 41));
        txtGiaTri.setBackground(Color.LIGHT_GRAY);
        txtGiaTri.setFont(new Font("Arial", Font.PLAIN, 13));
        txtGiaTri.setBorder(BorderFactory.createTitledBorder("Giá trị KM"));
        txtGiaTri.setEditable(false);

        txtMaKM = new JTextField(idKhuyenMai);
        txtMaKM.setPreferredSize(new Dimension(206, 41));
        txtMaKM.setBackground(Color.LIGHT_GRAY);
        txtMaKM.setFont(new Font("Arial", Font.PLAIN, 13));
        txtMaKM.setBorder(BorderFactory.createTitledBorder("Mã KM"));
        txtMaKM.setEditable(false);

        leftFieldsPanel.add(txtTenKM);
        leftFieldsPanel.add(txtMaKM);
        leftFieldsPanel.add(txtGiaTri);

        String[] columns = {"Mã sản phẩm", "Tên sản phẩm"};
        productTableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productTableModel);
        productTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane productScrollPane = new JScrollPane(productTable);

        btnChonSanPham = new RoundedButton("Chọn sản phẩm", 20, null);
        btnChonSanPham.setVisible(false);
        leftPanel.add(leftFieldsPanel, BorderLayout.NORTH);
        leftPanel.add(productScrollPane, BorderLayout.CENTER);
        leftPanel.add(btnChonSanPham, BorderLayout.SOUTH);

        // Bên phải: Ngày BĐ, Ngày KT và nút Sửa combo
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel rightFieldsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        txtNgayBD = new JTextField();
        txtNgayBD.setPreferredSize(new Dimension(206, 41));
        txtNgayBD.setBackground(Color.LIGHT_GRAY);
        txtNgayBD.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNgayBD.setBorder(BorderFactory.createTitledBorder("Ngày BĐ"));
        txtNgayBD.setEditable(false);

        txtNgayKT = new JTextField();
        txtNgayKT.setPreferredSize(new Dimension(206, 41));
        txtNgayKT.setBackground(Color.LIGHT_GRAY);
        txtNgayKT.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNgayKT.setBorder(BorderFactory.createTitledBorder("Ngày KT"));
        txtNgayKT.setEditable(false);

        rightFieldsPanel.add(txtNgayBD);
        rightFieldsPanel.add(txtNgayKT);

        btnSuaCombo = new RoundedButton("Sửa combo", 20, new ImageIcon("src/resources/icon/edit.png"));
        btnSuaCombo.setBackground(new Color(0, 255, 255));
        btnSuaCombo.setForeground(Color.BLACK);
        btnSuaCombo.setPreferredSize(new Dimension(187, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSuaCombo);

        rightPanel.add(rightFieldsPanel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        bottomPanel.add(leftPanel);
        bottomPanel.add(rightPanel);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Sự kiện nút Sửa combo
        btnSuaCombo.addActionListener(e -> {
            if ("Sửa combo".equals(btnSuaCombo.getText())) {
                btnSuaCombo.setText("Hoàn tất");
                txtTenKM.setEditable(true);
                txtGiaTri.setEditable(true);
                txtNgayBD.setEditable(true);
                txtNgayKT.setEditable(true);
                btnChonSanPham.setVisible(true);
            } else {
                String maKM = txtMaKM.getText();
                String tenKM = txtTenKM.getText();
                String giaTri = txtGiaTri.getText();
                String ngayBD = txtNgayBD.getText();
                String ngayKT = txtNgayKT.getText();

                if (tenKM.isEmpty() || giaTri.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }
                if (selectedProducts.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một sản phẩm!");
                    return;
                }

                if (promotionBUS.savePromotion(maKM, tenKM, giaTri, ngayBD, ngayKT, "Combo", true, selectedProducts)) {
                    JOptionPane.showMessageDialog(this, "Sửa combo thành công!");
                    btnSuaCombo.setText("Sửa combo");
                    txtTenKM.setEditable(false);
                    txtGiaTri.setEditable(false);
                    txtNgayBD.setEditable(false);
                    txtNgayKT.setEditable(false);
                    btnChonSanPham.setVisible(false);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa combo thất bại!");
                }
            }
        });

        // Sự kiện nút Chọn sản phẩm
        btnChonSanPham.addActionListener(e -> {
            JDialog selectDialog = new JDialog(this, "Chọn sản phẩm", true);
            selectDialog.setSize(500, 500);
            selectDialog.setLayout(new BorderLayout());

            String[] columnsSelect = {"Mã sản phẩm", "Tên sản phẩm"};
            DefaultTableModel model = new DefaultTableModel(columnsSelect, 0);
            for (ComboProduct product : promotionBUS.getAllProducts()) {
                model.addRow(new Object[]{product.getIdSanPham(), product.getTenSanPham()});
            }
            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            RoundedButton btnHoanTat = new RoundedButton("Hoàn tất", 20, new ImageIcon("src/resources/icon/complete.png"));
            btnHoanTat.addActionListener(e1 -> {
                productTableModel.setRowCount(0);
                selectedProducts.clear();
                int[] rows = table.getSelectedRows();
                for (int row : rows) {
                    String id = (String) model.getValueAt(row, 0);
                    String ten = (String) model.getValueAt(row, 1);
                    selectedProducts.add(new ComboProduct(txtMaKM.getText(), id, ten));
                    productTableModel.addRow(new Object[]{id, ten});
                }
                selectDialog.dispose();
            });

            selectDialog.add(new JScrollPane(table), BorderLayout.CENTER);
            selectDialog.add(btnHoanTat, BorderLayout.SOUTH);
            selectDialog.setLocationRelativeTo(this);
            selectDialog.setVisible(true);
        });
    }

    private void loadData() {
        Promotion p = promotionBUS.getAllPromotions().stream()
                .filter(prom -> prom.getIdKhuyenMai().equals(idKhuyenMai)).findFirst().orElse(null);
        if (p != null) {
            txtMaKM.setText(p.getIdKhuyenMai());
            txtTenKM.setText(p.getTenKhuyenMai());
            txtGiaTri.setText(String.valueOf(p.getGiaTri()));
            txtNgayBD.setText(p.getNgayBatDau() != null ? p.getNgayBatDau().format(dateFormatter) : "");
            txtNgayKT.setText(p.getNgayKetThuc() != null ? p.getNgayKetThuc().format(dateFormatter) : "");
            selectedProducts = promotionBUS.getComboProducts(idKhuyenMai);
            productTableModel.setRowCount(0);
            for (ComboProduct cp : selectedProducts) {
                productTableModel.addRow(new Object[]{cp.getIdSanPham(), cp.getTenSanPham()});
            }
        }
    }
}