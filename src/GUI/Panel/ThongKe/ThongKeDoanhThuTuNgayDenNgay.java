package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTungNgayDTO;
import GUI.Components.PanelBorderRadius;
import GUI.Components.Chart.BarChart.Chart;
import GUI.Components.Chart.BarChart.ModelChart;
import com.toedter.calendar.JDateChooser;
import helper.Formater;
import helper.JTableExporter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class ThongKeDoanhThuTuNgayDenNgay extends JPanel {

    private PanelBorderRadius pnlChart;
    private JPanel pnl_top;
    private ThongKeBUS thongkeBUS;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton btnThongKe, btnReset, btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;
    private Chart chart;

    public ThongKeDoanhThuTuNgayDenNgay(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel chứa bộ lọc
        pnl_top = new JPanel(new FlowLayout());
        JLabel lblFrom = new JLabel("Từ ngày");
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        JLabel lblTo = new JLabel("Đến ngày");
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        btnThongKe = new JButton("Thống kê");
        btnReset = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        pnl_top.add(lblFrom);
        pnl_top.add(dateFrom);
        pnl_top.add(lblTo);
        pnl_top.add(dateTo);
        pnl_top.add(btnThongKe);
        pnl_top.add(btnExport);
        pnl_top.add(btnReset);

        // Sự kiện xuất Excel
        btnExport.addActionListener((ActionEvent e) -> {
            try {
                JTableExporter.exportJTableToExcel(tableThongKe);
            } catch (IOException ex) {
                Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Sự kiện thay đổi ngày
        dateFrom.addPropertyChangeListener("date", e -> {
            try {
                validateSelectDate();
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        dateTo.addPropertyChangeListener("date", e -> {
            try {
                validateSelectDate();
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Panel chứa biểu đồ
        pnlChart = new PanelBorderRadius();
        pnlChart.setLayout(new BorderLayout());
        chart = new Chart();
        chart.addLegend("Chi phí", new Color(245, 189, 135));
        chart.addLegend("Doanh thu", new Color(135, 189, 245));
        chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
        pnlChart.add(chart, BorderLayout.CENTER);

        // Bảng dữ liệu
        tableThongKe = new JTable();
        tableThongKe.setBackground(new Color(0xA1D6E2));
        scrollTableThongKe = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Ngày", "Chi phí", "Doanh thu", "Lợi nhuận"};
        tblModel.setColumnIdentifiers(header);
        tableThongKe.setModel(tblModel);
        tableThongKe.setAutoCreateRowSorter(true);
        tableThongKe.setDefaultEditor(Object.class, null);
        scrollTableThongKe.setViewportView(tableThongKe);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableThongKe.setDefaultRenderer(Object.class, centerRenderer);
        tableThongKe.setFocusable(false);
        scrollTableThongKe.setPreferredSize(new Dimension(0, 300));

        // Sự kiện nút Thống kê
        btnThongKe.addActionListener(e -> {
            try {
                if (validateSelectDate()) {
                    if (dateFrom.getDate() != null && dateTo.getDate() != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String start = formatter.format(dateFrom.getDate());
                        String end = formatter.format(dateTo.getDate());
                        loadThongKeTungNgayTrongThang(start, end);
                    } else {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin");
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Sự kiện nút Làm mới
        btnReset.addActionListener(e -> {
            dateFrom.setDate(null);
            dateTo.setDate(null);
            tblModel.setRowCount(0);
            pnlChart.remove(chart);
            chart = new Chart();
            chart.addLegend("Chi phí", new Color(245, 189, 135));
            chart.addLegend("Doanh thu", new Color(135, 189, 245));
            chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
            pnlChart.add(chart);
            pnlChart.repaint();
            pnlChart.validate();
        });

        // Thêm các thành phần vào panel
        this.add(pnl_top, BorderLayout.NORTH);
        this.add(pnlChart, BorderLayout.CENTER);
        this.add(scrollTableThongKe, BorderLayout.SOUTH);
    }

    public boolean validateSelectDate() throws ParseException {
        Date time_start = dateFrom.getDate();
        Date time_end = dateTo.getDate();

        Date current_date = new Date();
        if (time_start != null && time_start.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateFrom.setCalendar(null);
            return false;
        }
        if (time_end != null && time_end.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateTo.setCalendar(null);
            return false;
        }
        if (time_start != null && time_end != null && time_start.after(time_end)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải lớn hơn ngày bắt đầu", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateTo.setCalendar(null);
            return false;
        }
        return true;
    }

    public void loadThongKeTungNgayTrongThang(String start, String end) {
        ArrayList<ThongKeTungNgayDTO> list = thongkeBUS.getThongKeTuNgayDenNgay(start, end);
        tblModel.setRowCount(0);
        for (ThongKeTungNgayDTO i : list) {
            tblModel.addRow(new Object[]{
                i.getNgay(),
                Formater.FormatVND(i.getChiphi()),
                Formater.FormatVND(i.getDoanhthu()),
                Formater.FormatVND(i.getLoinhuan())
            });
        }

        // Cập nhật biểu đồ
        pnlChart.remove(chart);
        chart = new Chart();
        chart.addLegend("Chi phí", new Color(245, 189, 135));
        chart.addLegend("Doanh thu", new Color(135, 189, 245));
        chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
        for (ThongKeTungNgayDTO i : list) {
            chart.addData(new ModelChart(i.getNgay(), new double[]{i.getChiphi(), i.getDoanhthu(), i.getLoinhuan()}));
        }
        chart.repaint();
        chart.validate();
        pnlChart.add(chart);
        pnlChart.repaint();
        pnlChart.validate();
    }
}