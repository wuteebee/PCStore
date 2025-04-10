package GUI.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.*;

import BUS.ProductBUS;
import DAO.ProductDAO;
import DTO.Product;
import GUI.Components.MenuChucNang;
import GUI.Main;

public class ProductDetailPanel extends JPanel {
    private Main mainFrame;
    private String id;
    private ProductDAO productDAO = new ProductDAO();
    private Product product;

    public ProductDetailPanel(Main mainFrame, String id) {
        this.mainFrame = mainFrame;
        this.id = id;
        initComponent();
    }

    public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));
        toolbar.setBackground(Color.WHITE);

        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));
        toolbar.add(MenuChucNang.createSearchPanel());

        return toolbar;
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);
        this.setVisible(true);

        add(createCustomToolbar(), BorderLayout.NORTH);
        add(mainProduct(), BorderLayout.CENTER);
    }

    private JPanel mainProduct() {
        product= productDAO.getProductByIdFull(id);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;



        // ===== Panel ảnh sản phẩm =====
       
        


        // ===== Panel thông tin sản phẩm =====
        JPanel contentPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        contentPanel.add(new JLabel("Tên sản phẩm: " + product.getTenSp()));
        contentPanel.add(new JLabel("Danh mục: " + product.getDanhMuc().getTenDanhMuc()));
        contentPanel.add(new JLabel("Thương hiệu: " + product.getThuongHieu().getTenThuongHieu()));
        contentPanel.add(new JLabel("Mô tả: " + product.getMoTaSanPham()));
        contentPanel.add(new JLabel("Trạng thái: " + (product.isTrangThai() ? "Đang bán" : "Ngừng bán")));

        // ===== Panel cấu hình (tab + chi tiết) =====
        JPanel panelConfigWrapper = new JPanel(new BorderLayout(10, 10));
        panelConfigWrapper.setBackground(Color.WHITE);
        System.out.println(product.getDanhSachPhienBan().size());

        JPanel variantTabsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        variantTabsPanel.setBackground(Color.WHITE);
        variantTabsPanel.setBorder(BorderFactory.createTitledBorder("Chọn cấu hình"));

        for (int i = 1; i <= product.getDanhSachPhienBan().size(); i++) {
            JButton btn = new JButton("Cấu hình " + i);
            btn.setPreferredSize(new Dimension(120, 30));
            variantTabsPanel.add(btn);
        }

 

        // ==== Gán vào layout chính ====
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.6;
        panel.add(PanelAnh(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 2;
        panel.add(contentPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.4;
        panel.add(panelConfigWrapper, gbc);
        panelConfigWrapper.add(variantTabsPanel, BorderLayout.NORTH);
        panelConfigWrapper.add(CauhinhPanel(), BorderLayout.CENTER);
        return panel;
    }


    public JPanel CauhinhPanel(){
        // System.out.println(
        //     "Số lượng "+
        //     product.getDanhSachPhienBan().get(0).getChitiet().size()
        // );

        JPanel panelConfig = new JPanel(new GridLayout(0, 1, 5, 5));
        panelConfig.setBackground(Color.WHITE);
        panelConfig.setBorder(BorderFactory.createTitledBorder("Chi tiết cấu hình"));

        System.out.println("Danh mục nè: " +product.getDanhMuc().getMaDanhMuc());
        // panelConfig.add(new JLabel("➤ Phiên bản 1:"));
        // panelConfig.add(new JLabel(" - RAM: 8GB"));
        // panelConfig.add(new JLabel(" - SSD: 256GB"));
        ProductBUS productBUS = new ProductBUS();
        productBUS.getProductInfoconfig(id, 0);



        return panelConfig;
    }


    public JPanel PanelAnh(){
        JPanel panelImg = new JPanel(new BorderLayout());
        panelImg.setPreferredSize(new Dimension(250, 250));
        panelImg.setBackground(Color.black);
        // panelImg.setBorder(BorderFactory.createTitledBorder("Ảnh sản phẩm"));

        // System.out.println(product.getAnhSanPham());
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        try {
            if(product.getAnhSanPham() == null || product.getAnhSanPham().isEmpty()) {
                imageLabel.setText("Không có ảnh sản phẩm");
                return panelImg;
            }
            String base64Data = product.getAnhSanPham().split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            System.out.println(imageBytes.length);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
            // Scale ảnh để vừa khung
            Image scaledImage = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            imageLabel.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setText("Không thể hiển thị ảnh");
        }


        panelImg.add(imageLabel, BorderLayout.CENTER);
        return panelImg;
    }
}
