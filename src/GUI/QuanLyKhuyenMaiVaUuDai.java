package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class QuanLyKhuyenMaiVaUuDai extends JPanel {

    // Khai báo các thành phần giao diện và dữ liệu
    private JTable tableKhuyenMai;
    private DefaultTableModel tableModel;
    private RoundedButton  btnThemMaKM, btnXoaMaKM, btnSuaMaKM, btnTaoCombo;
    private JTextField txtMaKM, txtTenKM, txtPhanTramKM, txtNgayBD, txtNgayKT, txtLoai;
    private JLabel jlbTenKM;
    private JPanel buttonPanel;
    private JPanel inputPanel;
    private Icon icon1,icon2,icon3,icon4,icon5,icon6;
    private Map<String, Object> khuyenMaiMap;
    private JDialog comboDialog, createCombo;
    private boolean isAddingMode = false;
    private boolean isEditingMode = false;
    private List<Map<String, String>> danhSachSanPham = new ArrayList<>();
    private JTextField txtMaKMDialog;
    private JTextField txtTenKMComboDialog, txtMaKMComboDialog, txtGiaTriKMComboDialog;
    private JTextField txtNgayBDComboDialog, txtNgayKTComboDialog;
    private DefaultTableModel selectedProductsTableModelDialog;

    // Constructor: Khởi tạo giao diện
    public QuanLyKhuyenMaiVaUuDai() {
        khuyenMaiMap = new HashMap<>();
        khoiTaoSanPhamMau(); // Khởi tạo danh sách sản phẩm mẫu

        // Thiết lập kích thước và layout cho JPanel
        setPreferredSize(new Dimension(1080, 770));
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(404, 120));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        // Tạo các Icon cho các nút chức năng
        icon1 = new ImageIcon("src//resources//add.png");
        icon2= new ImageIcon("src//resources//edit.png");
        icon3 = new ImageIcon("src//resources//delete.png");
        icon4= new ImageIcon("src//resources//createcombo.png");




        btnThemMaKM = new RoundedButton("Thêm mã KM",20,icon1);
        btnThemMaKM.setBackground(new Color(0,255,255));
        btnThemMaKM.setForeground(Color.BLACK);
        btnThemMaKM.setFocusPainted(false);

        btnXoaMaKM = new RoundedButton("Xóa mã KM",20,icon2);
        btnXoaMaKM.setBackground(new Color(0,255,255));
        btnXoaMaKM.setForeground(Color.BLACK);

        btnSuaMaKM = new RoundedButton("Sửa mã KM",20,icon3);
        btnSuaMaKM.setBackground(new Color(0,255,255));
        btnSuaMaKM.setForeground(Color.BLACK);

        btnTaoCombo = new RoundedButton("Tạo combo",20,icon4);
        btnTaoCombo.setBackground(new Color(0,255,255));
        btnTaoCombo.setForeground(Color.BLACK);

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
        tableKhuyenMai.getTableHeader().setReorderingAllowed(false);

        // Áp dụng renderer tùy chỉnh cho cột "Loại"
        tableKhuyenMai.getColumnModel().getColumn(5).setCellRenderer(new TypeColumnRenderer());

        // Thêm sự kiện nhấp chuột vào bảng để mở dialog khi nhấp vào ô "Combo"
        tableKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tableKhuyenMai.columnAtPoint(e.getPoint());
                int row = tableKhuyenMai.rowAtPoint(e.getPoint());
                if (column == 5 && "Combo".equals(tableModel.getValueAt(row, 5))) {
                    showComboDialog();
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
        private JLabel eyeLabel;
        private ImageIcon eyeIcon;

        public TypeColumnRenderer() {
            eyeLabel = new JLabel();
            ImageIcon originalIcon = new ImageIcon("src//resources//eye_icon.png");
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
                        JOptionPane.showMessageDialog(QuanLyKhuyenMaiVaUuDai.this,
                            "Muốn thêm sản phẩm? Hãy ấn nút Thêm sản phẩm.");
                    } else {
                        JOptionPane.showMessageDialog(QuanLyKhuyenMaiVaUuDai.this,
                            "Muốn sửa sản phẩm? Hãy ấn Sửa sản phẩm.");
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

        //Tạo icon cho nút hoàn tất và hủy
        icon5= new ImageIcon("src//resources//complete.png");
        icon6 = new ImageIcon("src//resources//exit.png");

        

        RoundedButton btnHoanTat = new RoundedButton("Hoàn tất",20,icon5);
        btnHoanTat.setBackground(new Color(0,255,255));
        btnHoanTat.setForeground(Color.BLACK);
        btnHoanTat.setFocusPainted(false);
        btnHoanTat.setHorizontalAlignment(SwingConstants.CENTER);
        btnHoanTat.setIconTextGap(10);
        



        RoundedButton btnHuy = new RoundedButton("Hủy",20,icon6);
        btnHuy.setBackground(new Color(0,255,255));
        btnHuy.setForeground(Color.BLACK);
        btnHuy.setFocusPainted(false);
        btnHuy.setHorizontalAlignment(SwingConstants.CENTER);
        btnHuy.setIconTextGap(30);


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
            comboDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Danh sách Combo", true);
            comboDialog.setSize(593, 666);
            comboDialog.setLocationRelativeTo(this);
            comboDialog.setLayout(new BorderLayout());
    
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
            // 1. Thanh mã KM ban đầu là JTextField
            JPanel comboTopPanel = new JPanel(new BorderLayout(10, 0));
            JLabel lblTenKM = new JLabel("Tên KM:");
            lblTenKM.setPreferredSize(new Dimension(50, 41));
            txtMaKMDialog = new JTextField();
            txtMaKMDialog.setPreferredSize(new Dimension(510, 41));
            txtMaKMDialog.setEditable(false);
            comboTopPanel.add(lblTenKM, BorderLayout.WEST);
            comboTopPanel.add(txtMaKMDialog, BorderLayout.CENTER);
            mainPanel.add(comboTopPanel, BorderLayout.NORTH);
    
            // 2. Phần bên dưới chia đôi
            JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
    
            // 2.1. Bên trái: Các ô nhập liệu và bảng sản phẩm đã chọn
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
            JPanel leftFieldsPanel = new JPanel(new GridLayout(4, 1, 0, 20));
            txtTenKMComboDialog = new JTextField();
            txtTenKMComboDialog.setPreferredSize(new Dimension(206, 41));
            txtTenKMComboDialog.setBackground(Color.LIGHT_GRAY);
            txtTenKMComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtTenKMComboDialog.setBorder(BorderFactory.createTitledBorder("Tên KM"));
            txtTenKMComboDialog.setEditable(false);
    
            txtMaKMComboDialog = new JTextField();
            txtMaKMComboDialog.setPreferredSize(new Dimension(206, 41));
            txtMaKMComboDialog.setBackground(Color.LIGHT_GRAY);
            txtMaKMComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtMaKMComboDialog.setBorder(BorderFactory.createTitledBorder("Mã KM"));
            txtMaKMComboDialog.setEditable(false);
    
            txtGiaTriKMComboDialog = new JTextField();
            txtGiaTriKMComboDialog.setPreferredSize(new Dimension(206, 41));
            txtGiaTriKMComboDialog.setBackground(Color.LIGHT_GRAY);
            txtGiaTriKMComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtGiaTriKMComboDialog.setBorder(BorderFactory.createTitledBorder("Giá trị khuyến mãi"));
            txtGiaTriKMComboDialog.setEditable(false);
    
            JButton btnChonSanPham = new JButton("Chọn sản phẩm");
            btnChonSanPham.setPreferredSize(new Dimension(206, 41));
            btnChonSanPham.setVisible(false);
    
            leftFieldsPanel.add(txtTenKMComboDialog);
            leftFieldsPanel.add(txtMaKMComboDialog);
            leftFieldsPanel.add(txtGiaTriKMComboDialog);
            leftFieldsPanel.add(btnChonSanPham);
    
            String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm"};
            selectedProductsTableModelDialog = new DefaultTableModel(columnNames, 0);
            JTable selectedProductsTable = new JTable(selectedProductsTableModelDialog);
            selectedProductsTable.getTableHeader().setReorderingAllowed(false);
            JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsTable);
           
    
            leftPanel.add(leftFieldsPanel, BorderLayout.NORTH);
            leftPanel.add(selectedProductsScrollPane, BorderLayout.CENTER);
            bottomPanel.add(leftPanel);
    
            // 2.2. Bên phải: Ngày BĐ, Ngày KT và nút Sửa combo
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
            JPanel rightFieldsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
            txtNgayBDComboDialog = new JTextField();
            txtNgayBDComboDialog.setPreferredSize(new Dimension(206, 41));
            txtNgayBDComboDialog.setBackground(Color.LIGHT_GRAY);
            txtNgayBDComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtNgayBDComboDialog.setBorder(BorderFactory.createTitledBorder("Ngày BĐ"));
            txtNgayBDComboDialog.setEditable(false);
    
            txtNgayKTComboDialog = new JTextField();
            txtNgayKTComboDialog.setPreferredSize(new Dimension(206, 41));
            txtNgayKTComboDialog.setBackground(Color.LIGHT_GRAY);
            txtNgayKTComboDialog.setFont(new Font("Arial", Font.PLAIN, 13));
            txtNgayKTComboDialog.setBorder(BorderFactory.createTitledBorder("Ngày KT"));
            txtNgayKTComboDialog.setEditable(false);
    
            rightFieldsPanel.add(txtNgayBDComboDialog);
            rightFieldsPanel.add(txtNgayKTComboDialog);
    
            RoundedButton btnSuaCombo = new RoundedButton("Sửa combo",20,icon2);
            btnSuaCombo.setBackground(new Color(0,255,255));
            btnSuaCombo.setForeground(Color.BLACK);
            btnSuaCombo.setFocusPainted(false);
            // btnSuaCombo.setHorizontalAlignment(SwingConstants.CENTER);
            // btnSuaCombo.setIconTextGap(10);



            btnSuaCombo.setPreferredSize(new Dimension(187, 40));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(btnSuaCombo);
    
            rightPanel.add(rightFieldsPanel, BorderLayout.NORTH);
            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            bottomPanel.add(rightPanel);
    
            mainPanel.add(bottomPanel, BorderLayout.CENTER); // Sửa thành bottomPanel
    
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

                //Hoàn tất của show combo
                
                RoundedButton btnHoanTatChonSP = new RoundedButton("Hoàn tất",20,icon5);
                btnHoanTatChonSP.setBackground(new Color(0,255,255));
                btnHoanTatChonSP.setForeground(Color.BLACK);
                btnHoanTatChonSP.setFocusPainted(false);
                //btnHoanTatChonSP.setHorizontalAlignment(SwingConstants.CENTER);
                //btnHoanTatChonSP.setIconTextGap(10);






                btnHoanTatChonSP.setPreferredSize(new Dimension(180, 40));
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
    
                    comboTopPanel.remove(txtMaKMDialog);
                    DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
                    comboModel.addElement("-- Chọn mã khuyến mãi --");
                    for (Map.Entry<String, Object> entry : khuyenMaiMap.entrySet()) {
                        Object value = entry.getValue();
                        if (value instanceof String[] && "Đơn lẻ".equals(((String[]) value)[5])) { // Chỉ lấy mã "Đơn lẻ"
                            String[] data = (String[]) value;
                            comboModel.addElement(data[0] + " - " + data[1]);
                        }
                    }
                    JComboBox<String> maKMComboBox = new JComboBox<>(comboModel);
                    maKMComboBox.setPreferredSize(new Dimension(510, 41));
                    maKMComboBox.setBackground(Color.LIGHT_GRAY);
                    maKMComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
                    comboTopPanel.add(maKMComboBox, BorderLayout.CENTER);
    
                    txtTenKMComboDialog.setEditable(true);
                    txtMaKMComboDialog.setEditable(true);
                    txtGiaTriKMComboDialog.setEditable(true);
                    txtNgayBDComboDialog.setEditable(true);
                    txtNgayKTComboDialog.setEditable(true);
                    btnChonSanPham.setVisible(true);
    
                    maKMComboBox.addActionListener(e1 -> {
                        String selectedMaKM = (String) maKMComboBox.getSelectedItem();
                        if (selectedMaKM != null && !selectedMaKM.equals("-- Chọn mã khuyến mãi --")) {
                            String maKMKey = selectedMaKM.split(" - ")[0];
                            Object dataObj = khuyenMaiMap.get(maKMKey);
                            if (dataObj instanceof String[]) {
                                String[] data = (String[]) dataObj;
                                txtMaKMComboDialog.setText(maKMKey + "_C" + (khuyenMaiMap.size() + 1)); // Tạo mã mới dựa trên mã "Đơn lẻ"
                                txtTenKMComboDialog.setText(data[1] + " Combo");
                                txtGiaTriKMComboDialog.setText(data[2]);
                                txtNgayBDComboDialog.setText(data[3]);
                                txtNgayKTComboDialog.setText(data[4]);
                                selectedProductsTableModelDialog.setRowCount(0); // Không có sản phẩm từ mã "Đơn lẻ"
                            }
                        }
                    });
    
                } else {
                    String maKM = txtMaKMComboDialog.getText();
                    if (maKM.isEmpty()) {
                        JOptionPane.showMessageDialog(comboDialog, "Vui lòng nhập mã KM!");
                        return;
                    }
    
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
        createCombo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo Combo", true);
        createCombo.setSize(593, 666);
        createCombo.setLocationRelativeTo(this);
        createCombo.setLayout(new BorderLayout());
    
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JPanel comboTopPanel = new JPanel(new BorderLayout(10, 0));
        JLabel lblTenKM = new JLabel("Tên KM:");
        lblTenKM.setPreferredSize(new Dimension(50, 41));
    
        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
        comboModel.addElement("-- Tạo mới --"); // Tùy chọn tạo combo từ đầu
    
        // Thêm các mã "Đơn lẻ" vào JComboBox
        for (Map.Entry<String, Object> entry : khuyenMaiMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String[] && "Đơn lẻ".equals(((String[]) value)[5])) {
                String[] data = (String[]) value;
                comboModel.addElement(data[0] + " - " + data[1]);
            }
        }
    
        JComboBox<String> maKMComboBox = new JComboBox<>(comboModel);
        maKMComboBox.setPreferredSize(new Dimension(510, 41));
        maKMComboBox.setBackground(Color.LIGHT_GRAY);
        maKMComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        maKMComboBox.setEditable(false);
    
        comboTopPanel.add(lblTenKM, BorderLayout.WEST);
        comboTopPanel.add(maKMComboBox, BorderLayout.CENTER);
        mainPanel.add(comboTopPanel, BorderLayout.NORTH);
    
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
    
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
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
    
        String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm"};
        DefaultTableModel selectedProductsTableModel = new DefaultTableModel(columnNames, 0);
        JTable selectedProductsTable = new JTable(selectedProductsTableModel);
        JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsTable);
    
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
            productTable.getTableHeader().setReorderingAllowed(false);
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
    
        mainPanel.add(bottomPanel, BorderLayout.CENTER); // Sửa thành bottomPanel
    
        createCombo.add(mainPanel);
    
        maKMComboBox.addActionListener(e -> {
            String selectedMaKM = (String) maKMComboBox.getSelectedItem();
            if (selectedMaKM != null && !selectedMaKM.equals("-- Tạo mới --")) {
                String maKM = selectedMaKM.split(" - ")[0];
                Object dataObj = khuyenMaiMap.get(maKM);
                if (dataObj instanceof String[]) {
                    String[] data = (String[]) dataObj;
                    txtMaKMCombo.setText(data[0] + "_C" + (khuyenMaiMap.size() + 1)); // Tạo mã mới dựa trên mã gốc
                    txtTenKMCombo.setText(data[1] + " Combo");
                    txtGiaTriKMCombo.setText(data[2]);
                    txtNgayBDCombo.setText(data[3]);
                    txtNgayKTCombo.setText(data[4]);
                } else {
                    JOptionPane.showMessageDialog(createCombo, "Dữ liệu mã khuyến mãi không hợp lệ!");
                }
            } else {
                txtMaKMCombo.setText("");
                txtTenKMCombo.setText("");
                txtGiaTriKMCombo.setText("");
                txtNgayBDCombo.setText("");
                txtNgayKTCombo.setText("");
                selectedProductsTableModel.setRowCount(0);
            }
        });
    
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
            khuyenMaiMap.put(maKM, newComboData); // Thêm mã combo mới mà không ghi đè mã "Đơn lẻ"
    
            khuyenMaiMap.put(maKM + "_products", selectedProducts.stream()
                    .map(product -> product.get("idSanPham") + "|" + product.get("tenSanPham"))
                    .collect(Collectors.joining(";")));
    
            tableModel.addRow(newComboData);
            createCombo.dispose();
        });
    
        createCombo.setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Đặt màu nền và bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        
        super.paintComponent(g);
        g2.dispose();
    }

    

    // Hàm main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý khuyến mãi - Giao diện mới");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1080, 770);
            frame.setLocationRelativeTo(null);

            QuanLyKhuyenMaiVaUuDai panel = new QuanLyKhuyenMaiVaUuDai();
            frame.add(panel);

            frame.setVisible(true);
        });
    }
}