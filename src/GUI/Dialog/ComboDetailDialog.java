package GUI.Dialog;

import BUS.PromotionBUS;
import DTO.ComboProduct;
import GUI.Panel.PromotionPanel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ComboDetailDialog extends JDialog {
    private JTable comboTable;
    private DefaultTableModel comboTableModel;
    private PromotionBUS promotionBUS;

    public ComboDetailDialog(PromotionPanel parent, String idKhuyenMai) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Chi tiết Combo - " + idKhuyenMai, true);
        this.promotionBUS = new PromotionBUS();
        initComponents(idKhuyenMai);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setAlwaysOnTop(true); // Đảm bảo dialog luôn ở trên cùng
    }

    private void initComponents(String idKhuyenMai) {
        setLayout(new BorderLayout(10, 10));

        // Bảng hiển thị sản phẩm trong combo
        String[] columns = {"Mã sản phẩm", "Tên sản phẩm"};
        comboTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        comboTable = new JTable(comboTableModel);
        comboTable.setRowHeight(25);
        comboTable.setShowGrid(false);
        comboTable.setSelectionBackground(new Color(173, 216, 230));
        comboTable.setSelectionForeground(Color.BLACK);

        // Tải dữ liệu sản phẩm combo
        List<ComboProduct> products = promotionBUS.getComboProducts(idKhuyenMai);
        System.out.println("Loaded " + products.size() + " products for combo " + idKhuyenMai);
        for (ComboProduct p : products) {
            System.out.println("Adding combo product: " + p.getIdSanPham() + " - " + p.getTenSanPham());
            comboTableModel.addRow(new Object[]{p.getIdSanPham(), p.getTenSanPham()});
        }

        JScrollPane scrollPane = new JScrollPane(comboTable);
        add(scrollPane, BorderLayout.CENTER);

        
    }
}