package GUI.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class NhapHoaDonPanel extends JPanel {
    private JTable tableSp, tableChiTiet;
    private DefaultTableModel modelSp, modelChiTiet;
    private JTextField tfMaSP, tfTenSP, tfGiaNhap, tfMaPhieu, tfNhanVien, tfMaImei;
    private JComboBox<String> cbCauHinh, cbPhuongThuc, cbNhaCungCap;
    private JLabel lbTongTien;

    public NhapHoaDonPanel() {
        setLayout(null);
        setBounds(0, 0, 1150, 700); // tăng width toàn bộ panel
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
        initBottomPanel();
    }

    private void initLeftPanel() {
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(10, 10, 300, 400);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        String[] columnsSp = {"Mã SP", "Tên SP", "SL"};
        modelSp = new DefaultTableModel(columnsSp, 0);
        tableSp = new JTable(modelSp);
        JScrollPane scroll = new JScrollPane(tableSp);
        scroll.setBounds(10, 20, 280, 300);

        JButton btnThem = new JButton("Thêm");
        btnThem.setBounds(10, 330, 130, 30);
        JButton btnNhapExcel = new JButton("Nhập Excel");
        btnNhapExcel.setBounds(150, 330, 130, 30);

        leftPanel.add(scroll);
        leftPanel.add(btnThem);
        leftPanel.add(btnNhapExcel);

        add(leftPanel);
    }

    private void initCenterPanel() {
        JPanel centerPanel = new JPanel(null);
        centerPanel.setBounds(320, 10, 400, 400);
        centerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        JLabel lbMaSP = new JLabel("Mã SP:");
        lbMaSP.setBounds(10, 20, 100, 25);
        tfMaSP = new JTextField();
        tfMaSP.setBounds(120, 20, 250, 25);

        JLabel lbTenSP = new JLabel("Tên SP:");
        lbTenSP.setBounds(10, 50, 100, 25);
        tfTenSP = new JTextField();
        tfTenSP.setBounds(120, 50, 250, 25);

        JLabel lbCauHinh = new JLabel("Cấu hình:");
        lbCauHinh.setBounds(10, 80, 100, 25);
        cbCauHinh = new JComboBox<>(new String[]{"32GB - 3GB - Xanh"});
        cbCauHinh.setBounds(120, 80, 250, 25);

        JLabel lbGia = new JLabel("Giá nhập:");
        lbGia.setBounds(10, 110, 100, 25);
        tfGiaNhap = new JTextField();
        tfGiaNhap.setBounds(120, 110, 250, 25);

        JLabel lbPhuongThuc = new JLabel("Phương thức:");
        lbPhuongThuc.setBounds(10, 140, 100, 25);
        cbPhuongThuc = new JComboBox<>(new String[]{"Nhập từng máy"});
        cbPhuongThuc.setBounds(120, 140, 250, 25);

        JLabel lbImei = new JLabel("Mã Imei:");
        lbImei.setBounds(10, 170, 100, 25);
        tfMaImei = new JTextField();
        tfMaImei.setBounds(120, 170, 250, 25);

        JButton btnQuet = new JButton("Quét Imei");
        btnQuet.setBounds(70, 210, 110, 30);
        JButton btnNhapExcel = new JButton("Nhập Excel");
        btnNhapExcel.setBounds(200, 210, 110, 30);

        centerPanel.add(lbMaSP); centerPanel.add(tfMaSP);
        centerPanel.add(lbTenSP); centerPanel.add(tfTenSP);
        centerPanel.add(lbCauHinh); centerPanel.add(cbCauHinh);
        centerPanel.add(lbGia); centerPanel.add(tfGiaNhap);
        centerPanel.add(lbPhuongThuc); centerPanel.add(cbPhuongThuc);
        centerPanel.add(lbImei); centerPanel.add(tfMaImei);
        centerPanel.add(btnQuet); centerPanel.add(btnNhapExcel);

        add(centerPanel);
    }

    private void initRightPanel() {
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBounds(730, 10, 390, 200);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhập"));

        JLabel lbMaPhieu = new JLabel("Mã phiếu:");
        lbMaPhieu.setBounds(10, 20, 100, 25);
        tfMaPhieu = new JTextField("PN-1");
        tfMaPhieu.setBounds(120, 20, 250, 25);

        JLabel lbNhanVien = new JLabel("Nhân viên:");
        lbNhanVien.setBounds(10, 60, 100, 25);
        tfNhanVien = new JTextField("Hoàng Gia Bảo");
        tfNhanVien.setBounds(120, 60, 250, 25);

        JLabel lbNCC = new JLabel("Nhà cung cấp:");
        lbNCC.setBounds(10, 100, 100, 25);
        cbNhaCungCap = new JComboBox<>(new String[]{"Công Ty TNHH Thế Giới Di Động"});
        cbNhaCungCap.setBounds(120, 100, 250, 25);

        rightPanel.add(lbMaPhieu); rightPanel.add(tfMaPhieu);
        rightPanel.add(lbNhanVien); rightPanel.add(tfNhanVien);
        rightPanel.add(lbNCC); rightPanel.add(cbNhaCungCap);

        add(rightPanel);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(null);
        bottomPanel.setBounds(10, 420, 1110, 250);
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        String[] columns = {"STT", "Mã SP", "Tên sản phẩm", "Mã phân loại", "Đơn giá", "Số lượng"};
        modelChiTiet = new DefaultTableModel(columns, 0);
        tableChiTiet = new JTable(modelChiTiet);

        TableColumnModel columnModel = tableChiTiet.getColumnModel();
        int[] columnWidths = {40, 80, 250, 120, 100, 80};
        for (int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        JScrollPane scroll = new JScrollPane(tableChiTiet);
        scroll.setBounds(10, 20, 1080, 180);

        lbTongTien = new JLabel("TỔNG TIỀN: 4,000,000đ");
        lbTongTien.setForeground(Color.RED);
        lbTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lbTongTien.setBounds(900, 200, 200, 30);

        bottomPanel.add(scroll);
        bottomPanel.add(lbTongTien);

        add(bottomPanel);
    }
}
