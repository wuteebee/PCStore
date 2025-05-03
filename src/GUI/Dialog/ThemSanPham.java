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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import BUS.AttributeBUS;
import BUS.ProductBUS;
import DAO.ProductDAO;
import DTO.Catalog;

public class ThemSanPham extends JDialog {
    private JTextField txtTen, gia;
    private JComboBox<String> loaisanpham, danhmuc, storage, thuonghieu;
    private JTextArea mota;
    private JLabel imageLabel;
    private String imageBase64;

    private String[] labels = {"Tên sản phẩm", "Giá", "Loại sản phẩm", "Danh mục", "Thương hiệu", "Mô tả"};
    private ProductDAO productDAO;
    private ProductBUS productBUS;
    private List <Catalog> catalogs;

    public void formThemSanPham() {
        AttributeBUS attributeBUS = new AttributeBUS();
        catalogs = attributeBUS.getAllCatalogs();
        

        

        setTitle("Thêm sản phẩm mới");
        setSize(550, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1;
        gbc.gridx = 0;


        gbc.anchor = GridBagConstraints.EAST;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i] + ": ");
            label.setFont(new Font("Arial", Font.BOLD, 13));
            add(label, gbc);
        }

       
        txtTen = new JTextField(18);
        gia = new JTextField(18);
        loaisanpham = new JComboBox<>();
        danhmuc = new JComboBox<>();
        thuonghieu = new JComboBox<>();
        storage = new JComboBox<>();
        mota = new JTextArea(5, 20);
        mota.setLineWrap(true);
        mota.setWrapStyleWord(true);
        JScrollPane motaScroll = new JScrollPane(mota);

        Map<String, Catalog> catalogMap = new HashMap<>();
        for (Catalog catalog : catalogs) {
            if(catalog.getDanhMucCha() == null) {
                loaisanpham.addItem(catalog.getTenDanhMuc());
            }
            catalogMap.put(catalog.getTenDanhMuc(), catalog);
        }
        if (loaisanpham.getItemCount() > 0) {
            loaisanpham.setSelectedIndex(0);
            updateDanhMucTheoLoai((String) loaisanpham.getSelectedItem(), catalogs);
        }
      
     
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0; add(txtTen, gbc);
        gbc.gridy = 1; add(gia, gbc);
        gbc.gridy = 2; add(loaisanpham, gbc);
        gbc.gridy = 3; add(danhmuc, gbc);
        gbc.gridy = 4; add(thuonghieu, gbc);
        gbc.gridy = 5; add(motaScroll, gbc);

        // ===== Ảnh
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel lblAnh = new JLabel("Ảnh sản phẩm:");
        lblAnh.setFont(new Font("Arial", Font.BOLD, 13));
        add(lblAnh, gbc);

        gbc.gridx = 1;
        JButton btnChonAnh = new JButton("Chọn ảnh");
        add(btnChonAnh, gbc);

        // ===== Hiển thị ảnh
        gbc.gridy = 7;
        imageLabel = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(imageLabel, gbc);

        // ===== Chọn ảnh
        btnChonAnh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(ThemSanPham.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage img = ImageIO.read(selectedFile);
                        Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImg));
                        imageLabel.setText("");

                        
                        byte[] imageBytes =Files.readAllBytes(selectedFile.toPath());
                        String format = getImageFormat(selectedFile.getName());
                        String base64 = Base64.getEncoder().encodeToString(imageBytes);
                        imageBase64 = "data:image/" + format + ";base64," + base64;

                        System.out.println("Base64 ảnh đã chọn: " + imageBase64.substring(0, 50) + "...");

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ThemSanPham.this, "Không thể đọc ảnh", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        loaisanpham.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLoai = (String) loaisanpham.getSelectedItem();
                if (selectedLoai != null) {
                    updateDanhMucTheoLoai(selectedLoai, catalogs);
                }
            }
        });
        


        setVisible(true);
    }


    
    private String getImageFormat(String fileName) {
        if (fileName.toLowerCase().endsWith(".png")) return "png";
        if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) return "jpeg";
        if (fileName.toLowerCase().endsWith(".gif")) return "gif";
        if (fileName.toLowerCase().endsWith(".bmp")) return "bmp";
        return "png"; // mặc định
    }

    private void updateDanhMucTheoLoai(String tenLoai, List<Catalog> catalogs) {
        danhmuc.removeAllItems();
        for (Catalog cat : catalogs) {
            if (cat.getDanhMucCha() != null && cat.getDanhMucCha().getTenDanhMuc().equals(tenLoai)) {
                danhmuc.addItem(cat.getTenDanhMuc());
            }
        }
    }
    

    // loaisanpham.addActionListener(new ActionListener() {
    //     @Override
    //     public void actionPerformed(ActionEvent e) {
    //         String selectedLoai = (String) loaisanpham.getSelectedItem();
            
    //         // Xóa hết danh mục cũ
    //         danhmuc.removeAllItems();
    
    //         for (Catalog cat : catalogs) {
    //             if (cat.getDanhMucCha() != null && cat.getDanhMucCha().getTenDanhMuc().equals(selectedLoai)) {
    //                 danhmuc.addItem(cat.getTenDanhMuc());
    //             }
    //         }
    //     }
    // });
    
    

}
