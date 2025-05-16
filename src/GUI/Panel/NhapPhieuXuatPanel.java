package GUI.Panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;

import BUS.CustomerBUS;
import BUS.InvoiceBUS;
import BUS.PhieuNhapBUS;
import BUS.ProductBUS;
import BUS.PromotionBUS;
import BUS.SupplierBUS;
import DAO.EmployeeDAO;
import DAO.PhieuNhapDAO;
import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.Customer;
import DTO.Employee;
import DTO.Product;
import DTO.ProductDetail;
import DTO.Promotion;
import DTO.SalesInvoice;
import DTO.Supplier;
import DTO.Variant;
import GUI.Main;
import java.awt.*;
import java.text.DecimalFormat;

public class NhapPhieuXuatPanel extends JPanel{
       private JTable tableSp, tableChiTiet,tablechsp;
    private DefaultTableModel modelSp, modelChiTiet,modelchsp;
    private JTextField tfMaSP, tfTenSP, tfGiaXuat, tfMaPhieu, tfNhanVien, soluongsp;
    private JComboBox<String> cbCauHinh;
    private JLabel lbTongTien,soluong;
    private JButton Suasp, Xoasp, Luu, Huy,btnThem,submitbutton;
    private List<JCheckBox> checkBoxes;
      JComboBox<String> cbKhachHang;
    private Main mainFrame;
    private ProductDAO productDAO;
    private List<Product> products;
     private double tt;
    private HashMap<String, List<ProductDetail>> chiTietHDX = new HashMap<>();
    private Set<String> selectedIMEIs = new HashSet<>();
    private Promotion promotion ;
      public NhapPhieuXuatPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(java.awt.Color.white);
        setLayout(null);
        setBounds(0, 0, 1150, 700);
        productDAO=new ProductDAO();
        this.products=productDAO.getAllProducts();
   
        initLeftPanel();
        initCenterPanel();
        cbCauHinh.addActionListener(e -> {
            String maSP = tfMaSP.getText(); 
            String selectedCauHinh = (String) cbCauHinh.getSelectedItem(); // Giá trị đã chọn
            if (maSP != null && !maSP.isEmpty() && selectedCauHinh != null) {
                updateCauhinh(maSP, selectedCauHinh); // Gọi lại hàm cập nhật cấu hình
            }
        });

        initRightPanel();
        initBottomPanel();
        actionTable();

    }

    public NhapPhieuXuatPanel(){}

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
    header.setBackground(new java.awt.Color(220, 220, 220));
    header.setFont(new Font("Segoe UI", Font.BOLD, 12));

    JScrollPane scroll = new JScrollPane(tableSp);
    scroll.setBounds(10, 50, 280, 250);

   btnThem = new JButton("Thêm");
    btnThem.setBounds(10, 310, 130, 30);
    JButton btnNhapExcel = new JButton("Nhập Excel");
    btnNhapExcel.setBounds(150, 310, 130, 30);

  
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



            updateCenterPanel(maSp,null);
            updateCauhinh(maSp,cbCauHinh.getSelectedItem().toString()); 
        
        }
    });

    leftPanel.add(txtTimKiem);
    leftPanel.add(scroll);
    leftPanel.add(btnThem);
    leftPanel.add(btnNhapExcel);

    add(leftPanel);
    btnThem.addActionListener(e -> {
        updatePhieuXuat();
    });
}


  private void updatePhieuXuat() {
    String maSP = tfMaSP.getText();
    String tenSP = tfTenSP.getText();
    String pb = cbCauHinh.getSelectedItem().toString();
    int phienBanSo = Integer.parseInt(pb.replaceAll("\\D+", ""));
    String giaXuat = tfGiaXuat.getText();
    
    // Xóa dữ liệu trường nhập
    tfMaSP.setText("");
    tfTenSP.setText("");
    cbCauHinh.setSelectedIndex(0);
    tfGiaXuat.setText("");
    soluongsp.setText("");

    int sl = 0;
    ProductDAO productDAO = new ProductDAO();
    int maphanloai = productDAO.getIDPhanLoai(maSP, phienBanSo - 1);
    List<ProductDetail> list = new ArrayList<>();
    
    // Đếm số lượng IMEI được chọn và tạo ProductDetail
    for (JCheckBox cb : checkBoxes) {
        if (cb.isSelected() && cb.isEnabled()) {
            System.out.println("IMEI: " + cb.getText());
            sl += 1;
            ProductDetail productDetail = new ProductDetail(cb.getText(), maphanloai, Double.parseDouble(giaXuat), true);
            list.add(productDetail);
        }
    }

    if (sl == 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một IMEI!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int stt = modelChiTiet.getRowCount() + 1;
    chiTietHDX.put(String.valueOf(stt), list);

    modelChiTiet.addRow(new Object[]{
        stt,
        maSP,
        tenSP,
        phienBanSo,
        String.format("%,d", Integer.parseInt(giaXuat)).replace(",", "."),
        sl
    });

    updateTongTien(null);
}
  private void searchProducts(String query) {
    // Xóa hết các dòng cũ trong bảng
    modelSp.setRowCount(0);

    // Lấy danh sách sản phẩm từ cơ sở dữ liệu, lọc theo từ khóa tìm kiếm
    
    for (Product product : products) {
        if(product.getTenSp().toLowerCase().contains(query.toLowerCase())) {
            modelSp.addRow(new Object[]{product.getMaSp(), product.getTenSp()});
        }
    }
}

private void updateCenterPanel(String maSP, Integer phienBan) {
    
    Product product = productDAO.getProductByIdFull(maSP);
    
    tfMaSP.setText(product.getMaSp());
    tfTenSP.setText(product.getTenSp());
    
    cbCauHinh.removeAllItems();
    
    List<Variant> danhSach = product.getDanhSachPhienBan();
    int selectedIndex = -1;
    
    for (int i = 0, j = 0; i < danhSach.size(); i++) {
        Variant variant = danhSach.get(i);
        if (variant.isTrangThai()) {
            cbCauHinh.addItem("Cấu hình " + (variant.getPhienBan() + 1));
            
            if (phienBan != null && variant.getPhienBan() == phienBan) {
                selectedIndex = j; // lưu lại index trong combobox
            }
            j++; // chỉ tăng khi variant được add vào combobox
        }
    }

    if (selectedIndex != -1) {
        cbCauHinh.setSelectedIndex(selectedIndex);
        System.out.println("Combo cấu hình nè: "+selectedIndex);
        
    }
}

private void updateCauhinh(String maSP, String phienBan) {
        System.out.println("Phiên bản  nè:  "+phienBan);
         String[] parts = phienBan.trim().split(" ");
        int soPhienBan = Integer.parseInt(parts[2]) - 1;
        System.out.println("Số phiên bản: " + soPhienBan);
        // int soPhienBan = Integer.parseInt(phienBan.substring(10)) - 1;
        // System.out.println("Số phiên bản: " + soPhienBan);
        // String soPhienBan = phienBan.substring(10);.removeAllItems();
        modelchsp.setRowCount(0);
        System.out.println(soPhienBan); // Output: 1
        Product product = productDAO.getProductByIdFull(maSP);
        for (Variant variant : product.getDanhSachPhienBan()) {
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

        DecimalFormat df = new DecimalFormat("#.###");
        tfGiaXuat.setText(df.format(variant.getGia()));
        tfGiaXuat.setEditable(false);
                break;
            }
        }

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
    

    JLabel lbGia = new JLabel("Giá xuất:");
    lbGia.setBounds(10, 110, 100, 25);
    tfGiaXuat = new JTextField();
    tfGiaXuat.setBounds(120, 110, 250, 25);



    // --- Mã IMEI ---
    JLabel lbImei = new JLabel("Mã IMEI:");
    lbImei.setBounds(10, 175, 100, 25);
    
JButton selectImei = new JButton("Chọn IMEI");
selectImei.setBackground(Color.BLUE);         // Nền xanh
selectImei.setForeground(Color.WHITE);        // Chữ trắng
selectImei.setBounds(130,175,120,30);


    soluong = new JLabel("Số lượng tồn:");
    soluong.setBounds(10, 150, 100, 25);
    soluong.setVisible(true);

    soluongsp = new JTextField();
    soluongsp.setBounds(130, 150, 50, 25);
    soluongsp.setVisible(true);


Suasp = new JButton("Sửa");
Suasp.setBounds(10, 300, 100, 30);
Suasp.setBackground(new Color(255, 165, 0)); // Cam
Suasp.setForeground(Color.WHITE);     
Suasp.addActionListener(e->{
    System.out.println("Nhấn nút sửa nè");
    // xulySua();
});

Xoasp = new JButton("Xoá");
Xoasp.setBounds(120, 300, 100, 30);
Xoasp.setBackground(Color.RED);             // Đỏ
Xoasp.setForeground(Color.WHITE);           // Chữ trắng

selectImei.addActionListener(e->{
     themImei();
});



    // --- Add tất cả component vào panel ---
    centerPanel.add(lbMaSP); centerPanel.add(tfMaSP);
    centerPanel.add(lbTenSP); centerPanel.add(tfTenSP);
    centerPanel.add(lbCauHinh); centerPanel.add(cbCauHinh);
    centerPanel.add(lbGia); centerPanel.add(tfGiaXuat);
    // centerPanel.add(lbPhuongThuc); centerPanel.add(cbPhuongThuc);
    centerPanel.add(lbImei); 
    // centerPanel.add(tfMaImei);
    // centerPanel.add(lbFrom); centerPanel.add(tfImeiFrom);
    // centerPanel.add(lbTo); centerPanel.add(tfImeiTo);
    centerPanel.add(soluong); centerPanel.add(soluongsp);
    centerPanel.add(Suasp);
    centerPanel.add(Xoasp);
    centerPanel.add(selectImei);

    add(centerPanel);


}

private void themImei() {
    // Tạo JDialog
    JDialog dialog = new JDialog((Frame) null, "Chọn IMEI", true);
    dialog.setSize(300, 300);
    dialog.setLocationRelativeTo(null); // Giữa màn hình
    dialog.setLayout(new BorderLayout());

    // Panel chứa checkbox
    JPanel checkboxPanel = new JPanel();
    checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));

    InvoiceBUS bus = new InvoiceBUS();
    ProductDAO productDAO = new ProductDAO();
    String maSP = tfMaSP.getText();
    String selectedCauHinh = (String) cbCauHinh.getSelectedItem();
    int phienBanSo = Integer.parseInt(selectedCauHinh.replaceAll("\\D+", ""));
    List<String> imeiList = bus.getSNbyidPL(productDAO.getIDPhanLoai(tfMaSP.getText(), phienBanSo - 1) + "");
    
    checkBoxes = new ArrayList<>();

    // Lọc và thêm checkbox cho các IMEI chưa được chọn
    for (String imei : imeiList) {
        if (!selectedIMEIs.contains(imei)) { // Chỉ thêm IMEI chưa được chọn
            JCheckBox cb = new JCheckBox(imei);
            checkBoxes.add(cb);
            checkboxPanel.add(cb);
        } else {
            // Tùy chọn: Thêm checkbox nhưng vô hiệu hóa
            JCheckBox cb = new JCheckBox(imei);
            cb.setEnabled(false); // Vô hiệu hóa checkbox
            cb.setSelected(true); // Đánh dấu là đã chọn
            checkBoxes.add(cb);
            checkboxPanel.add(cb);
        }
    }

    // Cuộn nếu nhiều checkbox
    JScrollPane scrollPane = new JScrollPane(checkboxPanel);
    dialog.add(scrollPane, BorderLayout.CENTER);

    // Nút xác nhận
    JButton confirmButton = new JButton("Xác nhận");
    confirmButton.addActionListener(e -> {
        System.out.println("IMEI đã chọn:");
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected() && cb.isEnabled()) { // Chỉ thêm IMEI mới chọn
                System.out.println(cb.getText());
                selectedIMEIs.add(cb.getText()); // Lưu IMEI vào danh sách đã chọn
            }
        }
        dialog.dispose(); // Đóng dialog
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(confirmButton);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.setVisible(true); // Hiển thị dialog
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

    // ===== Thông tin nhập =====
    JPanel rightPanel = new JPanel(null);
    rightPanel.setBounds(730, 260, 400, 200); // Tăng chiều cao để chứa thêm khách hàng

    tfMaPhieu = new JTextField("PN-1");
    tfMaPhieu.setBounds(120, 20, 250, 25);

    JLabel lbNhanVien = new JLabel("Nhân viên:");
    lbNhanVien.setBounds(10, 60, 100, 25);
    EmployeeDAO employeeDAO = new EmployeeDAO();
    Employee employee = employeeDAO.getEmployeeById(mainFrame.getUser().getIdNhanVien());
    tfNhanVien = new JTextField(employee.getName());
    tfNhanVien.setBounds(120, 60, 250, 25);
    tfNhanVien.setEditable(false);

    // Thêm trường khách hàng
    JLabel lbKhachHang = new JLabel("Khách hàng:");
    lbKhachHang.setBounds(10, 100, 100, 25);
    cbKhachHang = new JComboBox<>();
    cbKhachHang.setBounds(120, 100, 250, 25);

    // Lấy danh sách khách hàng từ CustomerBUS
    CustomerBUS customerBUS = new CustomerBUS();
    List<Customer> customers = customerBUS.getAllCustomers();
    for (Customer customer : customers) {
        cbKhachHang.addItem(customer.getName());
    }

    JLabel lbNCC = new JLabel("Nhà cung cấp:");
    lbNCC.setBounds(10, 140, 100, 25); 
    SupplierBUS supplierBUS = new SupplierBUS();
    // Bạn có thể thêm JComboBox cho nhà cung cấp nếu cần
    // Ví dụ: JComboBox<String> cbNCC = new JComboBox<>();
    // cbNCC.setBounds(120, 140, 250, 25);
    // List<Supplier> suppliers = supplierBUS.getAllSuppliers();
    // for (Supplier supplier : suppliers) {
    //     cbNCC.addItem(supplier.getTenNhaCungCap());
    // }

    rightPanel.add(lbNhanVien); 
    rightPanel.add(tfNhanVien);
    rightPanel.add(lbKhachHang); 
    rightPanel.add(cbKhachHang);
    rightPanel.add(lbNCC);

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

    submitbutton = new JButton("Xuất kho");
    submitbutton.setBackground(new Color(0, 123, 255));
    submitbutton.setForeground(Color.WHITE);
    submitbutton.setFont(new Font("Segoe UI", Font.BOLD, 16));
    submitbutton.setFocusPainted(false);
    submitbutton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    submitbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    submitbutton.setBounds(450, 240, 200, 30);


    JLabel lblMaKhuyenMai = new JLabel("Mã khuyến mãi:");

   PromotionBUS promotionbus=new PromotionBUS();
 JComboBox<String> cbMaKhuyenMai = new JComboBox<>();
    lblMaKhuyenMai.setBounds(10, 200, 120, 25);
    cbMaKhuyenMai.setBounds(130, 200, 150, 25);
Map<String, Promotion> mapTenToMa = new HashMap<>();

List<Promotion> promotions = promotionbus.getAllPromotions();
for (Promotion tmp : promotions) {
    cbMaKhuyenMai.addItem(tmp.getTenKhuyenMai());  // thêm tên vào ComboBox
    mapTenToMa.put(tmp.getTenKhuyenMai(),tmp); // lưu mapping tên -> mã
}
submitbutton.addActionListener(e -> {
    String tenKM = (String) cbMaKhuyenMai.getSelectedItem();
    String maKM = mapTenToMa.get(tenKM).getIdKhuyenMai();

    try {
        int maKMInt = Integer.parseInt(maKM); 
        System.out.println("Xuất kho với mã khuyến mãi: " + maKMInt);
        // xulyXuatHang();
        // updateTongTien(maKMInt);  
    } catch (NumberFormatException ex) {
        System.err.println("Mã khuyến mãi không hợp lệ: " + maKM);
        // xử lý lỗi ở đây nếu cần
    }
});



    bottomPanel.add(scroll);
    bottomPanel.add(lbTongTien);
    bottomPanel.add(submitbutton);

    bottomPanel.add(lblMaKhuyenMai);
    bottomPanel.add(cbMaKhuyenMai);


    cbMaKhuyenMai.addActionListener(e -> {
    String selectedKM = (String) cbMaKhuyenMai.getSelectedItem();
    System.out.println("Mã khuyến mãi được chọn: " + selectedKM);
double maKMDouble = Double.parseDouble(mapTenToMa.get(selectedKM).getGiaTri()+"");
int maKMInt = (int) maKMDouble;  
updateTongTien(maKMInt);
    


});

    add(bottomPanel);
}


private void actionTable(){
    tableChiTiet.getSelectionModel().addListSelectionListener(e -> {
         int idphanloai = 0;
         
          if (!e.getValueIsAdjusting()) {
            
            int selectedRow = tableChiTiet.getSelectedRow();
            if (selectedRow != -1) {
                Object value = tableChiTiet.getValueAt(selectedRow, 4);
                tfGiaXuat.setText(value.toString());
            }
             for (ProductDetail tmp : chiTietHDX.get((selectedRow + 1) + "") ) {
                System.out.println(tmp.getSerialNumber());
                idphanloai = tmp.getIdPhanLoai();
            }
            ProductBUS productBUS = new ProductBUS();
            String masp = productBUS.getmaSPbyIdPL(idphanloai);
            int phienBan = productBUS.getphienbanbyIdPL(idphanloai);

            System.out.println("Mã phân loại nè: " + idphanloai);
            System.out.println("Mã sp nè: " + masp);


            
            updateCenterPanel(masp, phienBan);
  
            updateCauhinh(masp,cbCauHinh.getSelectedItem().toString());



         }
    });
}

private void updateTongTien(Integer KM) {
    long tongTien = 0;

    for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
        String giaStr = modelChiTiet.getValueAt(i, 4).toString().replace(".", "");
        int gia = Integer.parseInt(giaStr);
        int soLuong = Integer.parseInt(modelChiTiet.getValueAt(i, 5).toString());

        tongTien += gia * soLuong;
    }

    tt = tongTien;
    System.out.println("Khuyến mãi nè: " + KM);
    System.out.println("Tiền trc: " + tt);

    if (KM != null) {
        tt = tt - (tt * KM / 100);
    }

    System.out.println("Tiền sau khuyến mãi: " + tt);

String tongTienFormatted = String.format("%,d", (long) tt).replace(",", ".");

    lbTongTien.setText("TỔNG TIỀN: " + tongTienFormatted + "đ");
}

// private void xulyXuatHang() {
//     EmployeeDAO employeeDAO = new EmployeeDAO();
//     Employee employee = employeeDAO.getEmployeeById(mainFrame.getUser().getIdNhanVien());

//     CustomerBUS customerBUS = new CustomerBUS();

//     int selectedCustomerIndex = cbKhachHang.getSelectedIndex();
//     Customer customer = customerBUS.getAllCustomers().get(selectedCustomerIndex);

//     java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

//     System.out.println("Khách hàng: " + customer.getName());
//     System.out.println("Nhân viên: " + employee.getName());
//     System.out.println("Tổng tiền: " + tt);

//     SalesInvoice hdx = new SalesInvoice( employee, customer, sqlDate, tt, null);
//     PhieuXuatBUS phieuXuatBUS = new PhieuXuatBUS();
//     String idPhieuXuat = phieuXuatBUS.insertHoaDonXuat(hdx);
//     System.out.println("Phiếu xuất: " + idPhieuXuat);
//     hdx.setIdHoaDonXuat(idPhieuXuat);

//     for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
//         String stt = modelChiTiet.getValueAt(i, 0).toString();
//         String maSp = modelChiTiet.getValueAt(i, 1).toString();
//         String tenSp = modelChiTiet.getValueAt(i, 2).toString();
//         String phanLoai = modelChiTiet.getValueAt(i, 3).toString();
//         String giaBan = modelChiTiet.getValueAt(i, 4).toString();
//         String soLuong = modelChiTiet.getValueAt(i, 5).toString();

//         System.out.println("STT: " + stt + ", Mã SP: " + maSp + ", Tên SP: " + tenSp + ", Phân loại: " + phanLoai + ", Giá bán: " + giaBan + ", Số lượng: " + soLuong);

//         // Giảm tồn kho
//         phieuXuatBUS.updateSLTK(chiTietHDX.get(stt).get(0).getIdPhanLoai() + "", -Integer.parseInt(soLuong));

//         // Thêm chi tiết xuất hàng
//         chiTietHDX.forEach((key, value) -> {
//             if (key.equals(stt)) {
//                 value.forEach(productDetail -> {
//                     if (!selectedIMEIs.contains(productDetail.getSerialNumber())) {
//                         System.err.println("IMEI không hợp lệ: " + productDetail.getSerialNumber());
//                         return;
//                     }
//                     System.out.println("Bảng chitietsp: " + productDetail.getSerialNumber() + " " + productDetail.getIdPhanLoai());
//                     System.out.println("Bảng chitietdonxuat: " + productDetail.getSerialNumber() + " " + productDetail.getGiaBan());
//                     productDetail.setMaPhieuXuat(idPhieuXuat);

//                     if (phieuXuatBUS.insertChitietSP(productDetail)) {
//                         phieuXuatBUS.insertChitietPhieuXuat(productDetail);
//                     }
//                 });
//             }
//         });
//     }

//     JOptionPane.showMessageDialog(null, "Xuất hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//     resetSelectedIMEIs(); // Reset danh sách IMEI đã chọn sau khi xuất thành công
//     mainFrame.setMainPanel(new PhieuXuatPanel(mainFrame));
// }







}
