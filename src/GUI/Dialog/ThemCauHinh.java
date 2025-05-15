package GUI.Dialog;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import DAO.AtributeDAO;
import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.ChiTietCauHinh;
import DTO.Product;
import DTO.ProductDetail;
import DTO.ThongSoKyThuat;
import DTO.Variant;
import GUI.Panel.ProductDetailPanel;
// import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import BUS.ProductBUS;
public class ThemCauHinh extends JDialog {
    private JTextField[] textFields;
    
    private ProductDetailPanel panel;
    private Variant variant;
    public ThemCauHinh() {
    }

    public ThemCauHinh(ProductDetailPanel panel, Variant variant) {
        this.variant = variant;
        this.panel = panel;
        initComponents();
        setVisible(true);
    }

    private void initComponents(){
    
        String maDM = panel.getProduct().getDanhMuc().getMaDanhMuc();
        ProductDAO pDao = new ProductDAO();
        String dmc = pDao.getDanhMucCha(maDM);

        setTitle(variant==null?"Thêm cấu hình mới":"Sửa thông tin cấu hình");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        if(variant!=null){
            System.out.println("ID cấu hình nè"+variant.getIdVariant());
            System.out.println("Giá: "+variant.getGia());
            System.out.println("Phiên bản: "+variant.getPhienBan());
        }
        Map<String,CauHinhPC> mapPC = new HashMap<>();
        Map<String,CauHinhLaptop>mapLaptop = new HashMap<>();
        if(variant!=null){
      for (ChiTietCauHinh ct:variant.getChitiet()){
            if (ct instanceof CauHinhPC){
                CauHinhPC pc = (CauHinhPC) ct;
                mapPC.put(pc.getIdThongTin(), pc);
            }
            else if (ct instanceof CauHinhLaptop){
                CauHinhLaptop laptop = (CauHinhLaptop) ct;
                mapLaptop.put(laptop.getIdThongTin(), laptop);
            }
        }
        }
  
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;

        AtributeDAO ab = new AtributeDAO();
        List<ThongSoKyThuat> thongsoList = ab.getAllTechnicalParameter(dmc);
        ProductBUS productBUS=new ProductBUS();
        List<Map<String, Product>> listProductMaps = new ArrayList<>();
        textFields = new JTextField[thongsoList.size()];
        JComboBox<String>[] comboBox = new JComboBox[thongsoList.size()];

        int cols = (thongsoList.size() >= 7) ? 2 : 1;
        
        for (int i = 0; i < thongsoList.size(); i++) {
            ThongSoKyThuat ts = thongsoList.get(i);
            
            gbc.gridx = (cols == 2 && i >= (thongsoList.size() + 1) / 2) ? 2 : 0;
            gbc.gridy = (cols == 2 && i >= (thongsoList.size() + 1) / 2) ? i - (thongsoList.size() + 1) / 2 : i;
            JLabel label = new JLabel(ts.getTenThongSo() + ": ");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            add(label, gbc);

            gbc.gridx += 1;

            if (dmc.equals("DM002")) {
                List<Product> listlinhkien= productBUS.getSPbyCatalog(ts.getIdDMLinhKien());
                
                      Map<String, Product> mapLinhKien = listlinhkien.stream()
            .collect(Collectors.toMap(Product::getTenSp, Function.identity()));
        listProductMaps.add(mapLinhKien);
                comboBox[i] = new JComboBox<>();
                comboBox[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
                comboBox[i].setPreferredSize(new Dimension(250, 30));
                comboBox[i].setEditable(true);
        
                
                for (Product linhkien: listlinhkien){
                    comboBox[i].addItem(linhkien.getTenSp());
               
                }
                if (mapPC.containsKey(ts.getIdThongSo())) {
                    CauHinhPC pc = mapPC.get(ts.getIdThongSo());
                    if(pc.getLinhKien() != null){
                    comboBox[i].setSelectedItem(pc.getLinhKien().getTenSp());
                    }
                  
                }
                else{
                                        comboBox[i].setSelectedItem("");
                }
                AutoCompleteDecorator.decorate(comboBox[i]);

                add(comboBox[i], gbc);

                // comboBox.addActionListener(e -> {
                //     String selectedItem = (String) comboBox.getSelectedItem();
                //     System.out.println("Selected CPU: " + selectedItem);
                // });

            } else {
                JTextField textField = new JTextField();
                textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                textField.setPreferredSize(new Dimension(250, 30));
                textFields[i] = textField;
                if (mapLaptop.containsKey(ts.getIdThongSo())) {
                CauHinhLaptop laptop = mapLaptop.get(ts.getIdThongSo());
                textField.setText(laptop.getThongTin());
            }

                add(textField, gbc);
            }
        }

    gbc.gridx = 0;
    gbc.gridy = (cols == 2) ? (thongsoList.size() + 1) / 2 + 1 : thongsoList.size() + 1;
    JLabel priceLabel = new JLabel("Giá: ");
    priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    add(priceLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JTextField priceField = new JTextField();
    priceField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    priceField.setPreferredSize(new Dimension(250, 30));
        if (variant != null) {
        priceField.setText(String.valueOf(variant.getGia()));
    }
    add(priceField, gbc);

        JButton btnSave = new JButton("💾 Lưu cấu hình");
        btnSave.setFocusPainted(false);
        btnSave.setBackground(new Color(66, 133, 244));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = (cols == 2) ? (thongsoList.size() + 1) / 2 + 2 : thongsoList.size() + 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnSave, gbc);
    

        btnSave.addActionListener(e -> {
            boolean Save=false;
            if(!dmc.equals("DM002")){
                for (int i = 0; i < textFields.length; i++) {
                    String value = textFields[i].getText().trim();
                    System.out.println(thongsoList.get(i).getTenThongSo() + ": " + value);
                }
            }
            else{
                for (int i = 0; i < comboBox.length; i++) {
                    if (comboBox[i] != null) {
                        String selected = (String) comboBox[i].getSelectedItem();
                        System.out.println(thongsoList.get(i).getTenThongSo() + ": " + selected);
                    }
                }
            }
            System.out.println("Click lưu cấu hình nè"+panel.getProduct().getMaSp());
            System.out.println("Bắt đầu thêm data nè:   ------------------------------");
            ProductDAO productDAO=new ProductDAO();

            // Cauhinhpc: idSanPham idThongTin idLinhKien STTPL
            // cauhinhlaptop: idSanPham idThongTin ThongTin STTPL
            if(dmc.equals("DM002")){
                productDAO.deleteCauhinhPC(panel.getProduct().getMaSp(),panel.getPhienban()-1);
                for (int i=0;i<comboBox.length;i++){
                    String maLinhKien="null";
                    if (comboBox[i] != null&& comboBox[i].getSelectedItem() !=null) {
                     
                        
                        if(comboBox[i].getSelectedItem()!=null&&comboBox[i].getSelectedItem()!=""){
                            Map<String, Product> mapLinhKien = listProductMaps.get(i);
                            Product linhKien = mapLinhKien.get(comboBox[i].getSelectedItem());
                            maLinhKien=linhKien.getMaSp();
                        }
                    
                    }
                    if(maLinhKien.equals("null")){
                        continue;
                    }
      
                         boolean inserted = false;
                        if(variant!=null){
                              inserted = productBUS.insertCauHinh(panel.getPhienban()-1,
                            panel.getProduct().getMaSp(),
                            thongsoList.get(i).getIdThongSo(),
                            maLinhKien,true
                        );

                        }
                        else{
                                        inserted  = productBUS.insertCauHinh(panel.getProduct().getDanhSachPhienBan().size(),
                            panel.getProduct().getMaSp(),
                            thongsoList.get(i).getIdThongSo(),
                            maLinhKien,true
                        );

                        }
                    
                        System.out.println("Insert PC cấu hình " + i + ": " + (inserted ? "✅ Thành công" : "❌ Thất bại"));
                       Save=inserted? true: false;
                } 
            }
            else{
                  productDAO.deleteCauhinhLaptop(panel.getProduct().getMaSp(),panel.getPhienban()-1);
                for(int i=0;i<textFields.length;i++){
                    String value = textFields[i].getText().trim();
                    System.out.println(thongsoList.get(i).getTenThongSo() + ": " + value);
                    System.out.println(panel.getProduct().getMaSp()+" "+thongsoList.get(i).getIdThongSo()+textFields[i].getText().trim()+" " + panel.getProduct().getDanhSachPhienBan().size());
                    boolean inserted = false;
                    if(value.equals("")){
                        continue;
                    }
                    if(variant!=null){
                        System.out.println("ID cấu hình nè"+variant.getIdVariant());
                         inserted = productBUS.insertCauHinh(panel.getPhienban()-1,
                            panel.getProduct().getMaSp(),
                            thongsoList.get(i).getIdThongSo(),textFields[i].getText().trim(),
                            false
                        );
                    }
                    else{
                    inserted = productBUS.insertCauHinh( panel.getProduct().getDanhSachPhienBan().size(),
                        panel.getProduct(  ).getMaSp(),
                        thongsoList.get(i).getIdThongSo(),textFields[i].getText().trim(),
                       false
                    );}
                
                    System.out.println("Insert Laptop cấu hình " + i + ": " + (inserted ? "✅ Thành công" : "❌ Thất bại"));
                    Save=inserted? true:false;
                }
            }
        
        
            

            // Lấy thông số dựa vào tên thông số  -> Lấy ra được id Thongtin  -> Tính STTPL -> IDSP
        //    Với pc  -> Lấy ra id Linh kiện dựa vào tên linh kiện
         if (Save) {
    String priceText = priceField.getText().trim(); // Lấy giá trị từ ô nhập

    try {
        double price = Double.parseDouble(priceText); // Chuyển đổi giá trị thành kiểu double
        
        // Tiến hành xử lý với giá trị price
        System.out.println("Giá sản phẩm: " + price);

        if (variant != null) {
            // Cập nhật thông tin sản phẩm và phien bản
            productDAO.updateTrangThaiplsp(
                panel.getProduct().getDanhSachPhienBan().get(panel.getPhienban() - 1).getIdVariant(),  
                false
            );
            productDAO.insertplsp(
                panel.getProduct().getMaSp(),
                panel.getPhienban() - 1,
                price,  // Ghi giá trị đã chuyển đổi
                0
            );
        } else {
            // Chèn thông tin cho sản phẩm mới không có variant
            productDAO.insertplsp(
                panel.getProduct().getMaSp(),
                panel.getProduct().getDanhSachPhienBan().size(),
                price,  // Ghi giá trị đã chuyển đổi
                0
            );
        }

  } catch (NumberFormatException ex) {
    // Hiển thị thông báo lỗi và thông tin chi tiết về ngoại lệ (nếu cần)
    JOptionPane.showMessageDialog(this, "Vui lòng nhập giá hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    ex.printStackTrace();  // In thông tin chi tiết về ngoại lệ ra console để debug
}

}

            JOptionPane.showMessageDialog(this, "Thêm cấu hình thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            // panel.updateCauhinh(panel.getProduct().getMaSp());
            panel.reloadPanel();
        });


        setMinimumSize(new Dimension(800, 600));

        setVisible(true);
}
}
