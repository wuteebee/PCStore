package GUI.Panel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BUS.EmployeeBUS;
import BUS.PhieuNhapBUS;
import BUS.ProductBUS;
import BUS.SupplierBUS;
import DAO.AtributeDAO;
import DAO.EmployeeDAO;
import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.Employee;
import DTO.HoaDonNhap;
import DTO.Product;
import DTO.ProductDetail;
import DTO.Supplier;
import DTO.Variant;
import GUI.Main;

import java.awt.*;
import java.sql.Date;

public class NhapHoaDonPanel extends JPanel {
    private JTable tableSp, tableChiTiet,tablechsp;
    private DefaultTableModel modelSp, modelChiTiet,modelchsp;
    private JTextField tfMaSP, tfTenSP, tfGiaNhap, tfMaPhieu, tfNhanVien, tfMaImei,soluongsp, tfImeiFrom,tfImeiTo;
    private JComboBox<String> cbCauHinh, cbPhuongThuc, cbNhaCungCap;
    private JLabel lbTongTien,soluong;
    private ProductDAO productDAO;
    private JButton Suasp, Xoasp, Luu, Huy,btnThem,submitbutton;
    private JRadioButton rbTuNhap, rbTheoKhoang;
    private Main mainFrame;
    private int maphanloai;
    private HashMap<String, List<ProductDetail>> chiTietHDN = new HashMap<>();
    private double tt;
    public NhapHoaDonPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setBounds(0, 0, 1150, 700);
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
        initBottomPanel();
     
    }

    public NhapHoaDonPanel(){}
private void initLeftPanel() {
    JPanel leftPanel = new JPanel(null);
    leftPanel.setBounds(10, 10, 300, 420);
    leftPanel.setBorder(null);

    JTextField txtTimKiem = new JTextField();
    txtTimKiem.setBounds(10, 10, 220, 30);

    String[] columnsSp = {"Mã SP", "Tên SP"};

    modelSp = new DefaultTableModel(columnsSp, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tableSp = new JTable(modelSp);

    JTableHeader header = tableSp.getTableHeader();
    header.setBackground(new Color(220, 220, 220));
    header.setFont(new Font("Segoe UI", Font.BOLD, 12));

    JScrollPane scroll = new JScrollPane(tableSp);
    scroll.setBounds(10, 50, 280, 250);

   btnThem = new JButton("Thêm");
    btnThem.setBounds(10, 310, 130, 30);
    JButton btnNhapExcel = new JButton("Nhập Excel");
    btnNhapExcel.setBounds(150, 310, 130, 30);

    productDAO = new ProductDAO();
    List<Product> products = productDAO.getAllProducts();
    for (Product product : products) {
        modelSp.addRow(new Object[]{product.getMaSp(), product.getTenSp()});
    }

    txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            searchProducts(txtTimKiem.getText());
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            searchProducts(txtTimKiem.getText());
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            searchProducts(txtTimKiem.getText());
        }
    });

    // Thêm ListSelectionListener để nhận sự kiện click vào một hàng trong bảng
    tableSp.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && tableSp.getSelectedRow() != -1) {
            int selectedRow = tableSp.getSelectedRow();
            String maSp = (String) modelSp.getValueAt(selectedRow, 0); 

            System.out.println("Selected Product: " + maSp + " - " ); 
            updateCenterPanel(maSp);
            updateCauhinh(maSp,cbCauHinh.getSelectedItem().toString()); 
        
        }
    });

    leftPanel.add(txtTimKiem);
    leftPanel.add(scroll);
    leftPanel.add(btnThem);
    leftPanel.add(btnNhapExcel);

    add(leftPanel);
    btnThem.addActionListener(e -> {
        updatePhieuNhap();
    });
}

private void updatePhieuNhap() {
    String maSP = tfMaSP.getText();
    String tenSP = tfTenSP.getText();
    String phienBan = cbCauHinh.getSelectedItem().toString();
    String giaNhap = tfGiaNhap.getText();
    String imei = tfMaImei.getText();
    String soLuong = soluongsp.getText();
    String phuongThuc = cbPhuongThuc.getSelectedItem().toString();
    String imeiFrom = tfImeiFrom.getText();
    String imeiTo = tfImeiTo.getText();
    tfMaSP.setText("");
    tfTenSP.setText("");
    cbCauHinh.setSelectedIndex(0);
    tfGiaNhap.setText("");
    tfMaImei.setText("");
    soluongsp.setText("");
    tfImeiFrom.setText("");
    tfImeiTo.setText("");

    cbPhuongThuc.setSelectedIndex(0);
    
    if(rbTuNhap.isSelected()){
        imei = tfMaImei.getText();
        List<ProductDetail> list = new ArrayList<>();
        System.out.println("imei nè" +imei);
        ProductDetail productDetail = new ProductDetail(imei, maphanloai, Double.parseDouble(giaNhap) , true);
        list.add(productDetail);
         chiTietHDN.put((modelChiTiet.getRowCount() + 1)+"", list);
         soLuong="1";
    }
    else if(rbTheoKhoang.isSelected()){

        imei = tfImeiFrom.getText() + " - " + tfImeiTo.getText();
        System.out.println("imei: " + imei);
        List<ProductDetail> list = new ArrayList<>();
        for (String imeiNumber : generateImeiList(imeiFrom, imeiTo)) {
            System.out.println("Ê"+ imeiNumber );
            ProductDetail productDetail = new ProductDetail(imeiNumber, maphanloai, Double.parseDouble(giaNhap) , true);
            list.add(productDetail);
        }
      int stt = modelChiTiet.getRowCount() + 1;
chiTietHDN.put(String.valueOf(stt), list);
System.out.println("Đây là danh sách IMEI: " + stt);

    }
    // Lưu gì giờ:))))
    // Serial number, idphanloai
    // Thêm dòng mới vào bảng
    modelChiTiet.addRow(new Object[]{
        modelChiTiet.getRowCount() + 1,
        maSP,
        tenSP,
        phienBan,
       String.format("%,d", Integer.parseInt(giaNhap)).replace(",", "."),
        soLuong
    });

    // Tính tổng tiền
    int tongTien = Integer.parseInt(giaNhap) * Integer.parseInt(soLuong);
    tt=tongTien;
    // Định dạng tiền (thêm dấu chấm ngăn cách hàng nghìn)
    String tongTienFormatted = String.format("%,d", tongTien).replace(",", ".");

    // Hiển thị tổng tiền
    lbTongTien.setText("TỔNG TIỀN: " + tongTienFormatted + "đ");

  
   

    tfMaSP.requestFocus(); 
}

// Phương thức tìm kiếm và cập nhật bảng sản phẩm
private void searchProducts(String query) {
    // Xóa hết các dòng cũ trong bảng
    modelSp.setRowCount(0);

    // Lấy danh sách sản phẩm từ cơ sở dữ liệu, lọc theo từ khóa tìm kiếm
    List<Product> products = productDAO.getAllProducts();
    for (Product product : products) {
        if(product.getTenSp().toLowerCase().contains(query.toLowerCase())) {
            modelSp.addRow(new Object[]{product.getMaSp(), product.getTenSp()});
        }
    }
}

    private void updateCenterPanel(String maSP){
        Product product = productDAO.getProductByIdFull(maSP);
        tfMaSP.setText(product.getMaSp());
        tfTenSP.setText(product.getTenSp());
    cbCauHinh.removeAllItems();
        for(Variant variant: product.getDanhSachPhienBan()){
            if(variant.isTrangThai()){
                    cbCauHinh.addItem("Cấu hình "+variant.getPhienBan()+1 );
                    maphanloai = variant.getIdVariant();
            }
        }
        // }


       
    }

    private void updateCauhinh(String maSP, String phienBan) {
    int soPhienBan =(Integer.parseInt(phienBan.substring(10))-1);
        System.out.println("Số phiên bản: " + soPhienBan);
        // int soPhienBan = Integer.parseInt(phienBan.substring(10)) - 1;
        // System.out.println("Số phiên bản: " + soPhienBan);
        // String soPhienBan = phienBan.substring(10);.removeAllItems();
        modelchsp.setRowCount(0);
System.out.println(soPhienBan); // Output: 1
        Product product = productDAO.getProductByIdFull(maSP);
        for (Variant variant : product.getDanhSachPhienBan()) {
            System.out.println("Có vào đâykhong");
            System.out.println(variant.getPhienBan());
            if (variant.getPhienBan()==soPhienBan) {
                System.out.println("hiiiii");
                variant.getChitiet().forEach(chitiet -> {
                   if(chitiet instanceof CauHinhPC){
                    System.out.println("cau hinh pc");
                        CauHinhPC cauHinhPC = (CauHinhPC) chitiet;
                        modelchsp.addRow(new Object[]{cauHinhPC.getIdThongTin(),cauHinhPC.getLinhKien().getTenSp()});
                   }
                   else if(chitiet instanceof CauHinhLaptop){
                    System.out.println("cau hinh laptop");
                        CauHinhLaptop cauHinhLaptop = (CauHinhLaptop) chitiet;
                        modelchsp.addRow(new Object[]{cauHinhLaptop.getIdThongTin(),cauHinhLaptop.getThongTin()});
                   }
                });
                break;
            }
        }

    }
private List<String> generateImeiList(String from, String to) {
    List<String> imeiList = new ArrayList<>();
    try {
        long start = Long.parseLong(from);
        long end = Long.parseLong(to);

        for (long i = start; i <= end; i++) {
            imeiList.add(String.valueOf(i));
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "IMEI không hợp lệ! Hãy nhập số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    return imeiList;
}

 private void initCenterPanel() {
    JPanel centerPanel = new JPanel(null);
    centerPanel.setBounds(320, 10, 400, 350);
    centerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

    JLabel lbMaSP = new JLabel("Mã sản phẩm:");
    lbMaSP.setBounds(10, 20, 100, 25);
    tfMaSP = new JTextField();
    tfMaSP.setBounds(120, 20, 250, 25);

    JLabel lbTenSP = new JLabel("Tên sản phẩm:");
    lbTenSP.setBounds(10, 50, 100, 25);
    tfTenSP = new JTextField();
    tfTenSP.setBounds(120, 50, 250, 25);

    JLabel lbCauHinh = new JLabel("Cấu hình:");
    lbCauHinh.setBounds(10, 80, 100, 25);
    cbCauHinh = new JComboBox<>();
    cbCauHinh.setBounds(120, 80, 250, 25);
    

    JLabel lbGia = new JLabel("Giá nhập:");
    lbGia.setBounds(10, 110, 100, 25);
    tfGiaNhap = new JTextField();
    tfGiaNhap.setBounds(120, 110, 250, 25);

    JLabel lbPhuongThuc = new JLabel("Phương thức:");
    lbPhuongThuc.setBounds(10, 140, 100, 25);
    cbPhuongThuc = new JComboBox<>(new String[]{"Nhập từng máy", "Nhập theo lô"});
    cbPhuongThuc.setBounds(120, 140, 250, 25);

    // --- Mã IMEI ---
    JLabel lbImei = new JLabel("Mã IMEI:");
    lbImei.setBounds(10, 170, 100, 25);

     rbTuNhap = new JRadioButton("Tự nhập");
    rbTuNhap.setBounds(120, 170, 100, 25);
    rbTuNhap.setSelected(true);

     rbTheoKhoang = new JRadioButton("Theo khoảng");
    rbTheoKhoang.setBounds(220, 170, 120, 25);

    ButtonGroup imeiGroup = new ButtonGroup();
    imeiGroup.add(rbTuNhap);
    imeiGroup.add(rbTheoKhoang);

    soluong = new JLabel("Số lượng:");
    soluong.setBounds(10, 200, 100, 25);
    soluong.setVisible(false);

    soluongsp = new JTextField();
    soluongsp.setBounds(70, 200, 50, 25);
    soluongsp.setVisible(false);

    tfMaImei = new JTextField();
    tfMaImei.setBounds(120, 200, 250, 25);

    JLabel  lbFrom = new JLabel("Từ:");
    lbFrom.setBounds(120, 200, 30, 25);
    tfImeiFrom = new JTextField();
    tfImeiFrom.setBounds(150, 200, 100, 25);

    JLabel lbTo = new JLabel("Đến:");
    lbTo.setBounds(260, 200, 30, 25);
   tfImeiTo = new JTextField();
    tfImeiTo.setBounds(290, 200, 80, 25);


    Suasp = new JButton("Sửa");
    // Ẩn mặc định
    lbFrom.setVisible(false);
    tfImeiFrom.setVisible(false);
    lbTo.setVisible(false);
    tfImeiTo.setVisible(false);

    // Xử lý hiển thị theo lựa chọn radio button
    rbTuNhap.addActionListener(e -> {
        tfMaImei.setVisible(true);
        lbFrom.setVisible(false);
        tfImeiFrom.setVisible(false);
        lbTo.setVisible(false);
        tfImeiTo.setVisible(false);
        soluongsp.setVisible(false);
        soluong.setVisible(false);
    });

    rbTheoKhoang.addActionListener(e -> {
        tfMaImei.setVisible(false);
        lbFrom.setVisible(true);
        tfImeiFrom.setVisible(true);
        lbTo.setVisible(true);
        tfImeiTo.setVisible(true);
        soluongsp.setVisible(true);
        soluong.setVisible(true);
    });

   DocumentListener listener = new DocumentListener() {
    private boolean isUpdating = false;

    void updateQuantity() {
        try {
            int from = Integer.parseInt(tfImeiFrom.getText());
            int to = Integer.parseInt(tfImeiTo.getText());
            if (to >= from) {
                soluongsp.setText(String.valueOf(to - from + 1));
            }
        } catch (NumberFormatException ignored) {}
    }

    void updateTo() {
        try {
            int from = Integer.parseInt(tfImeiFrom.getText());
            int quantity = Integer.parseInt(soluongsp.getText());
            if (quantity > 0) {
                tfImeiTo.setText(String.valueOf(from + quantity - 1));
            }
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        handle(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handle(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        handle(e);
    }

    void handle(DocumentEvent e) {
        if (isUpdating) return;

        try {
            isUpdating = true;
            Document src = e.getDocument();

            if (src == tfImeiFrom.getDocument()) {
                if (!soluongsp.getText().isEmpty()) {
                    updateTo(); // ưu tiên updateTo nếu số lượng có
                } else if (!tfImeiTo.getText().isEmpty()) {
                    updateQuantity();
                }
            } else if (src == tfImeiTo.getDocument()) {
                if (!tfImeiFrom.getText().isEmpty()) {
                    updateQuantity();
                }
            } else if (src == soluongsp.getDocument()) {
                if (!tfImeiFrom.getText().isEmpty()) {
                    updateTo();
                }
            }
        } finally {
            isUpdating = false;
        }
    }
};

    tfImeiFrom.getDocument().addDocumentListener(listener);
    tfImeiTo.getDocument().addDocumentListener(listener);
    soluongsp.getDocument().addDocumentListener(listener);

    // --- Add tất cả component vào panel ---
    centerPanel.add(lbMaSP); centerPanel.add(tfMaSP);
    centerPanel.add(lbTenSP); centerPanel.add(tfTenSP);
    centerPanel.add(lbCauHinh); centerPanel.add(cbCauHinh);
    centerPanel.add(lbGia); centerPanel.add(tfGiaNhap);
    centerPanel.add(lbPhuongThuc); centerPanel.add(cbPhuongThuc);
    centerPanel.add(lbImei); centerPanel.add(rbTuNhap); centerPanel.add(rbTheoKhoang);
    centerPanel.add(tfMaImei);
    centerPanel.add(lbFrom); centerPanel.add(tfImeiFrom);
    centerPanel.add(lbTo); centerPanel.add(tfImeiTo);
    centerPanel.add(soluong); centerPanel.add(soluongsp);

    add(centerPanel);
}

   private void initRightPanel() {
    JPanel rightSection = new JPanel();
    rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.Y_AXIS));
    rightSection.setBounds(730, 10, 390, 250); // tăng chiều cao tổng thể

    // ===== Chi tiết sản phẩm =====
    JPanel pdDetail = new JPanel(new BorderLayout());
    pdDetail.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));

    String[] columns = {"Thông tin", "Chi tiết"};
    modelchsp = new DefaultTableModel(columns, 0);
    tablechsp = new JTable(modelchsp);

    // Cài đặt độ rộng cột
    TableColumnModel columnModel = tablechsp.getColumnModel();
    int[] columnWidths = {150, 200};
    for (int i = 0; i < columnWidths.length; i++) {
        columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    JScrollPane scrollPane = new JScrollPane(tablechsp);
    scrollPane.setPreferredSize(new Dimension(380, 150)); // Chiều cao cố định

    pdDetail.add(scrollPane, BorderLayout.CENTER);

    // ===== Add tất cả vào rightSection =====
    rightSection.add(pdDetail);
    rightSection.add(Box.createVerticalStrut(10)); // Khoảng cách
    // rightSection.add(rightPanel);
    // ===== Thông tin nhập =====
    JPanel rightPanel = new JPanel(null);
    rightPanel.setBounds(730, 260, 400, 150);
    rightPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhập"));

    tfMaPhieu = new JTextField("PN-1");
    tfMaPhieu.setBounds(120, 20, 250, 25);

    JLabel lbNhanVien = new JLabel("Nhân viên:");
    lbNhanVien.setBounds(10, 60, 100, 25);
    EmployeeDAO employeeDAO = new EmployeeDAO();
    Employee employee = employeeDAO.getEmployeeById(mainFrame.getUser().getIdNhanVien());
    tfNhanVien = new JTextField(employee.getName());
    tfNhanVien.setBounds(120, 60, 250, 25);
    tfNhanVien.setEditable(false);


    JLabel lbNCC = new JLabel("Nhà cung cấp:");
    lbNCC.setBounds(10, 100, 100, 25); 
    SupplierBUS supplierBUS = new SupplierBUS();
    List<Supplier> suppliers = supplierBUS.getAllSuppliers();
      cbNhaCungCap = new JComboBox<>();
    for (Supplier supplier : suppliers) {
        cbNhaCungCap.addItem(supplier.getName());
    }
  
    cbNhaCungCap.setBounds(120, 100, 250, 25);


    rightPanel.add(lbNhanVien); rightPanel.add(tfNhanVien);
    rightPanel.add(lbNCC); rightPanel.add(cbNhaCungCap);
    add(rightSection);
    add(rightPanel);
}

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(null);
        bottomPanel.setBounds(10, 420, 1110, 280);
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        String[] columns = {"STT", "Mã sản phẩm", "Tên sản phẩm", "Mã phân loại", "Đơn giá", "Số lượng"};
        modelChiTiet = new DefaultTableModel(columns, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; 
    }
        };
        tableChiTiet = new JTable(modelChiTiet);

        TableColumnModel columnModel = tableChiTiet.getColumnModel();
        int[] columnWidths = {40, 80, 250, 120, 100, 80};
        for (int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        
        JScrollPane scroll = new JScrollPane(tableChiTiet);
        scroll.setBounds(10, 20, 1080, 180);

        lbTongTien = new JLabel("TỔNG TIỀN: ");
        lbTongTien.setForeground(Color.RED);
        lbTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lbTongTien.setBounds(900, 200, 200, 30);


        submitbutton=new JButton("Nhập hàng");
        submitbutton.setBackground(new Color(0, 123, 255)); // Màu xanh dương hiện đại
        submitbutton.setForeground(Color.WHITE);            // Chữ trắng
        submitbutton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font lớn, dễ nhìn
        submitbutton.setFocusPainted(false);                // Bỏ viền khi nhấn
        submitbutton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Viền padding
        submitbutton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Con trỏ tay khi hover
        submitbutton.setBounds(450, 240, 200, 30);

        submitbutton.addActionListener(e -> {
             xulyNhapHang();
        });

        bottomPanel.add(scroll);
        bottomPanel.add(lbTongTien);
        bottomPanel.add(submitbutton);

        add(bottomPanel);
    }


private void xulyNhapHang() {
//   Lấy từng hàng trong table
// Table hoadonnhap
// table chitietsp
// table chitiethoadonnhap

 
    EmployeeDAO employeeDAO = new EmployeeDAO();
    Employee employee = employeeDAO.getEmployeeById(mainFrame.getUser().getIdNhanVien());
    
    Date sqlDate = new Date(System.currentTimeMillis());

    SupplierBUS supplierBUS = new SupplierBUS();
    Supplier supplier = supplierBUS.getAllSuppliers().get(cbNhaCungCap.getSelectedIndex());
     
    System.out.println("Nhà cung cấp: " + supplier.getName());
    System.out.println("Nhân viên: " + employee.getName());
    
    System.out.println("Mã phân loại: " + maphanloai);
    System.out.println("Bảng hoadonnhap" +employee.getId()+ supplier.getId()+sqlDate+tt);
    System.out.println("Chi tiết sản phẩm");

    HoaDonNhap hdn=new HoaDonNhap(employee, supplier, sqlDate, tt);
    PhieuNhapBUS phieuNhapBUS = new PhieuNhapBUS();
    String idPhieuNhap = phieuNhapBUS.insertHoaDonNhap(hdn);
    System.out.println("Nhập phiếu: "+idPhieuNhap);
    hdn.setIdHoaDonNhap(idPhieuNhap);
    // Chitietsp
for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
    String stt = modelChiTiet.getValueAt(i, 0).toString();
    String maSp = modelChiTiet.getValueAt(i, 1).toString();
    String tenSp = modelChiTiet.getValueAt(i, 2).toString();
    String phanLoai = modelChiTiet.getValueAt(i, 3).toString();
    String giaNhap = modelChiTiet.getValueAt(i, 4).toString();
    String soLuong = modelChiTiet.getValueAt(i, 5).toString();
  
    System.out.println("STT: " + stt + ", Mã SP: " + maSp + ", Tên SP: " + tenSp + ", Phân loại: " + phanLoai + ", Giá nhập: " + giaNhap + ", Số lượng: " + soLuong);
   




    // Nhập chitietsp
 chiTietHDN.forEach((key, value) -> {
         if(key.equals(stt)){   
        value.forEach(productDetail -> {

            System.out.println("Bảng chitietsp: " +productDetail.getSerialNumber() + " " + productDetail.getIdPhanLoai() );
            System.out.println("Bảng chitietdonnhap: "+productDetail.getSerialNumber()+" "+productDetail.getGiaNhap());
            productDetail.setMaPhieuNhap(idPhieuNhap);
            if(phieuNhapBUS.insertChitietSP(productDetail)){
                phieuNhapBUS.insertChitietPhieuNhap(productDetail);
            }
        });
    }
      
    });

}

}



}
