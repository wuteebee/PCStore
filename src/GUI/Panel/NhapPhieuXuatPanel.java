package GUI.Panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
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

import BUS.InvoiceBUS;
import BUS.PhieuNhapBUS;
import BUS.ProductBUS;
import BUS.SupplierBUS;
import DAO.EmployeeDAO;
import DAO.PhieuNhapDAO;
import DAO.ProductDAO;
import DTO.CauHinhLaptop;
import DTO.CauHinhPC;
import DTO.Employee;
import DTO.Product;
import DTO.ProductDetail;
import DTO.Supplier;
import DTO.Variant;
import GUI.Main;
import java.awt.*;
import java.text.DecimalFormat;

public class NhapPhieuXuatPanel extends JPanel{
       private JTable tableSp, tableChiTiet,tablechsp;
    private DefaultTableModel modelSp, modelChiTiet,modelchsp;
    private JTextField tfMaSP, tfTenSP, tfGiaXuat, tfMaPhieu, tfNhanVien, tfMaImei,soluongsp;
    private JComboBox<String> cbCauHinh;
    private JLabel lbTongTien,soluong,lbFrom,lbTo;
    private JButton Suasp, Xoasp, Luu, Huy,btnThem,submitbutton;
    private JRadioButton rbTuNhap, rbTheoKhoang;
    private List<JCheckBox> checkBoxes;
    private Main mainFrame;
    private ProductDAO productDAO;
    private List<Product> products;
    private HashMap<String, List<ProductDetail>> chiTietHDX = new HashMap<>();
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
        // actionTable();

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


  private void updatePhieuXuat(){
    String maSP = tfMaSP.getText();
    String tenSP = tfTenSP.getText();
    String pb= cbCauHinh.getSelectedItem().toString();
    int phienBanSo = Integer.parseInt(pb.replaceAll("\\D+", "")) ;
    String giaXuat = tfGiaXuat.getText();
    tfMaSP.setText("");
    tfTenSP.setText("");
    cbCauHinh.setSelectedIndex(0);
    tfGiaXuat.setText("");
    soluongsp.setText("");
    int sl=0;
    ProductDAO productDAO=new ProductDAO();
    int maphanloai=productDAO.getIDPhanLoai(maSP, phienBanSo-1);
    List<ProductDetail> list =new ArrayList<>();
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                System.out.println(cb.getText());
                sl+=1;
            }
            ProductDetail productDetail=new ProductDetail(cb.getText(),maphanloai,Double.parseDouble(giaXuat),true);
            list.add(productDetail);

        }
          int stt = modelChiTiet.getRowCount() + 1;
        chiTietHDX.put(String.valueOf(stt), list);
    // tfImeiFrom.setText("");
    // tfImeiTo.setText("");
    



   modelChiTiet.addRow(new Object[]{
    modelChiTiet.getRowCount()+1,
    maSP,
    tenSP,
    phienBanSo,
    String.format("%,d", Integer.parseInt(giaXuat)).replace(",", "."),
    sl
    
   });
   
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

    InvoiceBUS bus=new InvoiceBUS();
    PhieuNhapBUS phieuNhapBUS=new PhieuNhapBUS();
  
    ProductDAO productDAO=new ProductDAO();
                String maSP = tfMaSP.getText(); 
     String selectedCauHinh = (String) cbCauHinh.getSelectedItem();
         int phienBanSo = Integer.parseInt(selectedCauHinh.replaceAll("\\D+", "")) ;
    List<String> imeiList=bus.getSNbyidPL((productDAO.getIDPhanLoai(tfMaSP.getText(), phienBanSo-1))+"");
    checkBoxes = new ArrayList<>();

    for (String imei : imeiList) {
        JCheckBox cb = new JCheckBox(imei);
        checkBoxes.add(cb);
        checkboxPanel.add(cb);
    }

    // Cuộn nếu nhiều checkbox
    JScrollPane scrollPane = new JScrollPane(checkboxPanel);
    dialog.add(scrollPane, BorderLayout.CENTER);

    // Nút xác nhận
    JButton confirmButton = new JButton("Xác nhận");
    confirmButton.addActionListener(e -> {
        System.out.println("IMEI đã chọn:");
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                System.out.println(cb.getText());
        
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
   

    rightPanel.add(lbNhanVien); rightPanel.add(tfNhanVien);

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


        submitbutton=new JButton("Xuất kho");
        submitbutton.setBackground(new Color(0, 123, 255)); // Màu xanh dương hiện đại
        submitbutton.setForeground(Color.WHITE);            // Chữ trắng
        submitbutton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font lớn, dễ nhìn
        submitbutton.setFocusPainted(false);                // Bỏ viền khi nhấn
        submitbutton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Viền padding
        submitbutton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Con trỏ tay khi hover
        submitbutton.setBounds(450, 240, 200, 30);


        
        submitbutton.addActionListener(e -> {
            System.out.println("Xử lý xuất hàng nè");
            //  xulyNhapHang();
        });

        bottomPanel.add(scroll);
        bottomPanel.add(lbTongTien);
        bottomPanel.add(submitbutton);

        add(bottomPanel);
    }



    
    
}
