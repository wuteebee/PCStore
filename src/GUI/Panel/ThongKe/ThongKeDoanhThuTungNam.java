package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import GUI.Components.NumericDocumentFilter;
import GUI.Components.PanelBorderRadius;
import GUI.Components.TableSorter;
import GUI.Components.Chart.BarChart.Chart;
import GUI.Components.Chart.BarChart.ModelChart;
import helper.Formater;
import helper.JTableExporter;
import helper.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder; // Thêm dòng này

public class ThongKeDoanhThuTungNam extends JPanel implements ActionListener {
    private PanelBorderRadius pnlChart;
    private JPanel pnlTop;
    private ThongKeBUS thongKeBUS;
    private JTextField yearChooserStart, yearChooserEnd;
    private Chart chart;
    private JButton btnReset, btnThongKe, btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;
    private ArrayList<ThongKeDoanhThuDTO> dataset;
    private int currentYear;

    public ThongKeDoanhThuTungNam(ThongKeBUS thongKeBUS) {
        this.thongKeBUS = thongKeBUS;
        this.currentYear = LocalDate.now().getYear();
        this.dataset = thongKeBUS.getDoanhThuTheoTungNam(currentYear - 5, currentYear);
        initComponents();
        loadDataTable(dataset);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top Panel
        pnlTop = new JPanel(new FlowLayout());
        JLabel lblChonNamBatDau = new JLabel("Từ năm");
        JLabel lblChonNamKetThuc = new JLabel("Đến năm");

        yearChooserStart = new JTextField("");
        yearChooserEnd = new JTextField("");
        PlainDocument docStart = (PlainDocument) yearChooserStart.getDocument();
        docStart.setDocumentFilter(new NumericDocumentFilter());
        PlainDocument docEnd = (PlainDocument) yearChooserEnd.getDocument();
        docEnd.setDocumentFilter(new NumericDocumentFilter());

        btnThongKe = new JButton("Thống kê");
        btnReset = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        btnThongKe.addActionListener(this);
        btnReset.addActionListener(this);
        btnExport.addActionListener(this);

        pnlTop.add(lblChonNamBatDau);
        pnlTop.add(yearChooserStart);
        pnlTop.add(lblChonNamKetThuc);
        pnlTop.add(yearChooserEnd);
        pnlTop.add(btnThongKe);
        pnlTop.add(btnReset);
        pnlTop.add(btnExport);

        // Chart Panel
        pnlChart = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(pnlChart, BoxLayout.Y_AXIS);
        pnlChart.setLayout(boxly);
        loadDataChart(dataset);

        // Table
        tableThongKe = new JTable();
        tableThongKe.setBackground(new Color(0xA1D6E2));
        scrollTableThongKe = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = {"Năm", "Chi phí", "Doanh thu", "Lợi nhuận"};
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

        TableSorter.configureTableColumnSorter(tableThongKe, 0, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 1, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 2, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 3, TableSorter.VND_CURRENCY_COMPARATOR);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlChart, BorderLayout.CENTER);
        add(scrollTableThongKe, BorderLayout.SOUTH);
    }

    public void loadDataTable(ArrayList<ThongKeDoanhThuDTO> list) {
        tblModel.setRowCount(0);
        for (ThongKeDoanhThuDTO i : list) {
            tblModel.addRow(new Object[]{
                i.getThoigian(),
                Formater.FormatVND(i.getVon()),
                Formater.FormatVND(i.getDoanhthu()),
                Formater.FormatVND(i.getLoinhuan())
            });
        }
    }

    public void loadDataChart(ArrayList<ThongKeDoanhThuDTO> list) {
        pnlChart.removeAll();
        chart = new Chart();
        chart.addLegend("Chi phí", new Color(245, 189, 135));
        chart.addLegend("Doanh thu", new Color(135, 189, 245));
        chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
        for (ThongKeDoanhThuDTO i : list) {
            chart.addData(new ModelChart("Năm " + i.getThoigian(), new double[]{i.getVon(), i.getDoanhthu(), i.getLoinhuan()}));
        }
        chart.repaint();
        chart.validate();
        pnlChart.add(chart);
        pnlChart.repaint();
        pnlChart.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnThongKe) {
            if (!Validation.isEmpty(yearChooserStart.getText()) && !Validation.isEmpty(yearChooserEnd.getText())) {
                int nambd = Integer.parseInt(yearChooserStart.getText());
                int namkt = Integer.parseInt(yearChooserEnd.getText());
                if (nambd > currentYear || namkt > currentYear) {
                    JOptionPane.showMessageDialog(this, "Năm không được lớn hơn năm hiện tại");
                } else if (namkt < nambd || namkt <= 2015 || nambd <= 2015) {
                    JOptionPane.showMessageDialog(this, "Năm kết thúc không được bé hơn năm bắt đầu và phải lớn hơn 2015");
                } else {
                    dataset = thongKeBUS.getDoanhThuTheoTungNam(nambd, namkt);
                    loadDataChart(dataset);
                    loadDataTable(dataset);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ!");
            }
        } else if (source == btnReset) {
            yearChooserStart.setText("");
            yearChooserEnd.setText("");
            dataset = thongKeBUS.getDoanhThuTheoTungNam(currentYear - 5, currentYear);
            loadDataChart(dataset);
            loadDataTable(dataset);
        } else if (source == btnExport) {
            try {
                JTableExporter.exportJTableToExcel(tableThongKe);
            } catch (IOException ex) {
                Logger.getLogger(ThongKeDoanhThuTungNam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}