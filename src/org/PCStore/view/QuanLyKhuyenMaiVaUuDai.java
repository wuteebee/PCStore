// tui thấy mainframe chưa có click vào quản lý khuyến mãi nên tui để setvisible để hiển thị riêng giao diện của quản lý khuyến mãi
// và ưu đãi á, background tui chưa có set tại chưa biết màu chủ đạo của mình nên tui thêm sau
package org.PCStore.view;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class QuanLyKhuyenMaiVaUuDai extends JFrame {

    // Khai báo các thành phần giao diện và dữ liệu
    private JTable tableKhuyenMai;
    private DefaultTableModel tableModel;
    private JButton btnThemMaKM, btnXoaMaKM, btnSuaMaKM, btnTaoCombo;
    private JTextField txtMaKM, txtTenKM, txtPhanTramKM, txtNgayBD, txtNgayKT, txtLoai;
    private JLabel jlbTenKM;
    private JPanel buttonPanel;
    private JPanel inputPanel;
    private Map<String, Object> khuyenMaiMap; 
    private JDialog comboDialog, createCombo;
    private boolean isAddingMode = false; // Trạng thái: đang ở chế độ thêm sản phẩm
    private boolean isEditingMode = false; // Trạng thái: đang ở chế độ sửa sản phẩm
    private List<Map<String, String>> danhSachSanPham = new ArrayList<>(); // Danh sách sản phẩm
    private JTextField txtMaKMDialog; // Tham chiếu đến JTextField mã KM
    private JTextField txtTenKMComboDialog, txtMaKMComboDialog, txtGiaTriKMComboDialog;
    private JTextField txtNgayBDComboDialog, txtNgayKTComboDialog;
    private DefaultTableModel selectedProductsTableModelDialog; // Tham chiếu đến table model

    // Constructor: Khởi tạo giao diện
    public QuanLyKhuyenMaiGiaoDienMoi() {   
        khuyenMaiMap = new HashMap<>();
        khoiTaoSanPhamMau(); // Khởi tạo danh sách sản phẩm mẫu
        
        setTitle("Quản lý khuyến mãi - Giao diện mới");
        setSize(1080, 770);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(404, 120));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        btnThemMaKM = new JButton("Thêm mã KM");
        btnXoaMaKM = new JButton("Xóa mã KM");
        btnSuaMaKM = new JButton("Sửa mã KM");
        btnTaoCombo = new JButton("Tạo combo");

        Dimension buttonSize = new Dimension(187, 40);
        
        for (JButton btn : new JButton[]{btnThemMaKM, btnXoaMaKM, btnSuaMaKM, btnTaoCombo}) {
            btn.setPreferredSize(buttonSize);
            buttonPanel.add(btn);
        }

        inputPanel = new JPanel(new BorderLayout());
        inputPanel.setPreferredSize(new Dimension(616, 120));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tenKMPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jlbTenKM = new JLabel("Tên KM:");
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
        subInputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        subInputPanel.setPreferredSize(new Dimension(596, 65));

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

        JPanel spacingPanel = new JPanel();
        spacingPanel.setPreferredSize(new Dimension(3, 120));
        inputPanel.add(spacingPanel, BorderLayout.CENTER);
        inputPanel.add(subInputPanel, BorderLayout.SOUTH);

        topPanel.add(inputPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        String[] columnNames = {"Mã KM", "Tên KM", "Phần trăm KM", "Ngày BĐ", "Ngày KT", "Loại"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableKhuyenMai = new JTable(tableModel);
        tableKhuyenMai.setPreferredScrollableViewportSize(new Dimension(930, 525));
        
        // Áp dụng renderer tùy chỉnh cho cột "Loại"
        tableKhuyenMai.getColumnModel().getColumn(5).setCellRenderer(new TypeColumnRenderer());
        
        // Thêm sự kiện nhấp chuột vào bảng để mở dialog khi nhấp vào ô "Combo"
        tableKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tableKhuyenMai.columnAtPoint(e.getPoint());
                int row = tableKhuyenMai.rowAtPoint(e.getPoint());
                if (column == 5 && "Combo".equals(tableModel.getValueAt(row, 5))) {
                    showComboDialog(); // Mở dialog khi nhấp vào ô "Combo"
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableKhuyenMai);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        addSampleData();
        addActionListeners();
        addInputFieldListeners();
    }

    // Renderer tùy chỉnh cho cột "Loại" với biểu tượng con mắt chỉ để hiển thị
    private class TypeColumnRenderer extends DefaultTableCellRenderer {
        private JLabel eyeLabel; // Biến riêng cho biểu tượng con mắt
        private ImageIcon eyeIcon; // Icon đã được scale

        public TypeColumnRenderer() {
            eyeLabel = new JLabel();
            // Tải và scale ảnh con mắt xuống kích thước 16x16
            ImageIcon originalIcon = new ImageIcon("img//eye_icon.png"); // Đường dẫn tới ảnh con mắt
            Image scaledImage = originalIcon.getImage().getScaledInstance(16, 20, Image.SCALE_SMOOTH);
            eyeIcon = new ImageIcon(scaledImage);
            eyeLabel.setIcon(eyeIcon);
            eyeLabel.setPreferredSize(new Dimension(16, 16)); // Kích thước cố định cho biểu tượng
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel textLabel = new JLabel((String) value);
            textLabel.setHorizontalAlignment(JLabel.LEFT);

            if ("Combo".equals(value)) {
                panel.add(textLabel, BorderLayout.CENTER);
                panel.add(eyeLabel, BorderLayout.EAST); // Đặt con mắt bên phải (chỉ để hiển thị)
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

    // Danh sách sản phẩm giả lập (thay thế bằng dữ liệu từ bảng SanPham)
    private void khoiTaoSanPhamMau() {
        Map<String, String> sp1 = new HashMap<>();
        sp1.put("idSanPham", "SP001");
        sp1.put("tenSanPham", "Laptop Dell XPS 13");
        danhSachSanPham.add(sp1);

        Map<String, String> sp2 = new HashMap<>();
        sp2.put("idSanPham", "SP002");
        sp2.put("tenSanPham", "Chuột Logitech MX Master");
        danhSachSanPham.add(sp2);

        Map<String, String> sp3 = new HashMap<>();
        sp3.put("idSanPham", "SP003");
        sp3.put("tenSanPham", "Bàn phím Keychron K8");
        danhSachSanPham.add(sp3);
    }

    // Thêm dữ liệu mẫu
    private void addSampleData() {
        String[] data1 = {"KM001", "Giảm giá mùa hè", "10%", "2025-06-01", "2025-06-30", "Đơn lẻ"};
        String[] data2 = {"KM002", "Combo laptop", "15%", "2025-07-01", "2025-07-15", "Combo"};
        khuyenMaiMap.put("KM001", data1);
        khuyenMaiMap.put("KM002", data2);
        tableModel.addRow(data1);
        tableModel.addRow(data2);
    }

    // Thêm sự kiện cho các nút chức năng
    private void addActionListeners() {
        tableKhuyenMai.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tableKhuyenMai.getSelectedRow();
            if (selectedRow >= 0) {
                String maKM = (String) tableModel.getValueAt(selectedRow, 0);
                Object dataObj = khuyenMaiMap.get(maKM);
                if (dataObj instanceof String[]) {
                    String[] data = (String[]) dataObj;
                    String loai = data[5];
                    if ("Đơn lẻ".equals(loai) || "Combo".equals(loai)) {
                        txtMaKM.setText(data[0]);
                        txtTenKM.setText(data[1]);
                        txtPhanTramKM.setText(data[2]);
                        txtNgayBD.setText(data[3]);
                        txtNgayKT.setText(data[4]);
                        txtLoai.setText(loai);
                    }
                }
            }
        });

        btnThemMaKM.addActionListener(e -> {
            resetInputFields();
            txtLoai.setText("Đơn lẻ");
            isAddingMode = true;
            showCompleteCancelButtons(false);
        });

        btnTaoCombo.addActionListener(e -> {
            resetInputFields();
            txtLoai.setText("Combo");
            isAddingMode = true;
            showCreateComboDialog();
            restoreDefaultButtons();
        });

        btnSuaMaKM.addActionListener(e -> {
            int selectedRow = tableKhuyenMai.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn mã KM cần sửa trước!");
            } else {
                isEditingMode = true;
                showCompleteCancelButtons(true);
            }
        });

        btnXoaMaKM.addActionListener(e -> {
            int selectedRow = tableKhuyenMai.getSelectedRow();
            if (selectedRow >= 0) {
                String maKM = (String) tableModel.getValueAt(selectedRow, 0);
                khuyenMaiMap.remove(maKM);
                khuyenMaiMap.remove(maKM + "_products"); // Xóa danh sách sản phẩm liên quan (nếu có)
                tableModel.removeRow(selectedRow);
                resetInputFields();
            }
        });
    }

    // Thêm sự kiện khi nhấp chuột vào ô nhập liệu
    private void addInputFieldListeners() {
        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isAddingMode && !isEditingMode) {
                    boolean isEmpty = txtMaKM.getText().isEmpty() && txtTenKM.getText().isEmpty() &&
                            txtPhanTramKM.getText().isEmpty() && txtNgayBD.getText().isEmpty() &&
                            txtNgayKT.getText().isEmpty() && txtLoai.getText().isEmpty();
                    
                    if (isEmpty) {
                        JOptionPane.showMessageDialog(QuanLyKhuyenMaiGiaoDienMoi.this, 
                            "Muốn thêm sản phẩm? Hãy ấn nút Thêm sản phẩm.");
                    } else {
                        JOptionPane.showMessageDialog(QuanLyKhuyenMaiGiaoDienMoi.this, 
                            "Muốn sửa sản phẩm? Hãy ấn nút Sửa sản phẩm.");
                    }
                }
            }
        };

        txtMaKM.addMouseListener(mouseListener);
        txtTenKM.addMouseListener(mouseListener);
        txtPhanTramKM.addMouseListener(mouseListener);
        txtNgayBD.addMouseListener(mouseListener);
        txtNgayKT.addMouseListener(mouseListener);
        txtLoai.addMouseListener(mouseListener);
    }

    // Reset các ô nhập liệu
    private void resetInputFields() {
        txtMaKM.setText("");
        txtTenKM.setText("");
        txtPhanTramKM.setText("");
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        txtLoai.setText("");
    }

    // Hiển thị nút "Hoàn tất" và "Hủy"
    private void showCompleteCancelButtons(boolean isEditMode) {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        
        JButton btnHoanTat = new JButton("Hoàn tất");
        JButton btnHuy = new JButton("Hủy");
        Dimension buttonSize = new Dimension(187, 40);
        btnHoanTat.setPreferredSize(buttonSize);
        btnHuy.setPreferredSize(buttonSize);

        btnHoanTat.addActionListener(e -> {
            String maKM = txtMaKM.getText();
            if (!maKM.isEmpty()) {
                String[] data = {
                    maKM, txtTenKM.getText(), txtPhanTramKM.getText(),
                    txtNgayBD.getText(), txtNgayKT.getText(), txtLoai.getText()
                };
                int selectedRow = tableKhuyenMai.getSelectedRow();
                if (isEditMode && selectedRow >= 0) {
                    String oldMaKM = (String) tableModel.getValueAt(selectedRow, 0);
                    khuyenMaiMap.remove(oldMaKM);
                    tableModel.removeRow(selectedRow);
                }
                khuyenMaiMap.put(maKM, data);
                tableModel.addRow(data);
                resetInputFields();
                isAddingMode = false;
                isEditingMode = false;
                restoreDefaultButtons();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã KM!");
            }
        });

        btnHuy.addActionListener(e -> {
            resetInputFields();
            isAddingMode = false;
            isEditingMode = false;
            restoreDefaultButtons();
        });

        buttonPanel.add(btnHoanTat);
        buttonPanel.add(btnHuy);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    // Khôi phục 4 nút ban đầu
    private void restoreDefaultButtons() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
        Dimension buttonSize = new Dimension(187, 40);
        btnThemMaKM.setPreferredSize(buttonSize);
        btnXoaMaKM.setPreferredSize(buttonSize);
        btnSuaMaKM.setPreferredSize(buttonSize);
        btnTaoCombo.setPreferredSize(buttonSize);
        buttonPanel.add(btnThemMaKM);
        buttonPanel.add(btnXoaMaKM);
        buttonPanel.add(btnSuaMaKM);
        buttonPanel.add(btnTaoCombo);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    
    
    private void showComboDialog() {
        if (comboDialog == null) { // Chỉ tạo dialog một lần
            comboDialog = new JDialog(this, "Danh sách Combo", true);
            comboDialog.setSize(593, 666); // Kích thước giống showCreateComboDialog
            comboDialog.setLocationRelativeTo(this);
            comboDialog.setLayout(new BorderLayout());
    
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
            // 1. Thanh mã KM ban đầu là JTextField
            JPanel comboTopPanel = new JPanel(new BorderLayout(10, 0));
            JLabel lblTenKM = new JLabel("Tên KM:");
            lblTenKM.setPreferredSize(new Dimension(50, 41));
            txtMaKMDialog = new JTextField(); // Khởi tạo tham chiếu
            txtMaKMDialog.setPreferredSize(new Dimension(510, 41));
            txtMaKMDialog.setEditable(false); // Khóa chỉnh sửa ban đầu
            comboTopPanel.add(lblTenKM, BorderLayout.WEST);
            comboTopPanel.add(txtMaKMDialog, BorderLayout.CENTER);
            mainPanel.add(comboTopPanel, BorderLayout.NORTH);
    
            // 2. Phần bên dưới chia đôi
            JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
    
            // 2.1. Bên trái: Các ô nhập liệu và bảng sản phẩm đã chọn
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
            JPanel leftFieldsPanel = new JPanel(new GridLayout(4, 1, 0, 20));
            txtTenKMComboDialog = new JTextField(); // Khởi tạo tham chiếu
            txtTenKMComboDialog.setPreferredSize(new Dimension(206, 41));
            txtTenKMComboDialog.setBackground(Color.LIGHT_GRAY);
            txtTenKMComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtTenKMComboDialog.setBorder(BorderFactory.createTitledBorder("Tên KM"));
            txtTenKMComboDialog.setEditable(false); // Khóa ban đầu
    
            txtMaKMComboDialog = new JTextField(); // Khởi tạo tham chiếu
            txtMaKMComboDialog.setPreferredSize(new Dimension(206, 41));
            txtMaKMComboDialog.setBackground(Color.LIGHT_GRAY);
            txtMaKMComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtMaKMComboDialog.setBorder(BorderFactory.createTitledBorder("Mã KM"));
            txtMaKMComboDialog.setEditable(false); // Khóa ban đầu
    
            txtGiaTriKMComboDialog = new JTextField(); // Khởi tạo tham chiếu
            txtGiaTriKMComboDialog.setPreferredSize(new Dimension(206, 41));
            txtGiaTriKMComboDialog.setBackground(Color.LIGHT_GRAY);
            txtGiaTriKMComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtGiaTriKMComboDialog.setBorder(BorderFactory.createTitledBorder("Giá trị khuyến mãi"));
            txtGiaTriKMComboDialog.setEditable(false); // Khóa ban đầu
    
            JButton btnChonSanPham = new JButton("Chọn sản phẩm");
            btnChonSanPham.setPreferredSize(new Dimension(206, 41));
            btnChonSanPham.setVisible(false); // Ẩn nút ban đầu
    
            leftFieldsPanel.add(txtTenKMComboDialog);
            leftFieldsPanel.add(txtMaKMComboDialog);
            leftFieldsPanel.add(txtGiaTriKMComboDialog);
            leftFieldsPanel.add(btnChonSanPham);
    
            String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm"};
            selectedProductsTableModelDialog = new DefaultTableModel(columnNames, 0); // Khởi tạo tham chiếu
            JTable selectedProductsTable = new JTable(selectedProductsTableModelDialog);
            JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsTable);
    
            leftPanel.add(leftFieldsPanel, BorderLayout.NORTH);
            leftPanel.add(selectedProductsScrollPane, BorderLayout.CENTER);
            bottomPanel.add(leftPanel);
    
            // 2.2. Bên phải: Ngày BĐ, Ngày KT và nút Sửa combo
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
            JPanel rightFieldsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
            txtNgayBDComboDialog = new JTextField(); // Khởi tạo tham chiếu
            txtNgayBDComboDialog.setPreferredSize(new Dimension(206, 41));
            txtNgayBDComboDialog.setBackground(Color.LIGHT_GRAY);
            txtNgayBDComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtNgayBDComboDialog.setBorder(BorderFactory.createTitledBorder("Ngày BĐ"));
            txtNgayBDComboDialog.setEditable(false); // Khóa ban đầu
    
            txtNgayKTComboDialog = new JTextField(); // Khởi tạo tham chiếu
            txtNgayKTComboDialog.setPreferredSize(new Dimension(206, 41));
            txtNgayKTComboDialog.setBackground(Color.LIGHT_GRAY);
            txtNgayKTComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtNgayKTComboDialog.setBorder(BorderFactory.createTitledBorder("Ngày KT"));
            txtNgayKTComboDialog.setEditable(false); // Khóa ban đầu
    
            rightFieldsPanel.add(txtNgayBDComboDialog);
            rightFieldsPanel.add(txtNgayKTComboDialog);
    
            JButton btnSuaCombo = new JButton("Sửa combo");
            btnSuaCombo.setPreferredSize(new Dimension(100, 40));
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(btnSuaCombo);
    
            rightPanel.add(rightFieldsPanel, BorderLayout.NORTH);
            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            bottomPanel.add(rightPanel);
    
            mainPanel.add(bottomPanel, BorderLayout.CENTER); // Thêm bottomPanel vào mainPanel
    
            // Sự kiện nút "Chọn sản phẩm"
            btnChonSanPham.addActionListener(e -> {
                JDialog selectProductDialog = new JDialog(comboDialog, "Chọn sản phẩm", true);
                selectProductDialog.setSize(500, 500);
                selectProductDialog.setLocationRelativeTo(comboDialog);
                selectProductDialog.setLayout(new BorderLayout());
    
                String[] productColumns = {"Mã sản phẩm", "Tên sản phẩm"};
                DefaultTableModel productTableModel = new DefaultTableModel(productColumns, 0);
                for (Map<String, String> product : danhSachSanPham) {
                    productTableModel.addRow(new Object[]{product.get("idSanPham"), product.get("tenSanPham")});
                }
                JTable productTable = new JTable(productTableModel);
                productTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                JScrollPane productScrollPane = new JScrollPane(productTable);
    
                JPanel selectButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton btnHoanTatChonSP = new JButton("Hoàn tất");
                btnHoanTatChonSP.setPreferredSize(new Dimension(100, 30));
                selectButtonPanel.add(btnHoanTatChonSP);
    
                selectProductDialog.add(selectButtonPanel, BorderLayout.NORTH);
                selectProductDialog.add(productScrollPane, BorderLayout.CENTER);
    
                btnHoanTatChonSP.addActionListener(e1 -> {
                    selectedProductsTableModelDialog.setRowCount(0);
                    int[] selectedRows = productTable.getSelectedRows();
                    for (int row : selectedRows) {
                        String idSanPham = (String) productTable.getValueAt(row, 0);
                        String tenSanPham = (String) productTable.getValueAt(row, 1);
                        selectedProductsTableModelDialog.addRow(new Object[]{idSanPham, tenSanPham});
                    }
                    selectProductDialog.dispose();
                });
    
                selectProductDialog.setVisible(true);
            });
    
            // Sự kiện nút "Sửa combo"
            btnSuaCombo.addActionListener(e -> {
                if (btnSuaCombo.getText().equals("Sửa combo")) {
                    btnSuaCombo.setText("Hoàn tất");
    
                    // Thay JTextField bằng JComboBox
                    comboTopPanel.remove(txtMaKMDialog);
                    DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
                    comboModel.addElement("-- Chọn mã khuyến mãi --");
                    for (Map.Entry<String, Object> entry : khuyenMaiMap.entrySet()) {
                        Object value = entry.getValue();
                        if (value instanceof String[] && "Combo".equals(((String[]) value)[5])) {
                            String[] data = (String[]) value;
                            comboModel.addElement(data[0] + " - " + data[1]);
                        }
                    }
                    JComboBox<String> maKMComboBox = new JComboBox<>(comboModel);
                    maKMComboBox.setPreferredSize(new Dimension(510, 41));
                    maKMComboBox.setBackground(Color.LIGHT_GRAY);
                    maKMComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
                    comboTopPanel.add(maKMComboBox, BorderLayout.CENTER);
    
                    // Bật chỉnh sửa các ô nhập liệu
                    txtTenKMComboDialog.setEditable(true);
                    txtMaKMComboDialog.setEditable(true);
                    txtGiaTriKMComboDialog.setEditable(true);
                    txtNgayBDComboDialog.setEditable(true);
                    txtNgayKTComboDialog.setEditable(true);
                    btnChonSanPham.setVisible(true); // Hiện nút chọn sản phẩm
    
                    // Sự kiện khi chọn mã từ JComboBox
                    maKMComboBox.addActionListener(e1 -> {
                        String selectedMaKM = (String) maKMComboBox.getSelectedItem();
                        if (selectedMaKM != null && !selectedMaKM.equals("-- Chọn mã khuyến mãi --")) {
                            String maKMKey = selectedMaKM.split(" - ")[0];
                            Object dataObj = khuyenMaiMap.get(maKMKey);
                            if (dataObj instanceof String[]) {
                                String[] data = (String[]) dataObj;
                                txtMaKMComboDialog.setText(data[0]);
                                txtTenKMComboDialog.setText(data[1]);
                                txtGiaTriKMComboDialog.setText(data[2]);
                                txtNgayBDComboDialog.setText(data[3]);
                                txtNgayKTComboDialog.setText(data[4]);
                                selectedProductsTableModelDialog.setRowCount(0);
                                Object productsObj = khuyenMaiMap.get(maKMKey + "_products");
                                if (productsObj instanceof String) {
                                    String[] products = ((String) productsObj).split(";");
                                    for (String product : products) {
                                        String[] parts = product.split("\\|");
                                        selectedProductsTableModelDialog.addRow(new Object[]{parts[0], parts[1]});
                                    }
                                }
                            }
                        }
                    });
    
                } else { // Nút "Hoàn tất"
                    String maKM = txtMaKMComboDialog.getText();
                    if (maKM.isEmpty()) {
                        JOptionPane.showMessageDialog(comboDialog, "Vui lòng nhập mã KM!");
                        return;
                    }
    
                    // Lưu thông tin đã chỉnh sửa
                    String[] newComboData = {
                        maKM, txtTenKMComboDialog.getText(), txtGiaTriKMComboDialog.getText(),
                        txtNgayBDComboDialog.getText(), txtNgayKTComboDialog.getText(), "Combo"
                    };
                    int selectedRow = tableKhuyenMai.getSelectedRow();
                    if (selectedRow >= 0) {
                        String oldMaKM = (String) tableModel.getValueAt(selectedRow, 0);
                        khuyenMaiMap.remove(oldMaKM);
                        khuyenMaiMap.remove(oldMaKM + "_products");
                        tableModel.removeRow(selectedRow);
                    }
                    khuyenMaiMap.put(maKM, newComboData);
                    List<Map<String, String>> selectedProducts = new ArrayList<>();
                    for (int i = 0; i < selectedProductsTableModelDialog.getRowCount(); i++) {
                        Map<String, String> product = new HashMap<>();
                        product.put("idSanPham", (String) selectedProductsTableModelDialog.getValueAt(i, 0));
                        product.put("tenSanPham", (String) selectedProductsTableModelDialog.getValueAt(i, 1));
                        selectedProducts.add(product);
                    }
                    khuyenMaiMap.put(maKM + "_products", selectedProducts.stream()
                            .map(p -> p.get("idSanPham") + "|" + p.get("tenSanPham"))
                            .collect(Collectors.joining(";")));
                    tableModel.addRow(newComboData);
    
                    // Quay lại trạng thái ban đầu
                    btnSuaCombo.setText("Sửa combo");
                    comboTopPanel.removeAll();
                    comboTopPanel.add(lblTenKM, BorderLayout.WEST);
                    txtMaKMDialog.setText(maKM);
                    comboTopPanel.add(txtMaKMDialog, BorderLayout.CENTER);
                    txtTenKMComboDialog.setEditable(false);
                    txtMaKMComboDialog.setEditable(false);
                    txtGiaTriKMComboDialog.setEditable(false);
                    txtNgayBDComboDialog.setEditable(false);
                    txtNgayKTComboDialog.setEditable(false);
                    btnChonSanPham.setVisible(false);
                }
    
                comboTopPanel.revalidate();
                comboTopPanel.repaint();
                comboDialog.revalidate();
                comboDialog.repaint();
            });
    
            comboDialog.add(mainPanel);
        }
    
        // Điền thông tin từ dòng được chọn trong bảng
        int selectedRow = tableKhuyenMai.getSelectedRow();
        if (selectedRow >= 0) {
            String maKM = (String) tableModel.getValueAt(selectedRow, 0);
            Object dataObj = khuyenMaiMap.get(maKM);
            if (dataObj instanceof String[]) {
                String[] data = (String[]) dataObj;
                txtMaKMDialog.setText(data[0]);
                txtMaKMComboDialog.setText(data[0]);
                txtTenKMComboDialog.setText(data[1]);
                txtGiaTriKMComboDialog.setText(data[2]);
                txtNgayBDComboDialog.setText(data[3]);
                txtNgayKTComboDialog.setText(data[4]);
                selectedProductsTableModelDialog.setRowCount(0);
                Object productsObj = khuyenMaiMap.get(maKM + "_products");
                if (productsObj instanceof String) {
                    String[] products = ((String) productsObj).split(";");
                    for (String product : products) {
                        String[] parts = product.split("\\|");
                        selectedProductsTableModelDialog.addRow(new Object[]{parts[0], parts[1]});
                    }
                }
            }
        }
    
        comboDialog.setVisible(true);
    }
    

    private void showCreateComboDialog() {
        createCombo = new JDialog(this, "Tạo Combo", true);
        createCombo.setSize(593, 666);
        createCombo.setLocationRelativeTo(this);
        createCombo.setLayout(new BorderLayout());
    
        // Panel chính bao quanh với padding 20px
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // 1. Thanh JComboBox (dropdown) chứa danh sách mã khuyến mãi
        JPanel comboTopPanel = new JPanel(new BorderLayout(10, 0)); // Sử dụng BorderLayout với khoảng cách 10px
        JLabel lblTenKM = new JLabel("Tên KM:");
        lblTenKM.setPreferredSize(new Dimension(50, 41));
    
        // Tạo JComboBox chỉ hiển thị mã "Đơn lẻ"
        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
        comboModel.addElement("-- Chọn mã khuyến mãi --"); // Mặc định không chọn mã
    
        // Kiểm tra và thêm dữ liệu vào JComboBox
        boolean hasDonLe = false;
        System.out.println("khuyenMaiMap: " + khuyenMaiMap); // Debug dữ liệu
        for (Map.Entry<String, Object> entry : khuyenMaiMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String[]) {
                String[] data = (String[]) value;
                System.out.println("Data for " + entry.getKey() + ": " + Arrays.toString(data)); // Debug dữ liệu
                if ("Đơn lẻ".equals(data[5])) { // Chỉ thêm mã "Đơn lẻ"
                    comboModel.addElement(data[0] + " - " + data[1]); // Hiển thị mã và tên khuyến mãi
                    hasDonLe = true;
                }
            }
        }
    
        // Nếu không có mã "Đơn lẻ" nào, hiển thị thông báo
        if (!hasDonLe) {
            JOptionPane.showMessageDialog(this, "Không có mã khuyến mãi 'Đơn lẻ' nào để tạo combo. Vui lòng thêm mã khuyến mãi 'Đơn lẻ' trước!");
            createCombo.dispose();
            return;
        }
    
        JComboBox<String> maKMComboBox = new JComboBox<>(comboModel);
        maKMComboBox.setPreferredSize(new Dimension(510, 41));
        maKMComboBox.setBackground(Color.LIGHT_GRAY);
        maKMComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        maKMComboBox.setEditable(false); // Không cho phép nhập tay
    
        // Debug số lượng phần tử trong comboModel
        System.out.println("Số lượng phần tử trong comboModel: " + comboModel.getSize());
    
        comboTopPanel.add(lblTenKM, BorderLayout.WEST);
        comboTopPanel.add(maKMComboBox, BorderLayout.CENTER);
        mainPanel.add(comboTopPanel, BorderLayout.NORTH);
    
        // Làm mới comboTopPanel
        comboTopPanel.revalidate();
        comboTopPanel.repaint();
    
        // 2. Phần bên dưới chia đôi
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
    
        // 2.1. Bên trái: Các ô nhập liệu và bảng hiển thị sản phẩm đã chọn
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
        // Panel chứa các ô nhập liệu và nút "Chọn sản phẩm"
        JPanel leftFieldsPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        JTextField txtTenKMCombo = new JTextField();
        txtTenKMCombo.setPreferredSize(new Dimension(206, 41));
        txtTenKMCombo.setBackground(Color.LIGHT_GRAY);
        txtTenKMCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtTenKMCombo.setBorder(BorderFactory.createTitledBorder("Tên KM"));
    
        JTextField txtMaKMCombo = new JTextField();
        txtMaKMCombo.setPreferredSize(new Dimension(206, 41));
        txtMaKMCombo.setBackground(Color.LIGHT_GRAY);
        txtMaKMCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtMaKMCombo.setBorder(BorderFactory.createTitledBorder("Mã KM"));
    
        JTextField txtGiaTriKMCombo = new JTextField();
        txtGiaTriKMCombo.setPreferredSize(new Dimension(206, 41));
        txtGiaTriKMCombo.setBackground(Color.LIGHT_GRAY);
        txtGiaTriKMCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtGiaTriKMCombo.setBorder(BorderFactory.createTitledBorder("Giá trị khuyến mãi"));
    
        JButton btnChonSanPham = new JButton("Chọn sản phẩm");
        btnChonSanPham.setPreferredSize(new Dimension(206, 41));
    
        leftFieldsPanel.add(txtTenKMCombo);
        leftFieldsPanel.add(txtMaKMCombo);
        leftFieldsPanel.add(txtGiaTriKMCombo);
        leftFieldsPanel.add(btnChonSanPham);
    
        // Bảng hiển thị sản phẩm đã chọn
        String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm"};
        DefaultTableModel selectedProductsTableModel = new DefaultTableModel(columnNames, 0);
        JTable selectedProductsTable = new JTable(selectedProductsTableModel);
        JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsTable);
        selectedProductsScrollPane.setViewportView(selectedProductsTable); // Đảm bảo bảng được thêm vào JScrollPane
    
        // Sự kiện khi nhấn nút "Chọn sản phẩm"
        btnChonSanPham.addActionListener(e -> {
            JDialog selectProductDialog = new JDialog(createCombo, "Chọn sản phẩm", true);
            selectProductDialog.setSize(500, 500);
            selectProductDialog.setLocationRelativeTo(createCombo);
            selectProductDialog.setLayout(new BorderLayout());
    
            String[] productColumns = {"Mã sản phẩm", "Tên sản phẩm"};
            DefaultTableModel productTableModel = new DefaultTableModel(productColumns, 0);
            for (Map<String, String> product : danhSachSanPham) {
                productTableModel.addRow(new Object[]{product.get("idSanPham"), product.get("tenSanPham")});
            }
            JTable productTable = new JTable(productTableModel);
            productTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane productScrollPane = new JScrollPane(productTable);
    
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnHoanTatChonSP = new JButton("Hoàn tất");
            btnHoanTatChonSP.setPreferredSize(new Dimension(100, 30));
            buttonPanel.add(btnHoanTatChonSP);
    
            selectProductDialog.add(buttonPanel, BorderLayout.NORTH);
            selectProductDialog.add(productScrollPane, BorderLayout.CENTER);
    
            btnHoanTatChonSP.addActionListener(e1 -> {
                selectedProductsTableModel.setRowCount(0);
                int[] selectedRows = productTable.getSelectedRows();
                for (int row : selectedRows) {
                    String idSanPham = (String) productTable.getValueAt(row, 0);
                    String tenSanPham = (String) productTable.getValueAt(row, 1);
                    selectedProductsTableModel.addRow(new Object[]{idSanPham, tenSanPham});
                }
                selectProductDialog.dispose();
            });
    
            selectProductDialog.setVisible(true);
        });
    
        leftPanel.add(leftFieldsPanel, BorderLayout.NORTH);
        leftPanel.add(selectedProductsScrollPane, BorderLayout.CENTER);
        bottomPanel.add(leftPanel);
    
        // 2.2. Bên phải: 2 ô JTextField (Ngày BĐ, Ngày KT) và nút Hoàn tất
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
        JPanel rightFieldsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        JTextField txtNgayBDCombo = new JTextField();
        txtNgayBDCombo.setPreferredSize(new Dimension(206, 41));
        txtNgayBDCombo.setBackground(Color.LIGHT_GRAY);
        txtNgayBDCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNgayBDCombo.setBorder(BorderFactory.createTitledBorder("Ngày BĐ"));
    
        JTextField txtNgayKTCombo = new JTextField();
        txtNgayKTCombo.setPreferredSize(new Dimension(206, 41));
        txtNgayKTCombo.setBackground(Color.LIGHT_GRAY);
        txtNgayKTCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNgayKTCombo.setBorder(BorderFactory.createTitledBorder("Ngày KT"));
    
        rightFieldsPanel.add(txtNgayBDCombo);
        rightFieldsPanel.add(txtNgayKTCombo);
    
        JButton btnHoanTatCombo = new JButton("Hoàn tất");
        btnHoanTatCombo.setPreferredSize(new Dimension(100, 40));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnHoanTatCombo);
    
        rightPanel.add(rightFieldsPanel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        bottomPanel.add(rightPanel);
    
        // Thêm bottomPanel vào mainPanel
        mainPanel.add(bottomPanel, BorderLayout.CENTER); // Sửa lỗi: Thay customPanel bằng bottomPanel
        createCombo.add(mainPanel);
    
        // Làm mới giao diện
        mainPanel.revalidate();
        mainPanel.repaint();
        createCombo.revalidate();
        createCombo.repaint();
    
        // Tự động điền thông tin khi chọn mã khuyến mãi
        maKMComboBox.addActionListener(e -> {
            String selectedMaKM = (String) maKMComboBox.getSelectedItem();
            if (selectedMaKM != null && !selectedMaKM.equals("-- Chọn mã khuyến mãi --")) {
                String maKM = selectedMaKM.split(" - ")[0]; // Lấy mã KM từ chuỗi
                Object dataObj = khuyenMaiMap.get(maKM);
                if (dataObj instanceof String[]) {
                    String[] data = (String[]) dataObj;
                    txtMaKMCombo.setText(data[0]); // Mã KM
                    txtTenKMCombo.setText(data[1]); // Tên KM
                    txtGiaTriKMCombo.setText(data[2]); // Giá trị khuyến mãi
                    txtNgayBDCombo.setText(data[3]); // Ngày BĐ
                    txtNgayKTCombo.setText(data[4]); // Ngày KT
                } else {
                    JOptionPane.showMessageDialog(createCombo, "Dữ liệu mã khuyến mãi không hợp lệ!");
                }
            } else {
                // Nếu không chọn mã, xóa các ô để người dùng tự nhập
                txtMaKMCombo.setText("");
                txtTenKMCombo.setText("");
                txtGiaTriKMCombo.setText("");
                txtNgayBDCombo.setText("");
                txtNgayKTCombo.setText("");
            }
        });
    
        // Xử lý sự kiện cho nút Hoàn tất
        btnHoanTatCombo.addActionListener(e -> {
            String maKM = txtMaKMCombo.getText();
            String tenKM = txtTenKMCombo.getText();
            String giaTriKM = txtGiaTriKMCombo.getText();
            String ngayBD = txtNgayBDCombo.getText();
            String ngayKT = txtNgayKTCombo.getText();
    
            if (maKM.isEmpty() || tenKM.isEmpty() || giaTriKM.isEmpty() || ngayBD.isEmpty() || ngayKT.isEmpty()) {
                JOptionPane.showMessageDialog(createCombo, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
    
            List<Map<String, String>> selectedProducts = new ArrayList<>();
            for (int i = 0; i < selectedProductsTableModel.getRowCount(); i++) {
                Map<String, String> product = new HashMap<>();
                product.put("idSanPham", (String) selectedProductsTableModel.getValueAt(i, 0));
                product.put("tenSanPham", (String) selectedProductsTableModel.getValueAt(i, 1));
                selectedProducts.add(product);
            }
    
            if (selectedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(createCombo, "Vui lòng chọn ít nhất một sản phẩm cho combo!");
                return;
            }
    
            String[] newComboData = {maKM, tenKM, giaTriKM, ngayBD, ngayKT, "Combo"};
            khuyenMaiMap.put(maKM, newComboData);
    
            khuyenMaiMap.put(maKM + "_products", selectedProducts.stream()
                    .map(product -> product.get("idSanPham") + "|" + product.get("tenSanPham"))
                    .collect(Collectors.joining(";")));
    
            tableModel.addRow(newComboData);
            createCombo.dispose();
        });
    
        createCombo.setVisible(true);
    }

    // Hàm main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuanLyKhuyenMaiGiaoDienMoi frame = new QuanLyKhuyenMaiGiaoDienMoi();
            frame.setVisible(true);
        });
    }
}