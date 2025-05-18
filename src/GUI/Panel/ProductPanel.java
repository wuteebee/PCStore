package GUI.Panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;

import BUS.ProductBUS;
import DAO.ProductDAO;
import DTO.Product;
import GUI.Main;
import GUI.Components.MenuChucNang;
import GUI.Dialog.ThemSanPham;

public class ProductPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable productTable;
    private String selectedProductId = "-1";
    private Main mainFrame;
    private JTextField searchField;
       private JButton btnSearch;
    private JButton btnReset;
    private   List<Product> products;

    public ProductPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        addTableSelectionListener();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);

        add(createCustomToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    public void updateTable(Product sp){
        Object[] newRow = {sp.getMaSp(),sp.getTenSp(), sp.getDanhMuc().getTenDanhMuc(), sp.getThuongHieu().getTenThuongHieu(),sp.getMoTaSanPham()};
        tableModel.addRow(newRow);
    }
    public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(980, 90));
        toolbar.setBackground(Color.WHITE);
        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));
         JPanel searchPanel=MenuChucNang.createSearchPanel();
        toolbar.add(searchPanel);


         // Lấy tham chiếu đến các thành phần trong searchPanel để xử lý tìm kiếm
        Component[] components = searchPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("Tìm kiếm")) {
                    btnSearch = button;
                } else if (button.getText().equals("Làm mới")) {
                    btnReset = button;
                }
            } else if (comp instanceof JTextField) {
                searchField = (JTextField) comp;
            }
        }

        // Gán sự kiện tìm kiếm và làm mới
        btnSearch.addActionListener(e -> searchProduct());
        btnReset.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });

        return toolbar;
    }

   private void searchProduct() {
    String keyword = searchField.getText().trim().toLowerCase();
    if (keyword.isEmpty()) {
        loadData();
        return;
    }

    tableModel.setRowCount(0); // Xóa dữ liệu cũ

    ProductBUS productBUS=new ProductBUS();
  
    List<Product> products =  productBUS.getAllProducts(); // lấy lại dữ liệu

    for (Product product : products) {
        if (!product.isTrangThai()) continue;

        String maSP = product.getMaSp();
        String tenSP = product.getTenSp();
        String tenLoai = product.getDanhMuc() != null ? product.getDanhMuc().getTenDanhMuc() : "N/A";
        String thuongHieu = product.getThuongHieu() != null ? product.getThuongHieu().getTenThuongHieu() : "N/A";
        String moTa = product.getMoTaSanPham();

        if (maSP.toLowerCase().contains(keyword) ||
            tenSP.toLowerCase().contains(keyword) ||
            moTa.toLowerCase().contains(keyword)) {
            Object[] row = { maSP, tenSP, tenLoai, thuongHieu, moTa };
            tableModel.addRow(row);
        }
    }
}

private void loadData() {
    ProductBUS productBUS=new ProductBUS();

    products = productBUS.getAllProducts(); 

    tableModel.setRowCount(0); // Xoá hết dữ liệu cũ

    for (Product product : products) {
        if (!product.isTrangThai()) continue;

        String maSP = product.getMaSp();
        String tenSP = product.getTenSp();
        String tenLoai = product.getDanhMuc() != null ? product.getDanhMuc().getTenDanhMuc() : "N/A";
        String thuongHieu = product.getThuongHieu() != null ? product.getThuongHieu().getTenThuongHieu() : "N/A";
        String moTa = product.getMoTaSanPham();

        Object[] row = { maSP, tenSP, tenLoai, thuongHieu, moTa };
        tableModel.addRow(row);
    }
}

    private JPanel createTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);

    ProductBUS productBUS = new ProductBUS();
   products = productBUS.getAllProducts();

    String[] columnNames = { "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Thương hiệu", "Mô tả" };
    tableModel = new DefaultTableModel(columnNames, 0);

    for (Product product : products) {
        if(product.isTrangThai()== false){
            continue;
        }
        String maSP = product.getMaSp();
        String tenSP = product.getTenSp();
        String tenLoai = product.getDanhMuc() != null ? product.getDanhMuc().getTenDanhMuc() : "N/A";
        String thuongHieu = product.getThuongHieu() != null ? product.getThuongHieu().getTenThuongHieu() : "N/A";
        String moTa = product.getMoTaSanPham();
        Object[] row = { maSP, tenSP, tenLoai, thuongHieu, moTa };
        tableModel.addRow(row);
    }

    productTable = new JTable(tableModel) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };


  productTable.setFillsViewportHeight(true);
    productTable.setRowHeight(30); // Đổi sang 30px để giống Supplier
    productTable.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Giữ font Segoe UI cho nội dung
    productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Đổi sang Arial cho tiêu đề
    productTable.getTableHeader().setBackground(new Color(100, 149, 237)); // Nền xanh dương giống Supplier
    productTable.getTableHeader().setForeground(Color.BLACK); // Giữ chữ đen như Product
    productTable.setShowGrid(false); // Ẩn lưới giống Supplier
    productTable.setSelectionBackground(new Color(173, 216, 230)); // Màu chọn giống Supplier
    productTable.setSelectionForeground(Color.BLACK);
    productTable.setRowMargin(3);

    JScrollPane scrollPane = new JScrollPane(productTable);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getViewport().setBackground(Color.WHITE);
    scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(180, 180, 180);
        }
    });

 
    JPanel tableWrapper = new JPanel(new BorderLayout());
    tableWrapper.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
    tableWrapper.setBackground(Color.WHITE);
    tableWrapper.add(scrollPane, BorderLayout.CENTER);

    panel.add(tableWrapper, BorderLayout.CENTER);
    return panel;
}

    public String getSelectedProductId() {
        return selectedProductId;
    }

    public void addTableSelectionListener() {
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedProductId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }

    public JTextField getTextField() {
        MenuChucNang menu = new MenuChucNang();
        return menu.getTextField();
    }
        public void reloadPanel() {
    mainFrame.setMainPanel(new ProductPanel(mainFrame));
}

    // public void printTableData(String name) {
    //     int rowCount = tableModel.getRowCount();
    //     int colCount = tableModel.getColumnCount();
    
    //     System.out.println("===== DỮ LIỆU TRONG BẢNG =====");
    //     for (int i = 0; i < rowCount; i++) {
           
    //         for (int j = 0; j < colCount; j++) {
    //             Object cellData = tableModel.getValueAt(i, j);
    //             if(cellData!=null&&cellData){
                    
    //             }
    //         }
         
    //     }
    // }
    
    // public void openAddNewProductDialog(Product sp) {
    //     ThemSanPham sanpham=new ThemSanPham();
    //     sanpham.formThemSanPham(sp);
    // }


}
