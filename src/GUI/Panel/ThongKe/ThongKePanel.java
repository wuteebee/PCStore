package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import GUI.Main;
import java.awt.*;
import javax.swing.*;

public class ThongKePanel extends JPanel {
    private JTabbedPane tabbedPane;
    private ThongKeDoanhThuTungNam thongKeTungNam;
    private ThongKeDoanhThuTungThang thongKeTungThang;
    private ThongKeDoanhThuTuNgayDenNgay thongKeTuNgayDenNgay;
    private ThongKeKhachHang thongKeKhachHang; // Thêm panel Khách hàng
    private ThongKeNhaCungCap thongKeNhaCungCap; // Thêm panel Nhà cung cấp
    private ThongKeTonKho thongKeTonKho; // Thêm panel Tồn kho
    private Color backgroundColor = new Color(240, 247, 250);
    private ThongKeBUS thongKeBUS;
    private Main mainFrame;

    public ThongKePanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.thongKeBUS = new ThongKeBUS();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(1, 1));
        setBackground(backgroundColor);

        // Khởi tạo các panel
        thongKeTungNam = new ThongKeDoanhThuTungNam(thongKeBUS);
        thongKeTungThang = new ThongKeDoanhThuTungThang(thongKeBUS);
        thongKeTuNgayDenNgay = new ThongKeDoanhThuTuNgayDenNgay(thongKeBUS);
        thongKeKhachHang = new ThongKeKhachHang(thongKeBUS); // Khởi tạo panel Khách hàng
        thongKeNhaCungCap = new ThongKeNhaCungCap(thongKeBUS); // Khởi tạo panel Nhà cung cấp
        thongKeTonKho = new ThongKeTonKho(thongKeBUS); // Khởi tạo panel Tồn kho

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(0xA1D6E2));
        tabbedPane.setOpaque(false);
        
        // Thêm các tab vào JTabbedPane
        tabbedPane.addTab("Thống kê theo năm", thongKeTungNam);
        tabbedPane.addTab("Thống kê từng tháng trong năm", thongKeTungThang);
        tabbedPane.addTab("Thống kê từ ngày đến ngày", thongKeTuNgayDenNgay);
        tabbedPane.addTab("Thống kê khách hàng", thongKeKhachHang); // Thêm tab Khách hàng
        tabbedPane.addTab("Thống kê nhà cung cấp", thongKeNhaCungCap); // Thêm tab Nhà cung cấp
        tabbedPane.addTab("Thống kê tồn kho", thongKeTonKho); // Thêm tab Tồn kho

        add(tabbedPane);

        // Rounded corners
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g);
        g2.dispose();
    }
}