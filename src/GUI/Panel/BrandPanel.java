package GUI.Panel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

import BUS.BrandBUS;
import DAO.BrandDAO;
import DTO.Brand;
import GUI.Components.MenuChucNang;
import GUI.Main;

public class BrandPanel extends JPanel {
    private List<Brand> brandList;
    private BrandBUS brandBUS;
    private BrandDAO brandDAO;
    private DefaultTableModel tableModel;
    private JTable brandTable;
    private String selectedBrandId = "-1";
    private Main mainFrame;

    public BrandPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.brandBUS = new BrandBUS();
        this.brandDAO = new BrandDAO();
        initComponents();
        addTableSelectionListener();
    }

    private void initComponents() {
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

        String[] columnNames = { "Mã thương hiệu", "Tên thương hiệu", "Mã danh mục", "Trạng thái" };
        tableModel = new DefaultTableModel(columnNames, 0);
        brandTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTableHeader header = brandTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
            brandTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        brandTable.setShowGrid(false);
        brandTable.setRowHeight(30);
        brandTable.setSelectionBackground(new Color(173, 216, 230));
        brandTable.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(brandTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        panel.add(scrollPane, BorderLayout.CENTER);
        loadBrandTable();

        return panel;
    }

    public void loadBrandTable() {
        tableModel.setRowCount(0);
        brandList = brandBUS.getAllBrands();
        for (Brand brand : brandList) {
            Object[] row = {
                brand.getMaThuongHieu(),
                brand.getTenThuongHieu(),
                brand.getmaDanhMuc() == null ? "Tất cả" : brand.getmaDanhMuc(),
                brand.isTrangThai() ? "Hoạt động" : "Ngưng hoạt động"
            };
            tableModel.addRow(row);
        }
    }

    private void addTableSelectionListener() {
        brandTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = brandTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedBrandId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }

    public String getSelectedBrandId() {
        return selectedBrandId;
    }

    public void openAddBrandDialog() {
        new GUI.Dialog.ThemThuongHieu(mainFrame, this, false, null).setVisible(true);
    }

    public void openEditBrandDialog(String id) {
        DTO.Brand brand = new BrandBUS().getBrandById(id);
        new GUI.Dialog.ThemThuongHieu(mainFrame, this, true, brand).setVisible(true);
    }

    public void openRemoveBrandDialog(String id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thương hiệu?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = brandBUS.deleteBrand(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa thương hiệu thành công.");
                loadBrandTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa thương hiệu vì đang được sử dụng trong sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}