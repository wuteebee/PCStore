package GUI.Panel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BUS.CustomerBUS;
import DAO.ProductDAO;
import DTO.Product;
import GUI.Components.MenuChucNang;

public class ProductPanel extends JPanel{
    private DefaultTableModel tableModel;
    private JTable customerTable;

    public ProductPanel(){
        initComponent();
    }

    private void initComponent(){
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);
        
        add(createCustomToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }


         public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));
        
        MenuChucNang menu = new MenuChucNang();
        toolbar.add(menu.createActionPanel(this));  
        toolbar.add(MenuChucNang.createSearchPanel()); 
        
        return toolbar;
    }


    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
    
        // Lấy danh sách sản phẩm từ DAO
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();
    
        // Tiêu đề cột
        String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Thương hiệu", "Mô tả"};
        tableModel = new DefaultTableModel(columnNames, 0);
    
        // Đổ dữ liệu vào bảng
        for (Product product : products) {
            String maSP = product.getMaSp();
            String tenSP = product.getTenSp();
            String tenLoai = product.getDanhMuc() != null ? product.getDanhMuc().getTenDanhMuc() : "N/A";
            String thuongHieu = product.getThuongHieu() != null ? product.getThuongHieu().getTenThuongHieu() : "N/A";
            String moTa = product.getMotaSanPham();
    
            Object[] row = {maSP, tenSP, tenLoai, thuongHieu, moTa};
            tableModel.addRow(row);
        }
    
        customerTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa
            }
        };
    
        // Tùy chỉnh bảng (nếu muốn)
        customerTable.setFillsViewportHeight(true);
        customerTable.setRowHeight(30);
        customerTable.getTableHeader().setReorderingAllowed(false);
    
        JScrollPane scrollPane = new JScrollPane(customerTable);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        return panel;
    }}
    