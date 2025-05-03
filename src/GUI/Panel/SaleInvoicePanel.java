package GUI.Panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BUS.InvoiceBUS;
import DTO.SalesInvoice;
import GUI.Main;
import GUI.Components.MenuChucNang;

public class SaleInvoicePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String selectedPhieuId = "-1";
    private Main mainFrame;
    private InvoiceBUS bus;

    public SaleInvoicePanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.bus = new InvoiceBUS();
        initComponent();
        addTableSelectionListener();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Color.WHITE);

        add(createCustomToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createCustomToolbar() {
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

        String[] columns = {"ID Hóa đơn xuất", "ID Nhân viên", "ID Khách hàng", "Ngày tạo", "Tổng tiền", "ID Khuyến mãi"};
        tableModel = new DefaultTableModel(columns, 0);

        List<SalesInvoice> danhSach = bus.fetchSalesInvoice();
        for (SalesInvoice p : danhSach) {
            Object[] row = {
                    p.getId(),
                    p.getEid(),
                    p.getCid(),
                    p.getDate(),
                    String.format("%,.0f VND", p.getTotalPayment()),
                    p.getDid()
            };
            tableModel.addRow(row);
        }

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public String getSelectedPhieuId() {
        return selectedPhieuId;
    }

    public void addTableSelectionListener() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedPhieuId = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }

    public void refreshTable() {
         tableModel.setRowCount(0);
         List<SalesInvoice> danhSach = bus.fetchSalesInvoice();
         for (SalesInvoice p : danhSach) {
            Object[] row = {
                    p.getId(),
                    p.getEid(),
                    p.getCid(),
                    p.getDate(),
                    String.format("%,.0f VND", p.getTotalPayment()),
                    p.getDid()
            };
            tableModel.addRow(row);
        }

    }
}
