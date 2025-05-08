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
    private JComboBox<String> loaisanpham, danhmuc, storage, thuonghieu;
    private JTextArea mota;
    private JLabel imageLabel;
    private String imageBase64;
    private JButton submit= new Button().createStyledButton("Tạo", null);
    private String[] labels = {"Tên sản phẩm", "Giá", "Loại sản phẩm", "Danh mục", "Thương hiệu", "Mô tả"};
    private ProductDAO productDAO;
    private ProductBUS productBUS=new ProductBUS();
    private List <Catalog> catalogs;
    private List<Brand> brands;
    private ProductPanel panel;
    Map<String, Catalog> catalogMap ;
    Map<String, Brand> BrandMap;
    private      boolean isSave=false;
    public ThemSanPham(){

    }
    public ThemSanPham(ProductPanel panel){
        this.panel=panel;
    } 
    public void formThemSanPham(Product sp) {
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

        brands=attributeBUS.getAllBrand(); 
        for(Brand tmp:brands){
          System.out.println(tmp.getMaThuongHieu()+tmp.getTenThuongHieu()+tmp.getmaDanhMuc());
        }
        BrandMap = new HashMap<>();
      for (Brand th : brands) {
          if(th.getmaDanhMuc() == null) {
              thuonghieu.addItem(th.getTenThuongHieu());
           
          }
          BrandMap.put(th.getTenThuongHieu(), th);
      }

catalogMap = new HashMap<>();
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
        


        // Nút lưu nè
        gbc.gridx = 0;
        gbc.gridy = 8;             
        gbc.gridwidth = 2;        
        gbc.anchor = GridBagConstraints.CENTER;  
        gbc.fill = GridBagConstraints.NONE;      // không co giãn theo chiều ngang
        add(submit, gbc);
        
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
        

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tenSanPham = txtTen.getText().trim();
                String giaStr = gia.getText().trim();
        
                try {
                    double giaValue = Double.parseDouble(giaStr);
        
                    String loai = (String) loaisanpham.getSelectedItem();
                    String dm = (String) danhmuc.getSelectedItem();
                    String th = (String) thuonghieu.getSelectedItem();
                    String moTa = mota.getText().trim();
               
                    sp.setTenSp(tenSanPham);
                    sp.setDanhMuc(catalogMap.get(dm));
                    sp.setThuongHieu(BrandMap.get(th));
                    sp.setMoTaSanPham(moTa);
                    sp.setAnhSanPham(imageBase64);
                    sp.setGiasp(giaValue);
                    sp.setTrangThai(true);

                    String errorMsg = productBUS.insertSP(sp);
                    if (errorMsg == null) {
                        panel.updateTable(sp);
                        JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công!");
                        SwingUtilities.getWindowAncestor(submit).dispose(); 
 
                    } else {
                        JOptionPane.showMessageDialog(null, errorMsg, "Lỗi", JOptionPane.WARNING_MESSAGE);
                    }
        
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Giá sản phẩm không hợp lệ. Vui lòng nhập số!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        

        danhmuc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDM = (String) danhmuc.getSelectedItem();
                if (selectedDM != null) {
                    updateTHTheoDM(selectedDM, brands, catalogMap);
                }
            }
        });
        
        setVisible(true);
    }


    public boolean getIsSave(){
        return isSave;
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
    
    // Lay them thuong hieu theo idDanhMuc

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
    private void updateTHTheoDM(String tenDanhMuc, List<Brand> brands, Map<String, Catalog> cataMap) {
        thuonghieu.removeAllItems();
        String maDanhMuc = cataMap.get(tenDanhMuc).getMaDanhMuc();
        for (Brand th : brands) {
            if (th.getmaDanhMuc() != null && th.getmaDanhMuc().equals(maDanhMuc)) {
                thuonghieu.addItem(th.getTenThuongHieu());
            }
            else if(th.getmaDanhMuc()==null){
                thuonghieu.addItem(th.getTenThuongHieu());
            }
        }
    }

    

}
