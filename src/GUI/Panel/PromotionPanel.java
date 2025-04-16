package GUI.Panel;

import BUS.PromotionBUS;
import DTO.Promotion;
import GUI.Components.RoundedButton;
import GUI.Dialog.QuanLyComboDialog;
import GUI.Dialog.ThemKhuyenMaiDialog;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PromotionPanel extends JPanel {
    private JTable tableKhuyenMai;
    private DefaultTableModel tableModel;
    private RoundedButton btnThemMaKM, btnXoaMaKM, btnSuaMaKM, btnTaoCombo;
    private JTextField txtMaKM, txtTenKM, txtPhanTramKM, txtNgayBD, txtNgayKT, txtLoai;
    private JPanel buttonPanel, inputPanel;
    private PromotionBUS promotionBUS;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PromotionPanel() {
        System.out.println("Initializing PromotionPanel");
        try {
            promotionBUS = new PromotionBUS();
            setPreferredSize(new Dimension(1080, 770));
            setBackground(Color.YELLOW);
            setLayout(new BorderLayout());
            initComponents();
            loadData();
        } catch (Exception e) {
            System.err.println("Failed to initialize PromotionPanel: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu khuyến mãi do lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            initEmptyPanel();
        }
    }

    private void initEmptyPanel() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.YELLOW);
        JLabel errorLabel = new JLabel("Không thể tải dữ liệu khuyến mãi. Vui lòng kiểm tra cơ sở dữ liệu!", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(errorLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(404, 120));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        btnThemMaKM = new RoundedButton("Thêm mã KM", 20, new ImageIcon("src/resources/icon/add.png"));
        btnXoaMaKM = new RoundedButton("Xóa mã KM", 20, new ImageIcon("src/resources/icon/delete.png"));
        btnSuaMaKM = new RoundedButton("Sửa mã KM", 20, new ImageIcon("src/resources/icon/edit.png"));
        btnTaoCombo = new RoundedButton("Tạo combo", 20, new ImageIcon("src/resources/icon/createcombo.png"));

        Dimension buttonSize = new Dimension(187, 40);
        for (RoundedButton btn : new RoundedButton[]{btnThemMaKM, btnXoaMaKM, btnSuaMaKM, btnTaoCombo}) {
            btn.setPreferredSize(buttonSize);
            btn.setBackground(new Color(0, 255, 255));
            btn.setForeground(Color.BLACK);
            buttonPanel.add(btn);
        }

        inputPanel = new JPanel(new BorderLayout());
        inputPanel.setPreferredSize(new Dimension(616, 120));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tenKMPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel jlbTenKM = new JLabel("Tên KM:");
        jlbTenKM.setPreferredSize(new Dimension(100, 25));
        txtTenKM = new JTextField();
        txtTenKM.setPreferredSize(new Dimension(496, 25));
        txtTenKM.setToolTipText("Nhập tên khuyến mãi");
        txtTenKM.setFont(new Font("Arial", Font.PLAIN, 13));
        txtTenKM.setBackground(Color.LIGHT_GRAY);
        txtTenKM.setForeground(Color.BLUE);
        tenKMPanel.add(jlbTenKM);
        tenKMPanel.add(txtTenKM);
        inputPanel.add(tenKMPanel, BorderLayout.NORTH);

        JPanel subInputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        txtMaKM = new JTextField();
        txtPhanTramKM = new JTextField();
        txtNgayBD = new JTextField();
        txtNgayKT = new JTextField();
        txtLoai = new JTextField();
        txtLoai.setEditable(false);

        subInputPanel.add(new JLabel("Mã KM:"));
        subInputPanel.add(txtMaKM);
        subInputPanel.add(new JLabel("Phần trăm KM:"));
        subInputPanel.add(txtPhanTramKM);
        subInputPanel.add(new JLabel("Ngày BĐ:"));
        subInputPanel.add(txtNgayBD);
        subInputPanel.add(new JLabel("Ngày KT:"));
        subInputPanel.add(txtNgayKT);

        inputPanel.add(subInputPanel, BorderLayout.SOUTH);
        topPanel.add(inputPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        String[] columnNames = {"Mã KM", "Tên KM", "Phần trăm KM", "Ngày BĐ", "Ngày KT", "Loại"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableKhuyenMai = new JTable(tableModel);
        tableKhuyenMai.setPreferredScrollableViewportSize(new Dimension(930, 525));
        tableKhuyenMai.getTableHeader().setReorderingAllowed(false);

        tableKhuyenMai.getColumnModel().getColumn(5).setCellRenderer(new TypeColumnRenderer());

        tableKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tableKhuyenMai.columnAtPoint(e.getPoint());
                int row = tableKhuyenMai.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    // Cập nhật các text field khi chọn dòng
                    txtMaKM.setText((String) tableModel.getValueAt(row, 0));
                    txtTenKM.setText((String) tableModel.getValueAt(row, 1));
                    txtPhanTramKM.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                    txtNgayBD.setText((String) tableModel.getValueAt(row, 3));
                    txtNgayKT.setText((String) tableModel.getValueAt(row, 4));
                    txtLoai.setText((String) tableModel.getValueAt(row, 5));

                    // Nếu nhấp vào cột "Loại" và là "Combo", mở dialog chi tiết
                    if (column == 5 && "Combo".equals(tableModel.getValueAt(row, 5))) {
                        new QuanLyComboDialog((Frame) SwingUtilities.getWindowAncestor(PromotionPanel.this),
                                (String) tableModel.getValueAt(row, 0)).setVisible(true);
                        loadData();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableKhuyenMai);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        addActionListeners();
    }

    private void addActionListeners() {
        btnThemMaKM.addActionListener(e -> {
            new ThemKhuyenMaiDialog((Frame) SwingUtilities.getWindowAncestor(this), null).setVisible(true);
            loadData();
        });

        btnSuaMaKM.addActionListener(e -> {
            int row = tableKhuyenMai.getSelectedRow();
            if (row >= 0) {
                String id = (String) tableModel.getValueAt(row, 0);
                Promotion p = promotionBUS.getAllPromotions().stream()
                        .filter(prom -> prom.getIdKhuyenMai().equals(id)).findFirst().orElse(null);
                new ThemKhuyenMaiDialog((Frame) SwingUtilities.getWindowAncestor(this), p).setVisible(true);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn mã KM cần sửa!");
            }
        });

        btnXoaMaKM.addActionListener(e -> {
            int row = tableKhuyenMai.getSelectedRow();
            if (row >= 0) {
                String id = (String) tableModel.getValueAt(row, 0);
                if (promotionBUS.deletePromotion(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn mã KM cần xóa!");
            }
        });

        btnTaoCombo.addActionListener(e -> {
            new ThemKhuyenMaiDialog((Frame) SwingUtilities.getWindowAncestor(this), null, true).setVisible(true);
            loadData();
        });
    }

    public void loadData() {
        try {
            tableModel.setRowCount(0);
            List<Promotion> promotions = promotionBUS.getAllPromotions();
            System.out.println("Loaded " + promotions.size() + " promotions");
            for (Promotion p : promotions) {
                System.out.println("Adding promotion: " + p.getTenKhuyenMai());
                tableModel.addRow(new Object[]{
                    p.getIdKhuyenMai(),
                    p.getTenKhuyenMai(),
                    p.getGiaTri(),
                    p.getNgayBatDau() != null ? p.getNgayBatDau().format(dateFormatter) : "",
                    p.getNgayKetThuc() != null ? p.getNgayKetThuc().format(dateFormatter) : "",
                    p.getLoai()
                });
            }
        } catch (Exception e) {
            System.err.println("Error loading promotions: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class TypeColumnRenderer extends DefaultTableCellRenderer {
        private JLabel eyeLabel;
        private ImageIcon eyeIcon;

        public TypeColumnRenderer() {
            eyeLabel = new JLabel();
            ImageIcon originalIcon = new ImageIcon("src/resources/icon/eye_icon.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(16, 20, Image.SCALE_SMOOTH);
            eyeIcon = new ImageIcon(scaledImage);
            eyeLabel.setIcon(eyeIcon);
            eyeLabel.setPreferredSize(new Dimension(16, 16));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel textLabel = new JLabel((String) value);
            textLabel.setHorizontalAlignment(JLabel.LEFT);

            if ("Combo".equals(value)) {
                panel.add(textLabel, BorderLayout.CENTER);
                panel.add(eyeLabel, BorderLayout.EAST);
            } else {
                panel.add(textLabel, BorderLayout.CENTER);
            }

            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
                textLabel.setForeground(table.getSelectionForeground());
            } else {
                panel.setBackground(table.getBackground());
                textLabel.setForeground(table.getForeground());
            }

            return panel;
        }
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