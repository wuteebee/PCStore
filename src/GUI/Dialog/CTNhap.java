package GUI.Dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import BUS.PDFExporter;
import BUS.PhieuNhapBUS;
import BUS.ProductBUS;
import DAO.ProductDAO;
import DTO.HoaDonNhap;
import DTO.Product;
import DTO.ProductDetail;
import GUI.Main;
import GUI.Components.Excel;
import GUI.Panel.PhieuNhapPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CTNhap extends JDialog {
    private JTextField tfMaPhieu, tfNhanVien, tfNhaCungCap, tfThoiGian;
    private JTable tableSanPham, tableIMEI;
    private JButton btnXuatPDF, btnHuy;
    private String maphieunhap;

    public CTNhap(PhieuNhapPanel panel, Main main,String maphieunhap) {
        this.maphieunhap=maphieunhap;
        initComponents();
        
        setVisible(true);
    }

   private void initComponents() {
    setTitle("Thông tin phiếu nhập");
    setSize(1000, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new GridBagLayout()); // SỬ DỤNG GridBagLayout

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);

    // === TITLE ===
    JLabel lblTitle = new JLabel("THÔNG TIN PHIẾU NHẬP", JLabel.CENTER);
    lblTitle.setOpaque(true);
    lblTitle.setBackground(new Color(0, 122, 204));
    lblTitle.setForeground(Color.WHITE);
    lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
    lblTitle.setPreferredSize(new Dimension(1000, 40));

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    add(lblTitle, gbc);

    // === THÔNG TIN CHUNG ===
    JPanel panelThongTin = new JPanel(new GridLayout(2, 4, 10, 10));
    panelThongTin.setBorder(new EmptyBorder(10, 10, 0, 10));


    // Lấy hoá đơn nhập ra
    PhieuNhapBUS phieuNhapBUS=new PhieuNhapBUS();
    HoaDonNhap hdn=phieuNhapBUS.getHoaDonNhapbyId(maphieunhap);
    
    tfMaPhieu = new JTextField(maphieunhap);
    tfNhanVien = new JTextField(hdn.getNhanVien().getName());
    tfNhaCungCap = new JTextField(hdn.getNhaCungCap().getName());
    tfThoiGian = new JTextField(hdn.getNgayTao().toString());

    tfMaPhieu.setEditable(false);
    tfNhanVien.setEditable(false);
    tfNhaCungCap.setEditable(false);
    tfThoiGian.setEditable(false);

    panelThongTin.add(new JLabel("Mã phiếu:"));
    panelThongTin.add(tfMaPhieu);
    panelThongTin.add(new JLabel("Nhân viên nhập:"));
    panelThongTin.add(tfNhanVien);
    panelThongTin.add(new JLabel("Nhà cung cấp:"));
    panelThongTin.add(tfNhaCungCap);
    panelThongTin.add(new JLabel("Thời gian tạo:"));
    panelThongTin.add(tfThoiGian);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    add(panelThongTin, gbc);

    // === BẢNG SẢN PHẨM ===
   Map<String,Integer> Danhsach= phieuNhapBUS.getSoLuongTheoPhanLoai(maphieunhap);
   
    String[] colsSP = {"STT", "Mã SP", "Tên Sản phẩm", "Cấu hình", "Đơn giá", "Số lượng"};
Object[][] dataSP = new Object[Danhsach.size()][6];
int stt = 1;
int index = 0;
ProductDAO productDAO=new ProductDAO();
ProductBUS productBUS=new ProductBUS();
HashMap<String, List<ProductDetail>> map = new HashMap<>();


for (Map.Entry<String, Integer> entry : Danhsach.entrySet()) {
    String idPhanLoai = entry.getKey();
    String masp=productDAO.getProductIDbyMaPhanLoai(Integer.parseInt(idPhanLoai));
    Product product =productBUS.getProductDetail(masp);
    List <ProductDetail> listpd=productBUS.getProductDetailList(Integer.parseInt(idPhanLoai));
    map.put(idPhanLoai, listpd);

   
    int soLuong = entry.getValue();
    // PhanLoaiDTO info = getThongTinPhanLoai(idPhanLoai);
    dataSP[index][0] = stt++; // STT
    dataSP[index][1] = masp;
    dataSP[index][2] = product.getTenSp();
    dataSP[index][3] = productDAO.getphienbanbyIdPL(Integer.parseInt(idPhanLoai));
    dataSP[index][4] = phieuNhapBUS.getGiabySN(listpd.get(0).getSerialNumber());
    dataSP[index][5] = soLuong;
  
    System.out.println("E" +phieuNhapBUS.getGiabySN(listpd.get(0).getSerialNumber()));
    index++;
}


  
    tableSanPham = new JTable(new DefaultTableModel(dataSP, colsSP));
    JScrollPane spSP = new JScrollPane(tableSanPham);
    spSP.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0.5;
    gbc.fill = GridBagConstraints.BOTH;
    add(spSP, gbc);

    // === BẢNG IMEI ===
    String[] colsIMEI = {"STT", "Mã IMEI"};
    Object[][] dataIMEI = {
   
     
    };
    tableIMEI = new JTable(new DefaultTableModel(dataIMEI, colsIMEI));
    JScrollPane spIMEI = new JScrollPane(tableIMEI);
    spIMEI.setBorder(BorderFactory.createTitledBorder("Danh sách IMEI"));

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0.5;
    gbc.fill = GridBagConstraints.BOTH;
    add(spIMEI, gbc);

    // === NÚT XUẤT FILE / HỦY BỎ ===
    JPanel panelButtons = new JPanel();
    btnXuatPDF = new JButton("Xuất file PDF");
    btnHuy = new JButton("Hủy bỏ");

    btnXuatPDF.setBackground(new Color(0, 153, 255));
    btnXuatPDF.setForeground(Color.WHITE);
    btnHuy.setBackground(new Color(204, 0, 0));
    btnHuy.setForeground(Color.WHITE);

    btnXuatPDF.setFocusPainted(false);
    btnHuy.setFocusPainted(false);

    panelButtons.add(btnXuatPDF);
    panelButtons.add(btnHuy);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.NONE;
    add(panelButtons, gbc);

    // === ACTIONS ===
    btnHuy.addActionListener(e -> dispose());
    btnXuatPDF.addActionListener(e -> {
       PDFExporter pdf=new PDFExporter();
        pdf.xuatHoaDon(hdn);
       

     

  
       
    });

tableSanPham.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting() && tableSanPham.getSelectedRow() != -1) {
        int selectedRow = tableSanPham.getSelectedRow();
        String idPhanLoai = null;
        int currentIndex = 0;
        for (String key : map.keySet()) {
            if (currentIndex == selectedRow) {
                idPhanLoai = key;
                break;
            }
            currentIndex++;
        }

if (idPhanLoai != null) {
    List<ProductDetail> listIMEI = map.get(idPhanLoai);
    String[] cols = {"STT", "Mã IMEI"};
    List<Object[]> validRows = new ArrayList<>();
    int dong = 1;

    for (ProductDetail pd : listIMEI) {
        if (pd.getMaPhieuNhap().equals(maphieunhap)) {
            validRows.add(new Object[]{dong++, pd.getSerialNumber()});
        }
    }

    Object[][] data = new Object[validRows.size()][2];
    for (int i = 0; i < validRows.size(); i++) {
        data[i] = validRows.get(i);
    }

    DefaultTableModel model = new DefaultTableModel(data, cols);
    tableIMEI.setModel(model);
}

    }
});

}

}
