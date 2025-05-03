package GUI.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import BUS.ProductBUS;
import DAO.AtributeDAO;
import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.ChiTietCauHinh;
import DTO.Product;
import DTO.ThongSoKyThuat;
import GUI.Components.MenuChucNang;
import GUI.Main;

public class ProductDetailPanel extends JPanel {
    private Main mainFrame;
    private String id;
    private ProductDAO productDAO = new ProductDAO();
    private Product product;
    private int phienban = 1;
    private JPanel cauhinhPanel;
    private List<JButton> configButtons = new ArrayList<>();
    private JLabel giaLabel;
    private JLabel soLuongLabel;



    public ProductDetailPanel(Main mainFrame, String id) {
        this.mainFrame = mainFrame;
        this.id = id;
        initComponent();
    }

    public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(960, 90));
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
        product = productDAO.getProductByIdFull(id);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // ===== Panel thông tin sản phẩm =====
        JPanel contentPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        contentPanel.add(new JLabel("Tên sản phẩm: " + product.getTenSp()));
        contentPanel.add(new JLabel("Danh mục: " + product.getDanhMuc().getTenDanhMuc()));
        contentPanel.add(new JLabel("Thương hiệu: " + product.getThuongHieu().getTenThuongHieu()));
        JTextArea moTaArea = new JTextArea("Mô tả: " + product.getMoTaSanPham());
        moTaArea.setWrapStyleWord(true);
        moTaArea.setLineWrap(true);
        moTaArea.setEditable(false);
        moTaArea.setFocusable(false);
        moTaArea.setOpaque(false);
        moTaArea.setBorder(null);
        contentPanel.add(moTaArea);

        contentPanel.add(new JLabel("Trạng thái: " + (product.isTrangThai() ? "Đang bán" : "Ngừng bán")));
        giaLabel=new JLabel();
        soLuongLabel=new JLabel(); 
        if(product.getDanhSachPhienBan().isEmpty()){
            giaLabel.setText("Giá: " + formatCurrency(0) );
            soLuongLabel.setText("Số lượng: " + 0 + " cái");
       
        }else{


        giaLabel.setText("Giá: " + formatCurrency(product.getDanhSachPhienBan().get(phienban-1).getGia()) + " VNĐ");
        soLuongLabel.setText("Số lượng: " + product.getDanhSachPhienBan().get(phienban-1).getSoLuong() + " cái");
    }
        contentPanel.add(giaLabel);
        contentPanel.add(soLuongLabel);

        // ===== Panel cấu hình (tab + chi tiết) =====
        JPanel panelConfigWrapper = new JPanel(new BorderLayout(10, 10));
        panelConfigWrapper.setBackground(Color.WHITE);

        JPanel variantTabsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        variantTabsPanel.setBackground(Color.WHITE);
        variantTabsPanel.setBorder(BorderFactory.createTitledBorder("Chọn cấu hình"));

        for (int i = 1; i <= product.getDanhSachPhienBan().size(); i++) {
            final int index = i;
            JButton btn = new JButton("Cấu hình " + index);
            btn.setPreferredSize(new Dimension(120, 30));
            btn.addActionListener(e ->{
                phienban = index;
                updateCauhinh(index);
                updateActivebutton();
            }
            );
            configButtons.add(btn);
            variantTabsPanel.add(btn);
        }


        panelConfigWrapper.add(variantTabsPanel, BorderLayout.NORTH);

        // Tạo panel chứa cấu hình, và thêm cấu hình ban đầu
        cauhinhPanel = new JPanel(new BorderLayout());
        cauhinhPanel.add(CauhinhPanel(phienban), BorderLayout.CENTER);
        panelConfigWrapper.add(cauhinhPanel, BorderLayout.CENTER);

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
        updateActivebutton();
        return panel;
    }

    public JPanel CauhinhPanel(int phienban) {
        AtributeDAO ab = new AtributeDAO();
        List<ThongSoKyThuat> thongSoList = ab.getAllTechnicalParameter(product.getDanhMuc().getMaDanhMuc());

        int cols = (thongSoList.size() >= 7) ? 2 : 1;

        JPanel panelConfig = new JPanel(new GridLayout(0, cols, 5, 5));
        panelConfig.setBackground(Color.WHITE);
        panelConfig.setBorder(BorderFactory.createTitledBorder("Chi tiết cấu hình"));

        ProductBUS productBUS = new ProductBUS();
        List<ChiTietCauHinh> defaultList = productBUS.getProductInfoconfig(id, "0");
        List<ChiTietCauHinh> overrideList = productBUS.getProductInfoconfig(id, String.valueOf(phienban - 1));

        Map<String, ChiTietCauHinh> mapCauHinh = new HashMap<>();
        for (ChiTietCauHinh ct : defaultList) {
            mapCauHinh.put(ct.getIdThongTin(), ct);
        }

        for (ChiTietCauHinh ct : overrideList) {
            mapCauHinh.put(ct.getIdThongTin(), ct);
        }

        for (ThongSoKyThuat tskt : thongSoList) {
            ChiTietCauHinh chitiet = mapCauHinh.get(tskt.getIdThongSo());
            String labelText = tskt.getTenThongSo() + ": ";

            if (chitiet instanceof CauHinhLaptop laptop) {
                labelText += laptop.getThongTin();
            } else if (chitiet instanceof CauHinhPC pc) {
                labelText += (pc.getLinhKien() != null) ? pc.getLinhKien().getTenSp() : "Không có linh kiện";
            } else {
                labelText += "(Chưa có)";
            }

            panelConfig.add(new JLabel(labelText));
        }

        return panelConfig;
    }

    public JPanel PanelAnh() {
        JPanel panelImg = new JPanel(new BorderLayout());
        panelImg.setPreferredSize(new Dimension(250, 250));
        panelImg.setBackground(Color.BLACK);

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);

        try {
            if (product.getAnhSanPham() == null || product.getAnhSanPham().isEmpty()) {
                imageLabel.setText("Không có ảnh sản phẩm");
                panelImg.add(imageLabel, BorderLayout.CENTER);
                return panelImg;
            }

            String base64Data = product.getAnhSanPham().split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

            int panelWidth = 250;
            int panelHeight = 250;

            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            float widthRatio = (float) panelWidth / imgWidth;
            float heightRatio = (float) panelHeight / imgHeight;
            float scale = Math.min(widthRatio, heightRatio);

            int scaledWidth = Math.max(1, Math.round(imgWidth * scale));
            int scaledHeight = Math.max(1, Math.round(imgHeight * scale));

            Image scaledImage = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);

            imageLabel.setIcon(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setText("Không thể hiển thị ảnh");
        }

        panelImg.add(imageLabel, BorderLayout.CENTER);
        return panelImg;
    }

    public void updateCauhinh(int i) {

        phienban = i;
        cauhinhPanel.removeAll();
        cauhinhPanel.add(CauhinhPanel(i), BorderLayout.CENTER);
        cauhinhPanel.revalidate();
        cauhinhPanel.repaint();
        giaLabel.setText("Giá: " + formatCurrency(product.getDanhSachPhienBan().get(i - 1).getGia()) + " VNĐ");
        soLuongLabel.setText("Số lượng: " + product.getDanhSachPhienBan().get(i - 1).getSoLuong() + " cái");

    
    }
    public void updateActivebutton(){
        for (int i = 0; i < configButtons.size(); i++) {
            if (i == phienban - 1) {
                configButtons.get(i).setBackground(new Color(100, 149, 237));
                configButtons.get(i).setForeground(Color.WHITE);
            } else {
                configButtons.get(i).setBackground(Color.WHITE);
                configButtons.get(i).setForeground(Color.BLACK);
            }
        }


    }

    public static String formatCurrency(double amount) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator('.');
    DecimalFormat formatter = new DecimalFormat("#,###", symbols);
    return formatter.format(amount) + " VNĐ";
}
public int getPhienban() {
    return phienban;

}}

