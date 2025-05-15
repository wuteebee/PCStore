package GUI.Dialog;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale.Category;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import BUS.AttributeBUS;
import BUS.ProductBUS;
import DAO.ProductDAO;
import DTO.Brand;
import DTO.Catalog;
import DTO.Product;
import GUI.Components.Button;
import GUI.Panel.ProductPanel;
public class ThemSanPham extends JDialog {
    private JTextField txtTen, gia;
    private JComboBox<String> loaisanpham, danhmuc, thuonghieu;
    private JTextArea mota;
    private JLabel imageLabel;
    private String imageBase64;
    private JButton submit = new Button().createStyledButton("Tạo", null);

    private Product editingProduct; // null nếu thêm mới
    private ProductPanel panel;
    private Map<String, Catalog> catalogMap = new HashMap<>();
    private Map<String, Brand> brandMap = new HashMap<>();
    private List<Catalog> catalogs;
    private List<Brand> brands;

    public ThemSanPham(ProductPanel panel, Product productToEdit) {
        this.panel = panel;
        this.editingProduct = productToEdit;
        initializeForm();
        setVisible(true);
    }

    private void initializeForm() {
        setTitle(editingProduct == null ? "Thêm sản phẩm mới" : "Sửa sản phẩm");
        setSize(550, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        AttributeBUS attributeBUS = new AttributeBUS();
        catalogs = attributeBUS.getAllCatalogs();
        brands = attributeBUS.getAllBrand();

        createFields();
        buildLayout();
        bindEvents();

        if (editingProduct != null) {
            populateData(editingProduct);
        }
    }

    private void createFields() {
        txtTen = new JTextField(18);
        gia = new JTextField(18);
        loaisanpham = new JComboBox<>();
        danhmuc = new JComboBox<>();
        thuonghieu = new JComboBox<>();
        mota = new JTextArea(5, 20);
        mota.setLineWrap(true);
        mota.setWrapStyleWord(true);
        imageLabel = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        for (Catalog c : catalogs) {
            if (c.getDanhMucCha() == null)
                loaisanpham.addItem(c.getTenDanhMuc());
            catalogMap.put(c.getTenDanhMuc(), c);
        }

        for (Brand b : brands) {
            if (b.getmaDanhMuc() == null)
                thuonghieu.addItem(b.getTenThuongHieu());
            brandMap.put(b.getTenThuongHieu(), b);
        }
    }

    private void buildLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Tên sản phẩm", "Giá", "Loại sản phẩm", "Danh mục", "Thương hiệu", "Mô tả"};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            add(new JLabel(labels[i] + ":"), gbc);
        }

        gbc.gridx = 1;
        gbc.gridy = 0; add(txtTen, gbc);
        gbc.gridy = 1; add(gia, gbc);
        gbc.gridy = 2; add(loaisanpham, gbc);
        gbc.gridy = 3; add(danhmuc, gbc);
        gbc.gridy = 4; add(thuonghieu, gbc);
        gbc.gridy = 5; add(new JScrollPane(mota), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Ảnh sản phẩm:"), gbc);

        gbc.gridx = 1;
        JButton btnChonAnh = new JButton("Chọn ảnh");
        add(btnChonAnh, gbc);

        gbc.gridy = 7;
        add(imageLabel, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submit, gbc);

        btnChonAnh.addActionListener(e -> chooseImage());
    }

    private void bindEvents() {
        loaisanpham.addActionListener(e -> {
            String selected = (String) loaisanpham.getSelectedItem();
            updateDanhMucTheoLoai(selected);
        });

        danhmuc.addActionListener(e -> {
            String selectedDM = (String) danhmuc.getSelectedItem();
            updateThuongHieuTheoDanhMuc(selectedDM);
        });

        submit.addActionListener(e -> {
            String ten = txtTen.getText().trim();
            String giaStr = gia.getText().trim();

            try {
                double giaValue = Double.parseDouble(giaStr);
                String dm = (String) danhmuc.getSelectedItem();
                String th = (String) thuonghieu.getSelectedItem();

                Product sp = (editingProduct != null) ? editingProduct : new Product();
                sp.setTenSp(ten);
                sp.setGiasp(giaValue);
                sp.setMoTaSanPham(mota.getText().trim());
                sp.setAnhSanPham(imageBase64);
                sp.setDanhMuc(catalogMap.get(dm));
                sp.setThuongHieu(brandMap.get(th));
                sp.setTrangThai(true);
                if(sp.getDanhMuc()==null){
                    sp.setDanhMuc(catalogMap.get(loaisanpham.getSelectedItem()));
                }
                ProductBUS productBUS = new ProductBUS();
                String error = (editingProduct == null) ? productBUS.saveProduct(sp) : productBUS.saveProduct(sp);

                if (error == null) {
                    panel.updateTable(sp);
                    JOptionPane.showMessageDialog(this, (editingProduct == null ? "Thêm" : "Cập nhật") + " sản phẩm thành công!");
                    panel.reloadPanel();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá sản phẩm không hợp lệ.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void chooseImage() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(file);
                imageLabel.setIcon(new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
                imageLabel.setText("");
                byte[] imgBytes = Files.readAllBytes(file.toPath());
                imageBase64 = "data:image/" + getImageFormat(file.getName()) + ";base64," + Base64.getEncoder().encodeToString(imgBytes);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Không thể đọc ảnh", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   private void populateData(Product sp) {
    txtTen.setText(sp.getTenSp());
    gia.setText(String.valueOf(sp.getGiasp()));
    mota.setText(sp.getMoTaSanPham());

    try {
        String base64String = sp.getAnhSanPham();

        if (base64String != null && !base64String.isEmpty()) {
            // If the Base64 string contains data:image/png;base64,..., split it out
            if (base64String.contains(",")) {
                base64String = base64String.split(",")[1];
            }

            byte[] imageData = Base64.getDecoder().decode(base64String);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));

            if (img != null) {
                int imgWidth = img.getWidth();
                int imgHeight = img.getHeight();

                int targetWidth = 200;
                int targetHeight = 200;

                float widthRatio = (float) targetWidth / imgWidth;
                float heightRatio = (float) targetHeight / imgHeight;
                float scale = Math.min(widthRatio, heightRatio);

                int scaledWidth = Math.max(1, Math.round(imgWidth * scale));
                int scaledHeight = Math.max(1, Math.round(imgHeight * scale));

                Image scaledImage = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText(""); // Clear "Chưa có ảnh" text
            } else {
                imageLabel.setText("Ảnh không hợp lệ");
            }
        } else {
            imageLabel.setText("Không có ảnh");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        imageLabel.setText("Không thể hiển thị ảnh");
    }

    String loai = sp.getDanhMuc().getDanhMucCha() != null ? sp.getDanhMuc().getDanhMucCha().getTenDanhMuc() : sp.getDanhMuc().getTenDanhMuc();
    loaisanpham.setSelectedItem(loai);
    updateDanhMucTheoLoai(loai);
    danhmuc.setSelectedItem(sp.getDanhMuc().getTenDanhMuc());
    thuonghieu.setSelectedItem(sp.getThuongHieu().getTenThuongHieu());

    submit.setText("Lưu");
}

    private void updateDanhMucTheoLoai(String loai) {
        danhmuc.removeAllItems();
        for (Catalog c : catalogs) {
            if (c.getDanhMucCha() != null && c.getDanhMucCha().getTenDanhMuc().equals(loai)) {
                danhmuc.addItem(c.getTenDanhMuc());
            }
        }
    }

    private void updateThuongHieuTheoDanhMuc(String danhMuc) {
        thuonghieu.removeAllItems();
        for (Brand b : brands) {
            if (b.getmaDanhMuc() != null && catalogMap.containsKey(danhMuc)
                && b.getmaDanhMuc().equals(catalogMap.get(danhMuc).getMaDanhMuc())) {
                thuonghieu.addItem(b.getTenThuongHieu());
            }
        }
    }

    private String getImageFormat(String name) {
        name = name.toLowerCase();
        if (name.endsWith(".png")) return "png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "jpeg";
        return "png";
    }
}
