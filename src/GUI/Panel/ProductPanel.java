package GUI.Panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;

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
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(980, 90));
        toolbar.setBackground(Color.WHITE);
        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this, mainFrame));
        toolbar.add(MenuChucNang.createSearchPanel());

        return toolbar;
    }

    private JPanel createTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);

    ProductDAO productDAO = new ProductDAO();
    List<Product> products = productDAO.getAllProducts();

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
    productTable.setRowHeight(28);
    productTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    productTable.getTableHeader().setBackground(new Color(240, 240, 240));
    productTable.setGridColor(new Color(230, 230, 230));
    productTable.setShowVerticalLines(false);
    productTable.setShowHorizontalLines(true);
    productTable.setSelectionBackground(new Color(204, 229, 255));
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

    // Bo góc bảng bằng JPanel bọc bên ngoài (nếu cần)
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
