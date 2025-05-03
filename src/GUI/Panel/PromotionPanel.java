package GUI.Panel;

import BUS.PromotionBUS;
import DTO.Promotion;
import GUI.Components.MenuChucNang;
import GUI.Dialog.ComboDetailDialog;
import GUI.Dialog.ThemKhuyenMaiDialog;
import GUI.Main;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PromotionPanel extends JPanel {
    private JTable tableKhuyenMai;
    private DefaultTableModel tableModel;
    private JPanel tablePanel;
    private PromotionBUS promotionBUS;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Main mainFrame;
    private JTextField searchField;
    private JButton btnSearch;
    private JButton btnReset;

    public PromotionPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        System.out.println("Initializing PromotionPanel");
        try {
            promotionBUS = new PromotionBUS();
            setPreferredSize(new Dimension(1080, 770));
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            initComponents();
        } catch (Exception e) {
            System.err.println("Failed to initialize PromotionPanel: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu khuyến mãi do lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            initEmptyPanel();
        }
    }

    private void initEmptyPanel() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel errorLabel = new JLabel("Không thể tải dữ liệu khuyến mãi. Vui lòng kiểm tra cơ sở dữ liệu!", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(errorLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initComponents() {
        // Thanh chức năng và tìm kiếm
        JPanel topPanel = createPromotionToolbar();

        // Bảng khuyến mãi
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        String[] columnNames = {"Mã KM", "Tên KM", "Phần trăm KM", "Ngày BĐ", "Ngày KT", "Loại"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableKhuyenMai = new JTable(tableModel);
        tableKhuyenMai.setPreferredScrollableViewportSize(new Dimension(930, 525));
        tableKhuyenMai.getTableHeader().setReorderingAllowed(false);

        // Thiết lập header bảng
        JTableHeader header = tableKhuyenMai.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableKhuyenMai.getColumnCount(); i++) {
            tableKhuyenMai.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Renderer cho cột "Loại"
        tableKhuyenMai.getColumnModel().getColumn(5).setCellRenderer(new TypeColumnRenderer());

        // Thiết lập bảng
        tableKhuyenMai.setShowGrid(false);
        tableKhuyenMai.setRowHeight(30);
        tableKhuyenMai.setSelectionBackground(new Color(173, 216, 230));
        tableKhuyenMai.setSelectionForeground(Color.BLACK);

        // Thêm MouseListener để xử lý nhấn chuột vào cột "Loại"
        tableKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableKhuyenMai.rowAtPoint(e.getPoint());
                int col = tableKhuyenMai.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 5) { // Cột "Loại"
                    int modelRow = tableKhuyenMai.convertRowIndexToModel(row);
                    String value = tableModel.getValueAt(modelRow, 5).toString().trim(); // Loại bỏ khoảng trắng
                    System.out.println("Clicked on Loai column, value: '" + value + "'");
                    if ("Combo".equalsIgnoreCase(value)) { // Bỏ qua hoa thường
                        String idKhuyenMai = tableModel.getValueAt(modelRow, 0).toString();
                        System.out.println("Opening ComboDetailDialog for ID: " + idKhuyenMai);
                        new ComboDetailDialog(PromotionPanel.this, idKhuyenMai).setVisible(true);
                    }
                }
            }
        });

        // Thêm MouseMotionListener để đổi con trỏ chuột khi di chuột qua cột "Loại"
        tableKhuyenMai.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = tableKhuyenMai.rowAtPoint(e.getPoint());
                int col = tableKhuyenMai.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 5) { // Cột "Loại"
                    int modelRow = tableKhuyenMai.convertRowIndexToModel(row);
                    String value = tableModel.getValueAt(modelRow, 5).toString().trim();
                    if ("Combo".equalsIgnoreCase(value)) {
                        tableKhuyenMai.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        tableKhuyenMai.setCursor(Cursor.getDefaultCursor());
                    }
                } else {
                    tableKhuyenMai.setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableKhuyenMai);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Tải dữ liệu ngay từ đầu
        loadData();
    }

    public JPanel createPromotionToolbar() {
        JPanel toolbar = new JPanel(new GridLayout(1, 2, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolbar.setPreferredSize(new Dimension(950, 110));

        // Thanh chức năng
        MenuChucNang menu = new MenuChucNang();
        JPanel actionPanel = menu.createActionPanel(this, mainFrame);
        toolbar.add(actionPanel);

        // Thanh tìm kiếm
        JPanel searchPanel = MenuChucNang.createSearchPanel();
        toolbar.add(searchPanel);

        // Lấy tham chiếu đến các thành phần trong searchPanel để xử lý tìm kiếm
        Component[] components = searchPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("Tìm kiếm")) {
                    btnSearch = button;
                } else if (button.getText().equals("Làm mới")) {
                    btnReset = button;
                }
            } else if (comp instanceof JTextField) {
                searchField = (JTextField) comp;
            }
        }

        // Gán sự kiện tìm kiếm và làm mới
        btnSearch.addActionListener(e -> searchPromotions());
        btnReset.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });

        return toolbar;
    }

    private void searchPromotions() {
        String keyword = searchField.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            loadData();
            return;
        }
        List<Promotion> promotions = promotionBUS.getAllPromotions().stream()
                .filter(p -> p.getIdKhuyenMai().toLowerCase().contains(keyword.toLowerCase()) ||
                             p.getTenKhuyenMai().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        tableModel.setRowCount(0);
        for (Promotion p : promotions) {
            tableModel.addRow(new Object[]{
                p.getIdKhuyenMai(),
                p.getTenKhuyenMai(),
                p.getGiaTri(),
                p.getNgayBatDau() != null ? p.getNgayBatDau().format(dateFormatter) : "",
                p.getNgayKetThuc() != null ? p.getNgayKetThuc().format(dateFormatter) : "",
                p.getLoai()
            });
        }
    }

    public void openAddPromotionDialog() {
        new ThemKhuyenMaiDialog(this, null).setVisible(true);
    }

    public void openEditPromotionDialog(String id) {
        Promotion p = promotionBUS.getAllPromotions().stream()
                .filter(prom -> prom.getIdKhuyenMai().equals(id)).findFirst().orElse(null);
        if (p != null) {
            new ThemKhuyenMaiDialog(this, p).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khuyến mãi với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openRemovePromotionDialog(String id) {
        if (promotionBUS.deletePromotion(id)) {
            JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thất bại!");
        }
    }

    public String getSelectedPromotionId() {
        int row = getSelectedRow();
        if (row >= 0) {
            return getValueAt(row, 0).toString();
        }
        return "-1";
    }

    public int getSelectedRow() {
        return tableKhuyenMai.getSelectedRow();
    }

    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
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
        private ImageIcon eyeIcon;

        public TypeColumnRenderer() {
            ImageIcon originalIcon = new ImageIcon("src/resources/icon/eye_icon.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(16, 20, Image.SCALE_SMOOTH);
            eyeIcon = new ImageIcon(scaledImage);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel textLabel = new JLabel((String) value);
            textLabel.setHorizontalAlignment(JLabel.CENTER);

            if ("Combo".equals(value)) {
                // Tạo eyeLabel mới cho mỗi ô
                JLabel eyeLabel = new JLabel();
                eyeLabel.setIcon(eyeIcon);
                eyeLabel.setPreferredSize(new Dimension(16, 16));

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