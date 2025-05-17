package GUI.Dialog;

import BUS.*;
import DAO.EmployeeDAO;
import DAO.ProductDAO;
import DTO.*;
import GUI.Main;
import GUI.Panel.SaleInvoicePanel;
import org.apache.xmlbeans.impl.soap.Detail;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhieuXuatDialog extends JDialog {
    private boolean flag = false;
    private int mode;

    private String selectedID;
    private SaleInvoicePanel panel;
    private JComboBox idNhanVien;
    private JComboBox idKhachHang;
    private JLabel tongTien;
    private JFormattedTextField ngayTao;
    private JComboBox idKhuyenMai;
    private JComboBox tenSanPham = new JComboBox();
    private PhieuXuatDialog1 serialNumber;
    private JButton chonSoSeri = new JButton("Chọn số seri");
    private JButton xacNhanThem = new JButton("Thêm");
    private JButton xacNhanXoa = new JButton("Xóa");
    private JTable sanPhamDaThem;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JButton xacNhanTao = new JButton();
    private JButton huy = new JButton("Hủy");

    private Font text = new Font("Segoe UI", Font.PLAIN, 15);
    private Font title = new Font("Segoe UI", Font.BOLD, 15);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private int selectedRow = -1;
    private JLabel titleNhanVien = new JLabel("Tên nhân viên");
    private JLabel titleKhachHang = new JLabel("Tên khách hàng");
    private JLabel titleNgayTao = new JLabel("Ngày tạo đơn");
    private JLabel titleKM = new JLabel("Khuyến mãi");
    private JLabel titleSP = new JLabel("Thông tin sản phẩm");
    private JLabel titleSN = new JLabel("Số seri");
    private JLabel titleTable = new JLabel("Danh sách sản phẩm được thêm");

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private EmployeeBUS employeeBus = new EmployeeBUS(employeeDAO);
    private CustomerBUS customerBUS = new CustomerBUS();
    private PromotionBUS promoBUS = new PromotionBUS();
    private ProductDAO productDAO= new ProductDAO();
    private ProductBUS productBUS = new ProductBUS();
    private InvoiceBUS invoiceBUS = new InvoiceBUS();

    private List<Employee> employeeList;
    private List<Customer> customerList;
    private List<Promotion> promoList;
    private List <ProductDetail> productDetailList;
    private List<Product> productList;
    private List<SalesInvoice> existingList;

    private List<DetailedSalesInvoice> addedList = new ArrayList<>();
    private double total = 0;

    public PhieuXuatDialog(Main Frame, SaleInvoicePanel panel, int mode, String selectedID) {
        super(Frame, "Thêm hóa đơn xuất", true);
        this.panel = panel;
        this.mode = mode;
        this.selectedID = selectedID;
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(Color.white);
        initDialog();
        actionProcessing();
    }

    private void initDialog() {
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.fill = GridBagConstraints.BOTH;

        switch (mode)
        {
            case 0:
                xacNhanTao.setText("Tạo");
                break;
            case 1:
                xacNhanTao.setText("Sửa");
                break;
            case 2:
                xacNhanTao.setVisible(false);
                huy.setText("Trở về");
                xacNhanThem.setVisible(false);
                xacNhanXoa.setVisible(false);
                break;
        }

        existingList = invoiceBUS.fetchSalesInvoice();
        //Init field nhân viên
        employeeList = employeeBus.getAllEmployees();
        String[] employeeName = {"Nhập tên nhân viên"};
        int n = 1;
        for (Employee e : employeeList) {
            employeeName = Arrays.copyOf(employeeName, n + 1);
            employeeName[n] = e.getName();
            n++;
        }
        n -= 1;  //Giảm do dư một index
        employeeName = Arrays.copyOf(employeeName, n);
        idNhanVien = new JComboBox(employeeName);
        idNhanVien.setEditable(true);
        AutoCompleteDecorator.decorate(idNhanVien);

        //Init field khach hang
        customerList = customerBUS.getAllCustomers();
        String[] customerName = {"Nhập tên khách hàng"};
        int n1 = 1;
        for (Customer e : customerList) {
            customerName = Arrays.copyOf(customerName, n1 + 1);
            customerName[n1] = e.getName();
            n1++;
        }
        idKhachHang = new JComboBox(customerName);
        idKhachHang.setEditable(true);
        AutoCompleteDecorator.decorate(idKhachHang);

        //init ngay tao
        MaskFormatter maskFormatter;
        try {
            maskFormatter = new MaskFormatter("##/##/####");
            maskFormatter.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ngayTao = new JFormattedTextField(maskFormatter);
        ngayTao.setHorizontalAlignment(JFormattedTextField.CENTER);

        //Init field khuyen mai
        promoList = promoBUS.getAllPromotions();
        String[] promoName = {"Không áp dụng"};
        int n2 = 1;
        for (Promotion e : promoList) {
            promoName = Arrays.copyOf(promoName, n2 + 1);
            promoName[n2] = e.getTenKhuyenMai();
            n2++;
        }
        idKhuyenMai = new JComboBox(promoName);
        idKhuyenMai.setEditable(true);
        AutoCompleteDecorator.decorate(idKhuyenMai);

        //Init combobox sanPham
        productDetailList = productBUS.getProductDetailForInvoice();
        List<String> tenSP = new ArrayList<String>();
        tenSP.add("Nhập tên sản phẩm");
        int n4 = 1;
        for (ProductDetail e : productDetailList) {
            String name = productBUS.getNamebyIdPL(e.getIdPhanLoai());
            if (!tenSP.contains(name)) {
                tenSP.add(name);
                n4++;
            }
        }
        tenSanPham = new JComboBox(tenSP.toArray());
        tenSanPham.setEditable(true);
        AutoCompleteDecorator.decorate(tenSanPham);

        //Init Border
        idNhanVien.setBorder(new RoundedBorder());
        idKhachHang.setBorder(new RoundedBorder());
        ngayTao.setBorder(new RoundedBorder());
        idKhuyenMai.setBorder(new RoundedBorder());
        tenSanPham.setBorder(new RoundedBorder());
        xacNhanThem.setBorder(new RoundedBorder());
        xacNhanXoa.setBorder(new RoundedBorder());
        chonSoSeri.setBorder(new RoundedBorder());
        xacNhanTao.setBorder(new RoundedBorder());
        huy.setBorder(new RoundedBorder());

        //Init font
        idNhanVien.setFont(text);
        idKhachHang.setFont(text);
        ngayTao.setFont(text);
        idKhuyenMai.setFont(text);
        tenSanPham.setFont(text);
        xacNhanThem.setFont(text);
        xacNhanXoa.setFont(text);
        xacNhanTao.setFont(text);
        chonSoSeri.setFont(text);
        huy.setFont(text);

        titleNhanVien.setFont(title);
        titleKhachHang.setFont(title);
        titleNgayTao.setFont(title);
        titleKM.setFont(title);
        titleSP.setFont(title);
        titleSN.setFont(title);
        titleTable.setFont(title);

        //Init layout
        grid.insets = new Insets(0, 15, 5, 15);
        grid.gridy = 0;
        add(titleNhanVien, grid);
        add(titleKhachHang, grid);
        add(titleNgayTao, grid);
        grid.gridwidth = 2;
        add(titleKM, grid);

        grid.insets = new Insets(20, 15, 5, 15);
        grid.gridy = 3;
        grid.gridwidth = 2;
        add(titleSP, grid);
        grid.gridwidth = 1;
        add(titleSN, grid);

        grid.gridy = 5;
        add(titleTable,grid);

        grid.insets = new Insets(0, 15, 0, 15);
        grid.ipady = 20;
        grid.gridy = 2;
        grid.weightx = 0.35;
        add(idNhanVien, grid);
        grid.weightx = 0.35;
        add(idKhachHang, grid);
        grid.weightx = 0.1;
        add(ngayTao, grid);
        grid.weightx = 0.2;
        grid.gridwidth = 2;
        add(idKhuyenMai, grid);

        grid.weightx = 0.7;
        grid.gridy = 4;
        add(tenSanPham, grid);
        grid.gridwidth = 1;
        grid.weightx = 0.1;
        add(chonSoSeri, grid);
        add(xacNhanThem, grid);
        add(xacNhanXoa, grid);

        grid.ipady = 0;
        grid.gridy = 6;
        grid.gridwidth = 5;
        grid.weightx = 1;

        String[] columns = {"STT", "So seri", "Tên sản phẩm", "Đơn giá"};
        tableModel = new DefaultTableModel(columns, 0);
        sanPhamDaThem = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < sanPhamDaThem.getColumnCount(); i++) {
            sanPhamDaThem.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        sanPhamDaThem.getColumnModel().getColumn(2).setPreferredWidth(700);
        sanPhamDaThem.setShowHorizontalLines(true);

        sanPhamDaThem.setFillsViewportHeight(true);
        sanPhamDaThem.setRowHeight(30);
        sanPhamDaThem.getTableHeader().setReorderingAllowed(false);
        scrollPane = new JScrollPane(sanPhamDaThem);
        scrollPane.setBorder(new RoundedBorder());
        add(scrollPane, grid);

        grid.insets = new Insets(20, 15, 0, 15);
        grid.ipady = 20;
        grid.gridy = 7;
        grid.gridx = 3;
        grid.gridwidth = 1;
        grid.weightx = 0.1;
        add(xacNhanTao, grid);
        grid.gridx = 4;
        add(huy, grid);

//        if (mode == 1) {
//            for (SalesInvoice s : existingList) {
//                if (s.getId().equals(selectedID)) {
//                    System.out.println("Test");
//                    for (Employee e : employeeList)
//                    {
//                        if (e.getId().equals(s.getEid()))
//                        {
//                            idNhanVien.setSelectedIndex(employeeList.indexOf(e) + 1);
//                        }
//                    }
//
//                    for (Customer e : customerList)
//                    {
//                        if (e.getId().equals(s.getCid()))
//                        {
//                            idKhachHang.setSelectedIndex(customerList.indexOf(e) + 1);
//                        }
//                    }
//
//                    ngayTao.setText(dateFormatter.format(s.getDate()));
//                    addedList = s.getDetailedSalesInvoices();
//
//                    for (DetailedSalesInvoice d : addedList)
//                    {
//                        int count = 1;
//                    String prodName = "Không tìm thấy tên sản phẩm";
//                    int prodPrice = 0;
//                    for (ProductDetail x : productDetailList) {
//                        if (d.getSeri().equals(x.getSerialNumber())) {
//                            String idProd = productDAO.getProductIDbyMaPhanLoai(x.getIdPhanLoai());
//                            for (Product y : productList) {
//                                if (y.getMaSp().equals(idProd)) {
//                                    prodName = y.getTenSp();
//                                    total += y.getGiasp();
//                                    break;
//                                }
//                            }
//                            break;
//                        }
//                    }
//                    Object[] row = {
//                           count,
//                            d.getSeri(),
//                            prodName,
//                            String.format("%d đồng", prodPrice),
//                    };
//                    tableModel.addRow(row);
//                }
//                }
//            }
//        }
    }

    private void actionProcessing()
    {
        /*serialNumber.addActionListener(e -> {
            String prodName = "Thông tin sản phẩm tương ứng với số seri";
            for (ProductDetail x : productDetailList) {
                if (serialNumber.getSelectedItem().toString().equals(x.getSerialNumber())) {
                    String idProd = productDAO.getProductIDbyMaPhanLoai(x.getIdPhanLoai());
                    for (Product y : productList) {
                        if (y.getMaSp().equals(idProd)) {
                            prodName = y.getTenSp();
                            break;
                        }
                    }
                    tenSanPham.setText(prodName);
                    break;
                }
            }
        });*/
        huy.addActionListener(e -> dispose());

        sanPhamDaThem.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = sanPhamDaThem.getSelectedRow();
            }
        });

        xacNhanTao.addActionListener(e -> {
            if (validHoaDonInput()) {
                System.out.println("Add");
                SalesInvoice add = fetchInput();
                for (DetailedSalesInvoice x : add.getDetailedSalesInvoices())
                {
                    System.out.println(x.getSeri());
                }
                existingList.add(add);
                invoiceBUS.addSalesInvoice(add);
                panel.refreshTable();
                dispose();
            }
        });

        chonSoSeri.addActionListener(e -> {
            if (tenSanPham.getSelectedIndex() != 0) {
                List<String> serialList = new ArrayList<>();
                for (ProductDetail x : productDetailList) {
                    if (productBUS.getNamebyIdPL(x.getIdPhanLoai()).equals(tenSanPham.getSelectedItem().toString())) {
                        if (addedList.size() == 0) {
                            System.out.println(" empty");
                            serialList.add(x.getSerialNumber());
                        } else {
                            boolean exist = false;
                            for (DetailedSalesInvoice y : addedList) {
                                System.out.println(x.getSerialNumber());
                                System.out.println(y.getSeri());
                                if (x.getSerialNumber().equals(y.getSeri())) {
                                    exist = true;
                                }
                            }
                            if (exist == false)
                            serialList.add(x.getSerialNumber());
                        }
                    }
                }
                serialNumber = new PhieuXuatDialog1(this, serialList);
            }
        });

        xacNhanThem.addActionListener(e -> {
            if (tenSanPham.getSelectedIndex() == 0 || serialNumber == null)
                return;
            List<String> selectedList = serialNumber.getSelectedList();
            if (selectedList.size() != 0) {
                int count = invoiceBUS.getDetailCount();
                for (ProductDetail x : productDetailList) {
                    for (String sn : selectedList) {
                        if (x.getSerialNumber().equals(sn)) {
                            DetailedSalesInvoice newDetail = new DetailedSalesInvoice();
                            newDetail.setId(String.valueOf(count));
                            newDetail.setFid(String.valueOf(existingList.size() + 1));
                            newDetail.setSeri(x.getSerialNumber());
                            newDetail.setDonGia(productBUS.getPriceByIdPL(x.getIdPhanLoai()));
                            addedList.add(newDetail);
                            count++;
                            Object[] row = {
                                    addedList.size(),
                                    newDetail.getSeri(),
                                    tenSanPham.getSelectedItem(),
                                    String.format("%f đồng", newDetail.getDonGia()),
                            };
                    tableModel.addRow(row);
                        }
                    }
                }
                selectedList.removeAll(selectedList);
            }
        });

            xacNhanXoa.addActionListener(e -> {
               if (selectedRow != -1) {
                   addedList.remove(selectedRow);
                   tableModel.removeRow(selectedRow);
                   for (DetailedSalesInvoice x : addedList) {
                       if (addedList.indexOf(x) >= selectedRow)
                       {
                           x.setId(String.valueOf(addedList.indexOf(x) + 1));
                           tableModel.setValueAt(addedList.indexOf(x) + 1,addedList.indexOf(x), 0);
                       }
                   }
               }
        });

        ngayTao.addPropertyChangeListener(e -> {
            //Kiem tra loi dinh dang ngay
            if (ngayTao.getText().length() == 10)
            {
                try
                {
                    LocalDate.parse(ngayTao.getText(), dateFormatter);
                    flag = true;
                }
                catch (DateTimeParseException d) {
                    flag = false;
                }
            }
        });
    }

    private SalesInvoice fetchInput() {
        SalesInvoice adding = new SalesInvoice();
        adding.setId(String.valueOf(existingList.size() + 1));
        for (Employee e : employeeList) {
            if (idNhanVien.getSelectedItem().toString().equals(e.getName())) {
                adding.setEid(e.getId());
                break;
            }
        }

        for (Customer e : customerList) {
            if (idKhachHang.getSelectedItem().toString().equals(e.getName())) {
                adding.setCid(e.getId());
                break;
            }
        }

        adding.setDate(LocalDate.parse(ngayTao.getText(), dateFormatter));
        adding.setTotalPayment(total);

        for (Promotion e : promoList) {
            if (idKhuyenMai.getSelectedItem().toString().equals(e.getTenKhuyenMai())) {
                adding.setDid(e.getIdKhuyenMai());
                break;
            }
        }

        adding.setDetailedSalesInvoice(addedList);
        return adding;
    }

    private boolean validHoaDonInput()
    {
        if (idKhachHang.getSelectedIndex() == 0 || idNhanVien.getSelectedIndex() == 0 /*|| serialNumber.getSelectedIndex() == 0*/ || flag == false)
        {
            System.out.println("invalid");
            return false;
        }
        return true;
    }

    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundedBorder() {
            this.radius = 25;
            this.color = Color.black;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2));
            RoundRectangle2D roundRect = new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius);
            g2d.draw(roundRect);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 5, 5);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = 5;
            return insets;
        }
    }

    public Point getButtonLocation() {
        return chonSoSeri.getLocation();
    }

    public Dimension getButtonSize() {
        return chonSoSeri.getSize();
    }
}