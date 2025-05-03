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
import DTO.ProductDetail;
import GUI.Panel.ProductDetailPanel;

import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.awt.Component;

public class DanhSachChiTietSP extends JDialog {
    DefaultTableModel tableModel;
    ProductDetailPanel productDetailPanel;
    PhieuNhapBUS bus=new PhieuNhapBUS();
    List <ChiTietDonNhap> danhsach=new ArrayList<>();

    public DanhSachChiTietSP(ProductDetailPanel productDetailPanel) {
        this.productDetailPanel = productDetailPanel;
        setTitle("Danh sách chi tiết sản phẩm");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Header
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        add(header(), gbc);

        // Table
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(table(), gbc);

        setModal(true);
        setResizable(true);
        setVisible(true);
    }

    public JPanel header() {
        JPanel header = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        header.setBackground(Color.decode("#E3F2FD")); // Xanh dương sáng pastel
        header.setPreferredSize(new Dimension(800, 80));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel tinhTrangPanel = new JPanel();
        tinhTrangPanel.setOpaque(false);
        tinhTrangPanel.add(new JLabel("Tình trạng"));
        String[] tinhTrangOptions = {"Tất cả", "Tồn kho", "Đã bán"};
        JComboBox<String> comboBox = new JComboBox<>(tinhTrangOptions);
        comboBox.setFont(inputFont);
        comboBox.setBackground(Color.decode("#FFFFFF"));
        tinhTrangPanel.add(comboBox);

        JPanel soLuongPanel = new JPanel();
        soLuongPanel.setOpaque(false);
        soLuongPanel.add(new JLabel("Số lượng"));
        JTextField sl = new JTextField(6);
        sl.setFont(inputFont);
        sl.setEditable(false);
        sl.setBackground(Color.decode("#F5F5F5"));
        soLuongPanel.add(sl);

        JPanel timKiemPanel = new JPanel();
        timKiemPanel.setOpaque(false);
        timKiemPanel.add(new JLabel("Tìm kiếm"));
        JTextField search = new JTextField(12);
        search.setFont(inputFont);
        search.setBackground(Color.decode("#FFFFFF"));
        timKiemPanel.add(search);

        // Gán font cho label
        for (Component c : tinhTrangPanel.getComponents()) {
            if (c instanceof JLabel) c.setFont(labelFont);
        }
        for (Component c : soLuongPanel.getComponents()) {
            if (c instanceof JLabel) c.setFont(labelFont);
        }
        for (Component c : timKiemPanel.getComponents()) {
            if (c instanceof JLabel) c.setFont(labelFont);
        }

        // Thêm vào header
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridy = 0;

        gbc.gridx = 0;
        header.add(tinhTrangPanel, gbc);

        gbc.gridx = 1;
        header.add(soLuongPanel, gbc);

        gbc.gridx = 2;
        header.add(timKiemPanel, gbc);

        return header;
    }

    public JPanel table() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#FFFFFF"));
        ProductBUS productBUS = new ProductBUS();
        productBUS.getProductDetailList(productDetailPanel.getPhienban());
        List<ChiTietDonNhap> danhsach=bus.getAll_ChiTietDonNhap();
        String[] columnNames = {"SerialNumber", "Giá Nhập", "Mã phiếu nhập", "Mã phiếu xuất", "Trạng thái"};
        Object[][] data = {
            {"SN001", 1000.0, "PN001", "PX001", true},
            {"SN002", 2000.0, "PN002", "PX002", false},
            {"SN003", 1500.0, "PN003", "PX003", true},
            {"SN004", 2500.0, "PN004", "PX004", false},
            {"SN005", 3000.0, "PN005", "PX005", true}
        };

        tableModel = new DefaultTableModel(data, columnNames);

        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Style cho bảng
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.setBackground(Color.decode("#FFFFFF"));
        table.setGridColor(Color.decode("#90CAF9"));
        table.setSelectionBackground(Color.decode("#BBDEFB"));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(scrollPane, gbc);

        return panel;
    }


        public void loadTable() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        danhsach = bus.getAll_ChiTietDonNhap();
        // for (ChiTietDonNhaps : customers) {
        //     System.out.println(customer.getName());
        //     Object[] rowData = {
        //         customer.getId(),
        //         customer.getName(),
        //         customer.getPhoneNumber(),
        //         customer.getEmail(),
        //         customer.getDateOfJoining()
        //     };
        //     tableModel.addRow(rowData);
        // }
    }
}
