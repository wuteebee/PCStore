package GUI.Panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DAO.ProductDAO;
import DTO.Product;
import GUI.Main;
import GUI.Components.MenuChucNang;

public class ProductPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable productTable;
    private String selectedCustomerId = "-1";
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

    public JPanel createCustomToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));

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
        productTable.setRowHeight(30);
        productTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public String getSelectedCustomerId() {
        return selectedCustomerId;
    }

    public void addTableSelectionListener() {
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedCustomerId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }
}
