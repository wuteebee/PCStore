package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeNhaCungCapDTO;
import GUI.Components.ButtonCustom;
import GUI.Components.InputDate;
import GUI.Components.InputForm;
import GUI.Components.PanelBorderRadius;
import GUI.Components.TableSorter;
import GUI.Components.itemTaskbar;
import helper.Formater;
import helper.JTableExporter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeNhaCungCap extends JPanel implements ActionListener, KeyListener, PropertyChangeListener {

    private PanelBorderRadius nhapxuat_left, nhapxuat_center, pnlTaskbar;
    private JTable tblKH;
    private JScrollPane scrollTblTonKho;
    private DefaultTableModel tblModel;
    private InputForm tenkhachhang, topFilter;
    private InputDate start_date, end_date;
    private ButtonCustom export, reset;
    private ThongKeBUS thongkebus;
    private ArrayList<ThongKeNhaCungCapDTO> list;
    private itemTaskbar[] listitem;

    public ThongKeNhaCungCap(ThongKeBUS thongkebus) {
        this.thongkebus = thongkebus;
        this.list = thongkebus.getAllNCC();
        initComponent();
        loadDataTable(list);
        updateTaskbar();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel bộ lọc bên trái
        nhapxuat_left = new PanelBorderRadius();
        nhapxuat_left.setPreferredSize(new Dimension(300, 100));
        nhapxuat_left.setLayout(new BorderLayout());
        nhapxuat_left.setBorder(new EmptyBorder(0, 0, 0, 5));
        JPanel left_content = new JPanel(new GridLayout(5, 1)); // Thêm 1 hàng cho Top N
        left_content.setPreferredSize(new Dimension(300, 400));
        nhapxuat_left.add(left_content, BorderLayout.NORTH);

        tenkhachhang = new InputForm("Tìm kiếm nhà cung cấp");
        tenkhachhang.getTxtForm().putClientProperty("JTextField.showClearButton", true);
        tenkhachhang.getTxtForm().addKeyListener(this);

        topFilter = new InputForm("Top N (VD: 5, 10, 20)");
        topFilter.getTxtForm().putClientProperty("JTextField.showClearButton", true);
        topFilter.getTxtForm().addKeyListener(this);

        start_date = new InputDate("Từ ngày");
        end_date = new InputDate("Đến ngày");
        start_date.getDateChooser().addPropertyChangeListener(this);
        end_date.getDateChooser().addPropertyChangeListener(this);

        JPanel btn_layout = new JPanel(new BorderLayout());
        JPanel btninner = new JPanel(new GridLayout(1, 2));
        btninner.setOpaque(false);
        btn_layout.setPreferredSize(new Dimension(30, 36));
        btn_layout.setBorder(new EmptyBorder(20, 10, 0, 10));
        btn_layout.setBackground(Color.white);
        export = new ButtonCustom("Xuất Excel", "excel", 14);
        reset = new ButtonCustom("Làm mới", "danger", 14);

        export.addActionListener(this);
        reset.addActionListener(this);

        btninner.add(export);
        btninner.add(reset);
        btn_layout.add(btninner, BorderLayout.NORTH);

        left_content.add(tenkhachhang);
        left_content.add(topFilter);
        left_content.add(start_date);
        left_content.add(end_date);
        left_content.add(btn_layout);

        // Panel chính giữa
        nhapxuat_center = new PanelBorderRadius();
        nhapxuat_center.setLayout(new BorderLayout());

        // Ô thống kê
        pnlTaskbar = new PanelBorderRadius();
        pnlTaskbar.setLayout(new GridLayout(1, 3, 20, 0));
        pnlTaskbar.setOpaque(false);
        pnlTaskbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlTaskbar.setPreferredSize(new Dimension(0, 120));
        listitem = new itemTaskbar[3];
        listitem[0] = new itemTaskbar("supplier.svg", "0", "Tổng nhà cung cấp", 0);
        listitem[1] = new itemTaskbar("tinhhieuqua_128px.svg", "0", "Tổng nhập", 0);
        listitem[2] = new itemTaskbar("1.svg", "0", "Tổng tiền nhập", 0);
        for (itemTaskbar item : listitem) {
            pnlTaskbar.add(item);
        }

        // Bảng
        tblKH = new JTable();
        tblKH.setBackground(new Color(0xA1D6E2));
        scrollTblTonKho = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã nhà cung cấp", "Tên nhà cung cấp", "Số lượng nhập", "Tổng số tiền"};
        tblModel.setColumnIdentifiers(header);
        tblKH.setModel(tblModel);
        tblKH.setAutoCreateRowSorter(true);
        tblKH.setDefaultEditor(Object.class, null);
        scrollTblTonKho.setViewportView(tblKH);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblKH.setDefaultRenderer(Object.class, centerRenderer);
        tblKH.setFocusable(false);
        tblKH.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblKH.getColumnModel().getColumn(1).setPreferredWidth(50);
        tblKH.getColumnModel().getColumn(2).setPreferredWidth(200);

        TableSorter.configureTableColumnSorter(tblKH, 0, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblKH, 1, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblKH, 3, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblKH, 4, TableSorter.VND_CURRENCY_COMPARATOR);

        // Thêm vào panel chính giữa
        nhapxuat_center.add(pnlTaskbar, BorderLayout.NORTH);
        nhapxuat_center.add(scrollTblTonKho, BorderLayout.CENTER);

        this.add(nhapxuat_left, BorderLayout.WEST);
        this.add(nhapxuat_center, BorderLayout.CENTER);
    }

    public void updateTaskbar() {
        int totalSuppliers = list.size();
        long totalImports = list.stream().mapToLong(ThongKeNhaCungCapDTO::getSoluong).sum();
        long totalMoney = list.stream().mapToLong(ThongKeNhaCungCapDTO::getTongtien).sum();
        listitem[0].setValue(String.valueOf(totalSuppliers));
        listitem[1].setValue(String.valueOf(totalImports));
        listitem[2].setValue(Formater.FormatVND(totalMoney));
    }

    public void loadDataTable(ArrayList<ThongKeNhaCungCapDTO> result) {
        tblModel.setRowCount(0);
        int k = 1;
        for (ThongKeNhaCungCapDTO i : result) {
            tblModel.addRow(new Object[]{
                k, i.getMancc(), i.getTenncc(), i.getSoluong(), Formater.FormatVND(i.getTongtien())
            });
            k++;
        }
    }

    public boolean validateSelectDate() throws ParseException {
        java.util.Date time_start = start_date.getDate();
        java.util.Date time_end = end_date.getDate();

        java.util.Date current_date = new java.util.Date();
        if (time_start != null && time_start.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            start_date.getDateChooser().setCalendar(null);
            return false;
        }
        if (time_end != null && time_end.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            end_date.getDateChooser().setCalendar(null);
            return false;
        }
        if (time_start != null && time_end != null && time_start.after(time_end)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải lớn hơn ngày bắt đầu", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            end_date.getDateChooser().setCalendar(null);
            return false;
        }
        return true;
    }

    public void Fillter() throws ParseException {
        if (validateSelectDate()) {
            String input = tenkhachhang.getText() != null ? tenkhachhang.getText() : "";
            java.util.Date time_start = start_date.getDate() != null ? start_date.getDate() : new java.util.Date(0);
            java.util.Date time_end = end_date.getDate() != null ? end_date.getDate() : new java.util.Date(System.currentTimeMillis());
            this.list = thongkebus.FilterNCC(input, new Date(time_start.getTime()), new Date(time_end.getTime()));

            // Lọc top N
            String topText = topFilter.getText().trim();
            int topN = topText.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(topText);
            if (topN > 0) {
                list.sort(Comparator.comparingLong(ThongKeNhaCungCapDTO::getTongtien).reversed());
                if (list.size() > topN) {
                    list = new ArrayList<>(list.subList(0, topN));
                }
            }

            loadDataTable(list);
            updateTaskbar();
        }
    }

    public void resetForm() throws ParseException {
        tenkhachhang.setText("");
        topFilter.setText("");
        start_date.getDateChooser().setCalendar(null);
        end_date.getDateChooser().setCalendar(null);
        Fillter();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == export) {
            try {
                JTableExporter.exportJTableToExcel(tblKH);
            } catch (IOException ex) {
                Logger.getLogger(ThongKeNhaCungCap.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (source == reset) {
            try {
                resetForm();
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeNhaCungCap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            Fillter();
        } catch (ParseException ex) {
            Logger.getLogger(ThongKeNhaCungCap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            Fillter();
        } catch (ParseException ex) {
            Logger.getLogger(ThongKeNhaCungCap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}