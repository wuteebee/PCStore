package GUI.Dialog;

import BUS.PromotionBUS;
import DTO.ComboProduct;
import DTO.Promotion;
import GUI.Components.RoundedButton;
import GUI.Panel.PromotionPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ThemKhuyenMaiDialog extends JDialog {
    private JTextField txtMaKM, txtTenKM, txtPhanTramKM, txtNgayBD, txtNgayKT;
    private JComboBox<String> cbLoai;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private PromotionBUS promotionBUS;
    private boolean isEditMode;
    private boolean isComboMode;
    private List<ComboProduct> selectedProducts = new ArrayList<>();
    private String originalLoai; // Lưu loại khuyến mãi ban đầu

    public ThemKhuyenMaiDialog(Frame parent, Promotion promotion, boolean isComboMode) {
        super(parent, promotion == null ? "Thêm khuyến mãi" : "Sửa khuyến mãi", true);
        this.promotionBUS = new PromotionBUS();
        this.isEditMode = promotion != null;
        this.isComboMode = isComboMode;
        this.originalLoai = isEditMode ? promotion.getLoai() : (isComboMode ? "Combo" : "Đơn lẻ");
        initComponents(promotion);
        setSize(600, isComboMode || (isEditMode && "Combo".equals(originalLoai)) ? 700 : 400);
        setLocationRelativeTo(parent);
    }

    public ThemKhuyenMaiDialog(Frame parent, Promotion promotion) {
        this(parent, promotion, false);
    }

    private void initComponents(Promotion promotion) {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        txtMaKM = new JTextField(isEditMode ? promotion.getIdKhuyenMai() : promotionBUS.generateUniqueId());
        txtMaKM.setEditable(!isEditMode);
        txtTenKM = new JTextField(isEditMode ? promotion.getTenKhuyenMai() : "");
        txtPhanTramKM = new JTextField(isEditMode ? String.valueOf(promotion.getGiaTri()) : "");
        txtNgayBD = new JTextField(isEditMode && promotion.getNgayBatDau() != null ?
                promotion.getNgayBatDau().toString() : "");
        txtNgayKT = new JTextField(isEditMode && promotion.getNgayKetThuc() != null ?
                promotion.getNgayKetThuc().toString() : "");
        cbLoai = new JComboBox<>(new String[]{"Đơn lẻ", "Combo"});
        cbLoai.setSelectedItem(originalLoai);
        cbLoai.setEnabled(false); // Không cho phép thay đổi loại

        inputPanel.add(new JLabel("Mã KM:"));
        inputPanel.add(txtMaKM);
        inputPanel.add(new JLabel("Tên KM:"));
        inputPanel.add(txtTenKM);
        inputPanel.add(new JLabel("Phần trăm KM:"));
        inputPanel.add(txtPhanTramKM);
        inputPanel.add(new JLabel("Ngày BĐ:"));
        inputPanel.add(txtNgayBD);
        inputPanel.add(new JLabel("Ngày KT:"));
        inputPanel.add(txtNgayKT);
        inputPanel.add(new JLabel("Loại:"));
        inputPanel.add(cbLoai);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        if (isComboMode || (isEditMode && "Combo".equals(originalLoai))) {
            String[] columns = {"Mã sản phẩm", "Tên sản phẩm"};
            productTableModel = new DefaultTableModel(columns, 0);
            productTable = new JTable(productTableModel);
            productTable.setEnabled(isComboMode); // Chỉ cho phép chỉnh sửa sản phẩm ở chế độ combo
            JScrollPane scrollPane = new JScrollPane(productTable);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            if (isEditMode) {
                selectedProducts = promotionBUS.getComboProducts(promotion.getIdKhuyenMai());
                for (ComboProduct p : selectedProducts) {
                    productTableModel.addRow(new Object[]{p.getIdSanPham(), p.getTenSanPham()});
                }
            }

            RoundedButton btnChonSanPham = new RoundedButton("Chọn sản phẩm", 20, null);
            btnChonSanPham.setVisible(isComboMode); // Chỉ hiển thị nút chọn sản phẩm khi tạo combo mới
            btnChonSanPham.addActionListener(e -> {
                JDialog selectDialog = new JDialog(this, "Chọn sản phẩm", true);
                selectDialog.setSize(500, 500);
                selectDialog.setLayout(new BorderLayout());

                DefaultTableModel model = new DefaultTableModel(columns, 0);
                for (ComboProduct p : promotionBUS.getAllProducts()) {
                    model.addRow(new Object[]{p.getIdSanPham(), p.getTenSanPham()});
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
            mainPanel.add(btnChonSanPham, BorderLayout.SOUTH);
        }

        RoundedButton btnHoanTat = new RoundedButton("Hoàn tất", 20, new ImageIcon("src/resources/icon/complete.png"));
        RoundedButton btnHuy = new RoundedButton("Hủy", 20, new ImageIcon("src/resources/icon/exit.png"));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnHoanTat);
        buttonPanel.add(btnHuy);

        btnHoanTat.addActionListener(e -> {
            String maKM = txtMaKM.getText();
            String tenKM = txtTenKM.getText();
            String phanTramKM = txtPhanTramKM.getText();
            String ngayBD = txtNgayBD.getText();
            String ngayKT = txtNgayKT.getText();
            String loai = originalLoai; // Sử dụng loại ban đầu thay vì cbLoai.getSelectedItem()

            if (maKM.isEmpty() || tenKM.isEmpty() || phanTramKM.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Nếu sửa combo ở chế độ không phải isComboMode, giữ nguyên danh sách sản phẩm
            List<ComboProduct> productsToSave = selectedProducts;
            if (isEditMode && "Combo".equals(loai) && !isComboMode) {
                productsToSave = promotionBUS.getComboProducts(maKM); // Lấy danh sách sản phẩm hiện tại
                if (productsToSave.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Combo phải có ít nhất một sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if ("Combo".equals(loai) && productsToSave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một sản phẩm cho combo!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (promotionBUS.savePromotion(maKM, tenKM, phanTramKM, ngayBD, ngayKT, loai, isEditMode, productsToSave)) {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Sửa" : "Thêm") + " khuyến mãi thành công!");
                dispose();
                // Cập nhật bảng trong PromotionPanel
                Container parent = getParent();
                while (parent != null && !(parent instanceof PromotionPanel)) {
                    parent = parent.getParent();
                }
                if (parent instanceof PromotionPanel) {
                    ((PromotionPanel) parent).loadData();
                }
            } else {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Sửa" : "Thêm") + " khuyến mãi thất bại!");
            }
        });

        btnHuy.addActionListener(e -> dispose());

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}