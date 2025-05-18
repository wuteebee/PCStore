package GUI.Dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import BUS.PhieuNhapBUS;
import BUS.ProductBUS;
import DTO.ChiTietDonNhap;
import DTO.Customer;
import DTO.Product;
import DTO.ProductDetail;
import GUI.Panel.ProductDetailPanel;

import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.awt.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DanhSachChiTietSP extends JDialog {
    DefaultTableModel tableModel;
    ProductDetailPanel productDetailPanel;
    PhieuNhapBUS bus = new PhieuNhapBUS();
    List<ChiTietDonNhap> danhsach = new ArrayList<>();

    public DanhSachChiTietSP(ProductDetailPanel productDetailPanel) {
        this.productDetailPanel = productDetailPanel;
        setTitle("Danh sách chi tiết sản phẩm");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Table
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1; // Quan trọng để full chiều ngang
        gbc.weighty = 1; // Full chiều dọc
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(table(), gbc);

        setModal(true);
        setResizable(true);
        setVisible(true);
    }

    public JPanel table() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#FFFFFF"));

        ProductBUS productBUS = new ProductBUS();
        Product product = productDetailPanel.getProduct();
        int phienban = productDetailPanel.getPhienban();

        List<ProductDetail> listpd = productBUS.getProductDetailList(
                product.getDanhSachPhienBan().get(phienban - 1).getIdVariant()
        );

        String[] columnNames = {"SerialNumber", "Giá Nhập", "Mã phiếu nhập", "Mã phiếu xuất", "Tình trạng"};
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")); // Format VND

        List<Object[]> dataList = new ArrayList<>();
        for (ProductDetail pd : listpd) {
            String maPhieuXuatText = pd.getMaPhieuXuat().equals("-1") ? "Chưa xuất kho" : pd.getMaPhieuXuat();
            String tinhTrang = pd.getMaPhieuXuat().equals("-1") ? "Tồn kho" : "Đã bán";
            double giaNhap = productBUS.getGiaNhap(pd.getSerialNumber());

            dataList.add(new Object[]{
                    pd.getSerialNumber(),
                    formatter.format(giaNhap),
                    pd.getMaPhieuNhap(),
                    maPhieuXuatText,
                    tinhTrang
            });
        }

        Object[][] data = dataList.toArray(new Object[0][]);
        tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.setBackground(Color.decode("#FFFFFF"));
        table.setGridColor(Color.decode("#90CAF9"));
        table.setSelectionBackground(Color.decode("#BBDEFB"));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(null);
        scrollPane.setMinimumSize(new Dimension(0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(scrollPane, gbc);

        return panel;
    }
}
