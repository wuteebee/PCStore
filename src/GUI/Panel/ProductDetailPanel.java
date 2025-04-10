package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.ChiTietCauHinh;
import DTO.Product;
import DTO.Variant;
import GUI.Components.MenuChucNang;
import GUI.Main;

public class ProductDetailPanel extends JPanel {
    private Main mainFrame;  // 🔹 Khai báo biến mainFrame
    private String id;

    public ProductDetailPanel(Main mainFrame,String id) {
        this.mainFrame = mainFrame; // 🔹 Gán giá trị
        this.id=id;
        initComponent();
    }

    public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));
        toolbar.setBackground(Color.WHITE);
        toolbar.setOpaque(true);

        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame)); // ✅ Sử dụng biến đã khai báo
        toolbar.add(MenuChucNang.createSearchPanel());

        return toolbar;
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);
        this.setVisible(true);
        add(createCustomToolbar(), BorderLayout.NORTH);
        add(mainProduct(),BorderLayout.CENTER);

    }


    private JPanel mainProduct() {
        
        ProductDAO pd = new ProductDAO();
        Product p = pd.getProductByIdFull(id);
        
        System.out.println("ID Sản phẩm: " + p.getMaSp());
        System.out.println("Tên: " + p.getTenSp());
        System.out.println("Mô tả: " + p.getMoTaSanPham());
        System.out.println("Danh mục: " + p.getDanhMuc().getTenDanhMuc());
        System.out.println("Thương hiệu: " + p.getThuongHieu().getTenThuongHieu());
        
        System.out.println("\n--- Danh sách phiên bản ---");
        List<Variant> danhSachPhienBan = p.getDanhSachPhienBan();
        for (Variant pb : danhSachPhienBan) {
            System.out.println("Phiên bản: " + pb.getPhienBan());
            System.out.println("Giá: " + pb.getGia());
            System.out.println("Số lượng: " + pb.getSoLuong());
        
            System.out.println(">>> Cấu hình:");
            for (ChiTietCauHinh ch : pb.getChitiet()) {
                if (ch instanceof CauHinhLaptop) {
                    CauHinhLaptop lap = (CauHinhLaptop) ch;
                    System.out.println("- " + lap.getIdThongTin() + ": " + lap.getThongTin());
                } else if (ch instanceof CauHinhPC) {
                    CauHinhPC pc = (CauHinhPC) ch;
                    System.out.println("- " + pc.getIdThongTin() + ": " + pc.getLinhKien().getTenSp());
                }
            }
            System.out.println("----------------------");
        }
        

        // for (ChiTietCauHinh ct : pb.getChitiet()) {
        //     System.out.println(ct.getIdThongTin());
            
        //     if (ct instanceof CauHinhPC) {
        //         CauHinhPC pcConfig = (CauHinhPC) ct;
        //         System.out.println("Linh kiện: " + pcConfig.getLinhKien().getTenSp());
        //     }
        //     else{
        //         CauHinhLaptop laptopconfig=(CauHinhLaptop)ct;
        //         System.out.println("Thông tin: "+laptopconfig.getThongTin());
        //     }
        // }


        

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.BOTH;
    
        // 👇 Panel ảnh
        JPanel panelImg = new JPanel();
        panelImg.setBackground(Color.BLACK);
        panelImg.setPreferredSize(new Dimension(300, 300)); // Optional, hoặc để layout tự scale
    
        // 👇 Panel nội dung
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.RED);
    
        // 🧱 Panel thông tin thêm
        JPanel pn = new JPanel();
        pn.setBackground(Color.PINK);
    
        // 👉 Dòng 0, Cột 0: panelImg
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5; // Chia tỉ lệ ngang
        gbc.weighty = 1;
        panel.add(panelImg, gbc);
    
        // 👉 Dòng 0, Cột 1: contentPanel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.5; // Chiếm phần lớn hơn
        panel.add(contentPanel, gbc);
    
        // 👉 Dòng 1, Cột span 2: pn
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        panel.add(pn, gbc);
    



        
        return panel;
    }
    
}




// Lấy sản phẩm được chọn
// Lấy thông tin sản phẩm
// Bảng sanpham
