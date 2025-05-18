package GUI.Dialog;

import BUS.PromotionBUS;
import DTO.ComboProduct;
import DTO.Promotion;
import GUI.Components.Button;
import GUI.Panel.PromotionPanel;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ThemKhuyenMaiDialog extends JDialog {
    private JTextField txtMaKM, txtTenKM, txtPhanTramKM;
    private JDateChooser dateChooserNgayBD, dateChooserNgayKT;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JButton btnDonLe, btnCombo;
    private JPanel typePanel, contentPanel, productPanel;
    private PromotionBUS promotionBUS;
    private boolean isEditMode;
    private List<ComboProduct> selectedProducts = new ArrayList<>();
    private String loai;
    private PromotionPanel parentPanel;

    public ThemKhuyenMaiDialog(PromotionPanel parent, Promotion promotion) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), promotion == null ? "Thêm khuyến mãi" : "Sửa khuyến mãi", true);
        this.parentPanel = parent;
        this.promotionBUS = new PromotionBUS();
        this.isEditMode = promotion != null;
        this.loai = isEditMode ? promotion.getLoai() : "Đơn lẻ";
        this.selectedProducts = isEditMode && "Combo".equals(loai) ? promotionBUS.getComboProducts(promotion.getIdKhuyenMai()) : new ArrayList<>();
        initComponents(promotion);
        setSize(600, "Combo".equals(loai) ? 800 : 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents(Promotion promotion) {
        setLayout(new BorderLayout(10, 10));

        // Thanh header: Chọn loại (Đơn lẻ hoặc Combo)
        typePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        Button buttonFactory = new Button();
        btnDonLe = buttonFactory.createStyledButton("Đơn lẻ", null);
        btnCombo = buttonFactory.createStyledButton("Combo", null);
        
        btnDonLe.setPreferredSize(new Dimension(100, 30));
        btnCombo.setPreferredSize(new Dimension(100, 30));
        btnDonLe.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        typePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        if (isEditMode) {
            btnDonLe.setEnabled(false);
            btnCombo.setEnabled(false);
        } else {
            btnDonLe.setBackground(Color.LIGHT_GRAY);
            btnCombo.setBackground(Color.WHITE);
        }

        btnDonLe.addActionListener(e -> switchType("Đơn lẻ"));
        btnCombo.addActionListener(e -> switchType("Combo"));
        typePanel.add(btnDonLe);
        typePanel.add(btnCombo);

        // Nội dung chính
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        // Sinh mã KM tự động hoặc lấy mã cũ (khi chỉnh sửa)
        txtMaKM = new JTextField(isEditMode ? promotion.getIdKhuyenMai() : generatePromotionId(loai));
        txtMaKM.setEditable(false); // Không cho chỉnh sửa mã KM trong mọi trường hợp
        txtTenKM = new JTextField(isEditMode ? promotion.getTenKhuyenMai() : "");
        txtPhanTramKM = new JTextField(isEditMode ? String.valueOf(promotion.getGiaTri()) : "");
        

        txtMaKM.setToolTipText("Nhập mã khuyến mãi và nhấn Enter để tiếp tục");
        txtMaKM.addActionListener(e -> txtTenKM.requestFocus());

        txtTenKM.setToolTipText("Nhập tên khuyến mãi và nhấn Enter để tiếp tục");
        txtTenKM.addActionListener(e -> txtPhanTramKM.requestFocus());

        txtPhanTramKM.setToolTipText("Nhập phần trăm khuyến mãi và nhấn Enter để kết thúc");
dateChooserNgayBD = new JDateChooser();
        dateChooserNgayBD.setDateFormatString("yyyy-MM-dd");
        if (isEditMode && promotion.getNgayBatDau() != null) {
            dateChooserNgayBD.setDate(java.sql.Date.valueOf(promotion.getNgayBatDau()));
        }
        dateChooserNgayKT = new JDateChooser();
        dateChooserNgayKT.setDateFormatString("yyyy-MM-dd");
        if (isEditMode && promotion.getNgayKetThuc() != null) {
            dateChooserNgayKT.setDate(java.sql.Date.valueOf(promotion.getNgayKetThuc()));
        }

        inputPanel.add(new JLabel("Mã KM:"));
        inputPanel.add(txtMaKM);
        inputPanel.add(new JLabel("Tên KM:"));
        inputPanel.add(txtTenKM);
        inputPanel.add(new JLabel("Phần trăm KM:"));
        inputPanel.add(txtPhanTramKM);
        inputPanel.add(new JLabel("Ngày BĐ:"));
        inputPanel.add(dateChooserNgayBD);
        inputPanel.add(new JLabel("Ngày KT:"));
        inputPanel.add(dateChooserNgayKT);

        contentPanel.add(inputPanel, BorderLayout.NORTH);

        // Phần chọn sản phẩm (chỉ hiển thị khi là Combo)
        productPanel = new JPanel(new BorderLayout());
        String[] columns = {"Mã sản phẩm", "Tên sản phẩm"};
        productTableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productTableModel);
        productTable.setEnabled(!isEditMode);
        JScrollPane scrollPane = new JScrollPane(productTable);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        JButton btnChonSanPham = buttonFactory.createStyledButton("Chọn sản phẩm", null);
        btnChonSanPham.setPreferredSize(new Dimension(130, 30));
        btnChonSanPham.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Gói vào panel căn giữa
        JPanel bottomBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomBtnPanel.add(btnChonSanPham);
        productPanel.add(bottomBtnPanel, BorderLayout.SOUTH);

        if (!isEditMode || ("Combo".equals(loai) && isEditMode)) {
            btnChonSanPham.setVisible(true);
        } else {
            btnChonSanPham.setVisible(false);
        }
        
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
            for (int i = 0; i < model.getRowCount(); i++) {
                String id = (String) model.getValueAt(i, 0);
                boolean isSelected = selectedProducts.stream().anyMatch(p -> p.getIdSanPham().equals(id));
                if (isSelected) {
                    table.addRowSelectionInterval(i, i);
                }
            }

            JButton btnHoanTatChon = buttonFactory.createStyledButton("Hoàn tất", "./resources/icon/complete.png");
            btnHoanTatChon.addActionListener(e1 -> {
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
            selectDialog.add(btnHoanTatChon, BorderLayout.SOUTH);
            selectDialog.setLocationRelativeTo(this);
            selectDialog.setVisible(true);
        });
        productPanel.add(btnChonSanPham, BorderLayout.SOUTH);

        if (isEditMode && "Combo".equals(loai)) {
            for (ComboProduct p : selectedProducts) {
                productTableModel.addRow(new Object[]{p.getIdSanPham(), p.getTenSanPham()});
            }
            contentPanel.add(productPanel, BorderLayout.CENTER);
        }

        // Nút Hoàn tất và Hủy
        JButton btnHoanTat = buttonFactory.createStyledButton("Hoàn tất", "./resources/icon/complete.png");
        JButton btnHuy = buttonFactory.createStyledButton("Hủy", "./resources/icon/exit.png");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnHoanTat);
        buttonPanel.add(btnHuy);

        btnHoanTat.addActionListener(e -> {
            String maKMToSave = txtMaKM.getText(); // Lấy mã KM từ txtMaKM
            String tenKM = txtTenKM.getText();
            String phanTramKM = txtPhanTramKM.getText();
            String ngayBD = dateChooserNgayBD.getDate() != null ?
                    new SimpleDateFormat("yyyy-MM-dd").format(dateChooserNgayBD.getDate()) : "";
            String ngayKT = dateChooserNgayKT.getDate() != null ?
                    new SimpleDateFormat("yyyy-MM-dd").format(dateChooserNgayKT.getDate()) : "";

            // Kiểm tra thông tin bắt buộc
            if (maKMToSave.isEmpty() || tenKM.isEmpty() || phanTramKM.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Kiểm tra ngày
            if (!ngayBD.isEmpty() && !ngayKT.isEmpty()) {
                LocalDate bd = LocalDate.parse(ngayBD);
                LocalDate kt = LocalDate.parse(ngayKT);
                if (kt.isBefore(bd)) {
                    JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Kiểm tra sản phẩm combo
            List<ComboProduct> productsToSave = selectedProducts;
            if ("Combo".equals(loai) && productsToSave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một sản phẩm cho combo!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra mã KM có đúng định dạng theo loại không
            if ("Combo".equals(loai) && !maKMToSave.startsWith("KMCombo")) {
                JOptionPane.showMessageDialog(this, "Mã KM cho Combo phải bắt đầu bằng 'KMCombo'!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            } else if ("Đơn lẻ".equals(loai) && !maKMToSave.startsWith("KM")) {
                JOptionPane.showMessageDialog(this, "Mã KM cho Đơn lẻ phải bắt đầu bằng 'KM'!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lưu khuyến mãi
            if (promotionBUS.savePromotion(maKMToSave, tenKM, phanTramKM, ngayBD, ngayKT, loai, isEditMode, productsToSave)) {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Sửa" : "Thêm") + " khuyến mãi thành công!");
                parentPanel.loadData();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Sửa" : "Thêm") + " khuyến mãi thất bại!");
            }
        });

        btnHuy.addActionListener(e -> dispose());

        add(typePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String generatePromotionId(String loai) {
        List<Promotion> promotions = promotionBUS.getAllPromotions();
        // Sắp xếp danh sách theo IdKhuyenMai để đảm bảo lấy mã cuối cùng
        promotions.sort(Comparator.comparing(Promotion::getIdKhuyenMai));

        String lastDonLeId = null;
        String lastComboId = null;

        // Duyệt danh sách từ cuối để tìm mã cuối cùng
        for (int i = promotions.size() - 1; i >= 0; i--) {
            String id = promotions.get(i).getIdKhuyenMai();
            if (lastDonLeId == null && id.startsWith("KM") && !id.startsWith("KMCombo")) {
                lastDonLeId = id;
            }
            if (lastComboId == null && id.startsWith("KMCombo")) {
                lastComboId = id;
            }
            if (lastDonLeId != null && lastComboId != null) {
                break; // Đã tìm thấy cả hai, thoát vòng lặp
            }
        }

        // Tạo mã mới dựa trên loại
        if ("Combo".equals(loai)) {
            if (lastComboId == null) {
                return "KMCombo001"; // Nếu chưa có mã combo, bắt đầu từ 001
            }
            try {
                int number = Integer.parseInt(lastComboId.replace("KMCombo", ""));
                return String.format("KMCombo%03d", number + 1);
            } catch (NumberFormatException e) {
                return "KMCombo001"; // Nếu không parse được, mặc định là 001
            }
        } else {
            if (lastDonLeId == null) {
                return "KM001"; // Nếu chưa có mã đơn lẻ, bắt đầu từ 001
            }
            try {
                int number = Integer.parseInt(lastDonLeId.replace("KM", ""));
                return String.format("KM%03d", number + 1);
            } catch (NumberFormatException e) {
                return "KM001"; // Nếu không parse được, mặc định là 001
            }
        }
    }

    private void switchType(String newLoai) {
        if (isEditMode) return;
    
        loai = newLoai;
        btnDonLe.setBackground("Đơn lẻ".equals(loai) ? Color.LIGHT_GRAY : Color.WHITE);
        btnCombo.setBackground("Combo".equals(loai) ? Color.LIGHT_GRAY : Color.WHITE);
    
        // Cập nhật mã KM khi chuyển loại
        txtMaKM.setText(generatePromotionId(loai));
    
        if ("Combo".equals(loai)) {
            contentPanel.add(productPanel, BorderLayout.CENTER);
            setSize(600, 600); // to hơn khi có bảng
        } else {
            contentPanel.remove(productPanel);
            setSize(600, 500); // nhỏ gọn khi là đơn lẻ
        }
    
        // Cập nhật lại layout
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}