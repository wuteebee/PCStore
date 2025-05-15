package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTheoThangDTO;
import GUI.Components.PanelBorderRadius;
import GUI.Components.TableSorter;
import GUI.Components.Chart.BarChart.Chart;
import GUI.Components.Chart.BarChart.ModelChart;
import com.toedter.calendar.JYearChooser;
import helper.Formater;
import helper.JTableExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder; // Thêm dòng này

public class ThongKeDoanhThuTungThang extends JPanel implements ActionListener {
    private PanelBorderRadius pnlChart;
    private JPanel pnlTop;
    private ThongKeBUS thongKeBUS;
    private JYearChooser yearChooser;
    private Chart chart;
    private JButton btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;

    public ThongKeDoanhThuTungThang(ThongKeBUS thongKeBUS) {
        this.thongKeBUS = thongKeBUS;
        initComponent();
        loadThongKeThang(yearChooser.getYear());
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top Panel
        pnlTop = new JPanel(new FlowLayout());
        JLabel lblChonNam = new JLabel("Chọn năm thống kê");
        yearChooser = new JYearChooser();
        yearChooser.addPropertyChangeListener("year", e -> {
            int year = (Integer) e.getNewValue();
            loadThongKeThang(year);
        });

        btnExport = new JButton("Xuất Excel");
        btnExport.addActionListener(this);
        pnlTop.add(lblChonNam);
        pnlTop.add(yearChooser);
        pnlTop.add(btnExport);

        // Chart Panel
        pnlChart = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(pnlChart, BoxLayout.Y_AXIS);
        pnlChart.setLayout(boxly);
        chart = new Chart();
        chart.addLegend("Chi phí", new Color(245, 189, 135));
        chart.addLegend("Doanh thu", new Color(135, 189, 245));
        chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
        pnlChart.add(chart);

        // Table
        tableThongKe = new JTable();
        tableThongKe.setBackground(new Color(0xA1D6E2));
        scrollTableThongKe = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = {"Tháng", "Chi phí", "Doanh thu", "Lợi nhuận"};
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

        TableSorter.configureTableColumnSorter(tableThongKe, 0, TableSorter.STRING_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 1, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 2, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 3, TableSorter.VND_CURRENCY_COMPARATOR);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlChart, BorderLayout.CENTER);
        add(scrollTableThongKe, BorderLayout.SOUTH);
    }

    public void loadThongKeThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> list = thongKeBUS.getThongKeTheoThang(nam);
        pnlChart.remove(chart);
        chart = new Chart();
        chart.addLegend("Chi phí", new Color(245, 189, 135));
        chart.addLegend("Doanh thu", new Color(135, 189, 245));
        chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
        for (ThongKeTheoThangDTO i : list) {
            chart.addData(new ModelChart("Tháng " + i.getThang(), new double[]{i.getChiphi(), i.getDoanhthu(), i.getLoinhuan()}));
        }
        chart.repaint();
        chart.validate();
        pnlChart.add(chart);
        pnlChart.repaint();
        pnlChart.validate();
        tblModel.setRowCount(0);
        for (ThongKeTheoThangDTO i : list) {
            tblModel.addRow(new Object[]{
                "Tháng " + i.getThang(),
                Formater.FormatVND(i.getChiphi()),
                Formater.FormatVND(i.getDoanhthu()),
                Formater.FormatVND(i.getLoinhuan())
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExport) {
            try {
                JTableExporter.exportJTableToExcel(tableThongKe);
            } catch (IOException ex) {
                Logger.getLogger(ThongKeDoanhThuTungThang.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}