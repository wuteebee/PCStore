package GUI.Panel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Date;
import DAO.*;
import DTO.*;
import GUI.Main;
import BUS.*;

public class NhapHoaDonPanel extends JPanel {
    private JTable tableSp, tableChiTiet, tablechsp;
    private DefaultTableModel modelSp, modelChiTiet, modelchsp;
    private JTextField tfMaSP, tfTenSP, tfGiaNhap, tfMaPhieu, tfNhanVien, tfMaImei, soluongsp, tfImeiFrom, tfImeiTo;
    private JComboBox<String> cbCauHinh, cbPhuongThuc, cbNhaCungCap;
    private JLabel lbTongTien, soluong, lbFrom, lbTo;
    private ProductDAO productDAO;
    private JButton Suasp, Xoasp, btnThem, submitbutton;
    private JRadioButton rbTuNhap, rbTheoKhoang;
    private Main mainFrame;
    private int maphanloai;
    private HashMap<String, List<ProductDetail>> chiTietHDN = new HashMap<>();
    private double tt;
    private boolean isEditing;
    private List<Product> products;

    private final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    private final Color SECONDARY_COLOR = new Color(255, 87, 34); // Orange
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public NhapHoaDonPanel(Main mainFrame, boolean isEditing) {
        this.mainFrame = mainFrame;
        this.isEditing = isEditing;
        setBackground(BACKGROUND_COLOR);
        setLayout(new GridBagLayout());
        productDAO = new ProductDAO();
        this.products = productDAO.getAllProducts();

        initComponents();
        actionTable();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Left Panel
        JPanel leftPanel = createLeftPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 0.6;
        add(leftPanel, gbc);

        // Center Panel
        JPanel centerPanel = createCenterPanel();
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        add(centerPanel, gbc);

        // Right Panel
        JPanel rightPanel = createRightPanel();
        gbc.gridx = 2;
        gbc.weightx = 0.3;
        add(rightPanel, gbc);

        // Bottom Panel
        JPanel bottomPanel = createBottomPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        add(bottomPanel, gbc);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search Field
        JTextField txtTimKiem = new JTextField();
        txtTimKiem.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(txtTimKiem, gbc);

        // Product Table
        String[] columnsSp = {"Mã SP", "Tên SP"};
        modelSp = new DefaultTableModel(columnsSp, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSp = new JTable(modelSp);
        styleTable(tableSp);
        JScrollPane scroll = new JScrollPane(tableSp);
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scroll, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnThem = createStyledButton("Thêm", PRIMARY_COLOR);
        JButton btnNhapExcel = createStyledButton("Nhập Excel", SECONDARY_COLOR);
        buttonPanel.add(btnThem);
        buttonPanel.add(btnNhapExcel);
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonPanel, gbc);

        // Populate table
        for (Product product : products) {
            modelSp.addRow(new Object[]{product.getMaSp(), product.getTenSp()});
        }

        // Search Listener
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchProducts(txtTimKiem.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchProducts(txtTimKiem.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchProducts(txtTimKiem.getText());
            }
        });

        // Table Selection Listener
        tableSp.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableSp.getSelectedRow() != -1) {
                int selectedRow = tableSp.getSelectedRow();
                String maSp = (String) modelSp.getValueAt(selectedRow, 0);
                updateCenterPanel(maSp, null);
                updateCauhinh(maSp, cbCauHinh.getSelectedItem().toString());
            }
        });

        btnThem.addActionListener(e -> updatePhieuNhap());
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields
        String[] labels = {"Mã sản phẩm:", "Tên sản phẩm:", "Cấu hình:", "Giá nhập:", "Phương thức:"};
        JTextField[] fields = {tfMaSP = new JTextField(), tfTenSP = new JTextField(), null, tfGiaNhap = new JTextField(), null};
        cbCauHinh = new JComboBox<>();
        cbPhuongThuc = new JComboBox<>(new String[]{"Nhập từng máy", "Nhập theo lô"});

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            if (fields[i] != null) {
                fields[i].setFont(LABEL_FONT);
                panel.add(fields[i], gbc);
            } else if (i == 2) {
                cbCauHinh.setFont(LABEL_FONT);
                panel.add(cbCauHinh, gbc);
            } else {
                cbPhuongThuc.setFont(LABEL_FONT);
                panel.add(cbPhuongThuc, gbc);
            }
        }

        // IMEI Section
        JLabel lbImei = new JLabel("Mã IMEI:");
        lbImei.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lbImei, gbc);

        rbTuNhap = new JRadioButton("Tự nhập");
        rbTheoKhoang = new JRadioButton("Theo khoảng");
        rbTuNhap.setFont(LABEL_FONT);
        rbTheoKhoang.setFont(LABEL_FONT);
        rbTuNhap.setSelected(true);
        ButtonGroup imeiGroup = new ButtonGroup();
        imeiGroup.add(rbTuNhap);
        imeiGroup.add(rbTheoKhoang);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setBackground(BACKGROUND_COLOR);
        radioPanel.add(rbTuNhap);
        radioPanel.add(rbTheoKhoang);
        gbc.gridx = 1;
        panel.add(radioPanel, gbc);

        // IMEI Fields
        tfMaImei = new JTextField();
        tfMaImei.setFont(LABEL_FONT);
        lbFrom = new JLabel("Từ:");
        tfImeiFrom = new JTextField();
        lbTo = new JLabel("Đến:");
        tfImeiTo = new JTextField();
        soluong = new JLabel("Số lượng:");
        soluongsp = new JTextField();
        lbFrom.setFont(LABEL_FONT);
        tfImeiFrom.setFont(LABEL_FONT);
        lbTo.setFont(LABEL_FONT);
        tfImeiTo.setFont(LABEL_FONT);
        soluong.setFont(LABEL_FONT);
        soluongsp.setFont(LABEL_FONT);

        JPanel imeiPanel = new JPanel(new GridBagLayout());
        imeiPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints imeiGbc = new GridBagConstraints();
        imeiGbc.insets = new Insets(5, 5, 5, 5);
        imeiGbc.fill = GridBagConstraints.HORIZONTAL;

        imeiGbc.gridx = 0;
        imeiGbc.gridy = 0;
        imeiGbc.gridwidth = 4;
        imeiPanel.add(tfMaImei, imeiGbc);

        imeiGbc.gridwidth = 1;
        imeiGbc.gridx = 0;
        imeiGbc.gridy = 0;
        imeiPanel.add(lbFrom, imeiGbc);
        imeiGbc.gridx = 1;
        imeiPanel.add(tfImeiFrom, imeiGbc);
        imeiGbc.gridx = 2;
        imeiPanel.add(lbTo, imeiGbc);
        imeiGbc.gridx = 3;
        imeiPanel.add(tfImeiTo, imeiGbc);

        imeiGbc.gridx = 0;
        imeiGbc.gridy = 1;
        imeiPanel.add(soluong, imeiGbc);
        imeiGbc.gridx = 1;
        imeiPanel.add(soluongsp, imeiGbc);

        lbFrom.setVisible(false);
        tfImeiFrom.setVisible(false);
        lbTo.setVisible(false);
        tfImeiTo.setVisible(false);
        soluong.setVisible(false);
        soluongsp.setVisible(false);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(imeiPanel, gbc);

        // Radio Button Listeners
        rbTuNhap.addActionListener(e -> {
            tfMaImei.setVisible(true);
            lbFrom.setVisible(false);
            tfImeiFrom.setVisible(false);
            lbTo.setVisible(false);
            tfImeiTo.setVisible(false);
            soluong.setVisible(false);
            soluongsp.setVisible(false);
        });

        rbTheoKhoang.addActionListener(e -> {
            tfMaImei.setVisible(false);
            lbFrom.setVisible(true);
            tfImeiFrom.setVisible(true);
            lbTo.setVisible(true);
            tfImeiTo.setVisible(true);
            soluong.setVisible(true);
            soluongsp.setVisible(true);
        });

        // Document Listener for IMEI Fields
        DocumentListener listener = new DocumentListener() {
            private boolean isUpdating = false;

            void updateQuantity() {
                try {
                    int from = Integer.parseInt(tfImeiFrom.getText());
                    int to = Integer.parseInt(tfImeiTo.getText());
                    if (to >= from) {
                        soluongsp.setText(String.valueOf(to - from + 1));
                    }
                } catch (NumberFormatException ignored) {}
            }

            void updateTo() {
                try {
                    int from = Integer.parseInt(tfImeiFrom.getText());
                    int quantity = Integer.parseInt(soluongsp.getText());
                    if (quantity > 0) {
                        tfImeiTo.setText(String.valueOf(from + quantity - 1));
                    }
                } catch (NumberFormatException ignored) {}
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                handle(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handle(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handle(e);
            }

            void handle(DocumentEvent e) {
                if (isUpdating) return;
                try {
                    isUpdating = true;
                    Document src = e.getDocument();
                    if (src == tfImeiFrom.getDocument()) {
                        if (!soluongsp.getText().isEmpty()) {
                            updateTo();
                        } else if (!tfImeiTo.getText().isEmpty()) {
                            updateQuantity();
                        }
                    } else if (src == tfImeiTo.getDocument()) {
                        if (!tfImeiFrom.getText().isEmpty()) {
                            updateQuantity();
                        }
                    } else if (src == soluongsp.getDocument()) {
                        if (!tfImeiFrom.getText().isEmpty()) {
                            updateTo();
                        }
                    }
                } finally {
                    isUpdating = false;
                }
            }
        };

        tfImeiFrom.getDocument().addDocumentListener(listener);
        tfImeiTo.getDocument().addDocumentListener(listener);
        soluongsp.getDocument().addDocumentListener(listener);

        // Buttons
        Suasp = createStyledButton("Sửa", SECONDARY_COLOR);
        Xoasp = createStyledButton("Xoá", Color.RED);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(Suasp);
        buttonPanel.add(Xoasp);
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(buttonPanel, gbc);

        Suasp.addActionListener(e -> xulySua());
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Product Detail Table
        JPanel pdDetail = new JPanel(new BorderLayout());
        pdDetail.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));
        String[] columns = {"Thông tin", "Chi tiết"};
        modelchsp = new DefaultTableModel(columns, 0);
        tablechsp = new JTable(modelchsp);
        styleTable(tablechsp);
        TableColumnModel columnModel = tablechsp.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(200);
        JScrollPane scrollPane = new JScrollPane(tablechsp);
        pdDetail.add(scrollPane, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6;
        panel.add(pdDetail, gbc);

        // Input Info Panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhập"));
        GridBagConstraints infoGbc = new GridBagConstraints();
        infoGbc.insets = new Insets(5, 5, 5, 5);
        infoGbc.fill = GridBagConstraints.HORIZONTAL;

        tfMaPhieu = new JTextField("PN-1");
        tfMaPhieu.setFont(LABEL_FONT);
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.getEmployeeById(mainFrame.getUser().getIdNhanVien());
        tfNhanVien = new JTextField(employee.getName());
        tfNhanVien.setFont(LABEL_FONT);
        tfNhanVien.setEditable(false);
        SupplierBUS supplierBUS = new SupplierBUS();
        List<Supplier> suppliers = supplierBUS.getAllSuppliers();
        cbNhaCungCap = new JComboBox<>();
        for (Supplier supplier : suppliers) {
            cbNhaCungCap.addItem(supplier.getName());
        }
        cbNhaCungCap.setFont(LABEL_FONT);

        String[] labels = {"Mã phiếu:", "Nhân viên:", "Nhà cung cấp:"};
        JComponent[] components = {tfMaPhieu, tfNhanVien, cbNhaCungCap};

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);
            infoGbc.gridx = 0;
            infoGbc.gridy = i;
            infoGbc.weightx = 0;
            infoPanel.add(label, infoGbc);

            infoGbc.gridx = 1;
            infoGbc.weightx = 1.0;
            infoPanel.add(components[i], infoGbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.4;
        panel.add(infoPanel, gbc);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Table
        String[] columns = {"STT", "Mã sản phẩm", "Tên sản phẩm", "Mã phân loại", "Đơn giá", "Số lượng"};
        modelChiTiet = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet = new JTable(modelChiTiet);
        styleTable(tableChiTiet);
        TableColumnModel columnModel = tableChiTiet.getColumnModel();
        int[] columnWidths = {40, 80, 250, 120, 100, 80};
        for (int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        JScrollPane scroll = new JScrollPane(tableChiTiet);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scroll, gbc);

        // Total Label
        lbTongTien = new JLabel("TỔNG TIỀN: ");
        lbTongTien.setForeground(Color.RED);
        lbTongTien.setFont(HEADER_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(lbTongTien, gbc);

        // Submit Button
        submitbutton = createStyledButton("Nhập hàng", PRIMARY_COLOR);
        submitbutton.setFont(HEADER_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitbutton, gbc);

        submitbutton.addActionListener(e -> xulyNhapHang());
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(LABEL_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(200, 200, 200));
        header.setFont(HEADER_FONT);
        table.setRowHeight(25);
        table.setFont(LABEL_FONT);
        table.setGridColor(new Color(220, 220, 220));
    }

    private void searchProducts(String query) {
        modelSp.setRowCount(0);
        for (Product product : products) {
            if (product.getTenSp().toLowerCase().contains(query.toLowerCase())) {
                modelSp.addRow(new Object[]{product.getMaSp(), product.getTenSp()});
            }
        }
    }

    private void updateCenterPanel(String maSP, Integer phienBan) {
        Product product = productDAO.getProductByIdFull(maSP);
        tfMaSP.setText(product.getMaSp());
        tfTenSP.setText(product.getTenSp());
        cbCauHinh.removeAllItems();
        List<Variant> danhSach = product.getDanhSachPhienBan();
        int selectedIndex = -1;
        for (int i = 0, j = 0; i < danhSach.size(); i++) {
            Variant variant = danhSach.get(i);
            if (variant.isTrangThai()) {
                cbCauHinh.addItem("Cấu hình " + (variant.getPhienBan() + 1));
                if (phienBan != null && variant.getPhienBan() == phienBan) {
                    selectedIndex = j;
                }
                j++;
            }
        }
        if (selectedIndex != -1) {
            cbCauHinh.setSelectedIndex(selectedIndex);
        }
    }

    private void updateCauhinh(String maSP, String phienBan) {
        String[] parts = phienBan.trim().split(" ");
        int soPhienBan = Integer.parseInt(parts[2]) - 1;
        modelchsp.setRowCount(0);
        Product product = productDAO.getProductByIdFull(maSP);
        for (Variant variant : product.getDanhSachPhienBan()) {
            if (variant.getPhienBan() == soPhienBan) {
                variant.getChitiet().forEach(chitiet -> {
                    if (chitiet instanceof CauHinhPC) {
                        CauHinhPC cauHinhPC = (CauHinhPC) chitiet;
                        modelchsp.addRow(new Object[]{cauHinhPC.getIdThongTin(), cauHinhPC.getLinhKien().getTenSp()});
                    } else if (chitiet instanceof CauHinhLaptop) {
                        CauHinhLaptop cauHinhLaptop = (CauHinhLaptop) chitiet;
                        modelchsp.addRow(new Object[]{cauHinhLaptop.getIdThongTin(), cauHinhLaptop.getThongTin()});
                    }
                });
                break;
            }
        }
    }

    private List<String> generateImeiList(String from, String to) {
        List<String> imeiList = new ArrayList<>();
        try {
            long start = Long.parseLong(from);
            long end = Long.parseLong(to);
            for (long i = start; i <= end; i++) {
                imeiList.add(String.valueOf(i));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "IMEI không hợp lệ! Hãy nhập số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return imeiList;
    }

    public boolean isImeiExistInChiTietHDN(String imei) {
        for (List<ProductDetail> list : chiTietHDN.values()) {
            for (ProductDetail pd : list) {
                if (pd.getSerialNumber().equals(imei)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updatePhieuNhap() {
        String maSP = tfMaSP.getText();
        String tenSP = tfTenSP.getText();
        String pb = cbCauHinh.getSelectedItem().toString();
        int phienBanSo = Integer.parseInt(pb.replaceAll("\\D+", ""));
        String giaNhap = tfGiaNhap.getText();
        String imei = tfMaImei.getText();
        String soLuong = soluongsp.getText();
        String phuongThuc = cbPhuongThuc.getSelectedItem().toString();
        String imeiFrom = tfImeiFrom.getText();
        String imeiTo = tfImeiTo.getText();
        tfMaSP.setText("");
        tfTenSP.setText("");
        cbCauHinh.setSelectedIndex(0);
        tfGiaNhap.setText("");
        tfMaImei.setText("");
        soluongsp.setText("");
        tfImeiFrom.setText("");
        tfImeiTo.setText("");
        cbPhuongThuc.setSelectedIndex(0);
        PhieuNhapDAO phieuNhapDAO = new PhieuNhapDAO();
        maphanloai = productDAO.getIDPhanLoai(maSP, phienBanSo - 1);
        if (rbTuNhap.isSelected()) {
            List<ProductDetail> list = new ArrayList<>();
            if (phieuNhapDAO.isImeiExistInDatabase(imei) || isImeiExistInChiTietHDN(imei)) {
                JOptionPane.showMessageDialog(null, "IMEI " + imei + " đã tồn tại! Không thể thêm trùng.");
                return;
            }
            ProductDetail productDetail = new ProductDetail(imei, maphanloai, Double.parseDouble(giaNhap), true);
            list.add(productDetail);
            chiTietHDN.put((modelChiTiet.getRowCount() + 1) + "", list);
            soLuong = "1";
        } else if (rbTheoKhoang.isSelected()) {
            imei = tfImeiFrom.getText() + " - " + tfImeiTo.getText();
            List<ProductDetail> list = new ArrayList<>();
            for (String imeiNumber : generateImeiList(imeiFrom, imeiTo)) {
                if (phieuNhapDAO.isImeiExistInDatabase(imeiNumber) || isImeiExistInChiTietHDN(imeiNumber)) {
                    JOptionPane.showMessageDialog(null, "IMEI " + imeiNumber + " đã tồn tại! Không thể thêm trùng.");
                    return;
                }
                ProductDetail productDetail = new ProductDetail(imeiNumber, maphanloai, Double.parseDouble(giaNhap), true);
                list.add(productDetail);
            }
            int stt = modelChiTiet.getRowCount() + 1;
            chiTietHDN.put(String.valueOf(stt), list);
        }
        modelChiTiet.addRow(new Object[]{
                modelChiTiet.getRowCount() + 1,
                maSP,
                tenSP,
                phienBanSo,
                String.format("%,d", Integer.parseInt(giaNhap)).replace(",", "."),
                soLuong
        });
        updateTongTien();
        tfMaSP.requestFocus();
    }

    private void xulyNhapHang() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.getEmployeeById(mainFrame.getUser().getIdNhanVien());
        Date sqlDate = new Date(System.currentTimeMillis());
        SupplierBUS supplierBUS = new SupplierBUS();
        Supplier supplier = supplierBUS.getAllSuppliers().get(cbNhaCungCap.getSelectedIndex());
        HoaDonNhap hdn = new HoaDonNhap(employee, supplier, sqlDate, tt);
        PhieuNhapBUS phieuNhapBUS = new PhieuNhapBUS();
        String idPhieuNhap = phieuNhapBUS.insertHoaDonNhap(hdn);
        hdn.setIdHoaDonNhap(idPhieuNhap);
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            String stt = modelChiTiet.getValueAt(i, 0).toString();
            String soLuong = modelChiTiet.getValueAt(i, 5).toString();
            phieuNhapBUS.updateSLTK(chiTietHDN.get(stt).get(0).getIdPhanLoai() + "", Integer.parseInt(soLuong));
            chiTietHDN.forEach((key, value) -> {
                if (key.equals(stt)) {
                    value.forEach(productDetail -> {
                        productDetail.setMaPhieuNhap(idPhieuNhap);
                        if (phieuNhapBUS.insertChitietSP(productDetail)) {
                            phieuNhapBUS.insertChitietPhieuNhap(productDetail);
                        }
                    });
                }
            });
        }
        JOptionPane.showMessageDialog(null, "Nhập hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.setMainPanel(new PhieuNhapPanel(mainFrame));
    }

    private void actionTable() {
        tableChiTiet.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableChiTiet.getSelectedRow();
                if (selectedRow == -1) return;
                int idphanloai = chiTietHDN.get((selectedRow + 1) + "").get(0).getIdPhanLoai();
                ProductBUS productBUS = new ProductBUS();
                String masp = productBUS.getmaSPbyIdPL(idphanloai);
                int phienBan = productBUS.getphienbanbyIdPL(idphanloai);
                updateCenterPanel(masp, phienBan);
                updateCauhinh(masp, cbCauHinh.getSelectedItem().toString());
                double giaNhap = chiTietHDN.get((selectedRow + 1) + "").get(0).getGiaNhap();
                tfGiaNhap.setText(String.valueOf((long) giaNhap));
                if (chiTietHDN.get((selectedRow + 1) + "").size() == 1) {
                    rbTuNhap.setSelected(true);
                    tfMaImei.setText(chiTietHDN.get((selectedRow + 1) + "").get(0).getSerialNumber());
                    tfMaImei.setVisible(true);
                    lbFrom.setVisible(false);
                    tfImeiFrom.setVisible(false);
                    lbTo.setVisible(false);
                    tfImeiTo.setVisible(false);
                    soluong.setVisible(false);
                    soluongsp.setVisible(false);
                } else {
                    rbTheoKhoang.setSelected(true);
                    soluongsp.setText(chiTietHDN.get((selectedRow + 1) + "").size() + "");
                    tfImeiFrom.setText(chiTietHDN.get((selectedRow + 1) + "").get(0).getSerialNumber());
                    tfMaImei.setVisible(false);
                    lbFrom.setVisible(true);
                    tfImeiFrom.setVisible(true);
                    lbTo.setVisible(true);
                    tfImeiTo.setVisible(true);
                    soluong.setVisible(true);
                    soluongsp.setVisible(true);
                }
                for (int i = 0; i < modelSp.getRowCount(); i++) {
                    Object value = modelSp.getValueAt(i, 0);
                    if (value != null && value.toString().equalsIgnoreCase(masp)) {
                        tableSp.setRowSelectionInterval(i, i);
                        tableSp.scrollRectToVisible(tableSp.getCellRect(i, 0, true));
                        break;
                    }
                }
            }
        });
    }

    private void xulySua() {
        if (!isEditing) {
            int selectedRow = tableChiTiet.getSelectedRow();
            if (selectedRow == -1) return;
            String pb = cbCauHinh.getSelectedItem().toString();
            int phienBanSo = Integer.parseInt(pb.replaceAll("\\D+", ""));
            String maSP = tfMaSP.getText();
            String giaNhap = tfGiaNhap.getText();
            String imeiFrom = tfImeiFrom.getText();
            String imeiTo = tfImeiTo.getText();
            String imei = tfMaImei.getText();
            String soLuong = soluongsp.getText();
            String tenSP = tfTenSP.getText();
            PhieuNhapDAO phieuNhapDAO = new PhieuNhapDAO();
            maphanloai = productDAO.getIDPhanLoai(maSP, phienBanSo - 1);
            List<ProductDetail> tmp = chiTietHDN.get(selectedRow + "");
            chiTietHDN.remove(selectedRow + "");
            if (rbTuNhap.isSelected()) {
                List<ProductDetail> list = new ArrayList<>();
                if (phieuNhapDAO.isImeiExistInDatabase(imei) || isImeiExistInChiTietHDN(imei)) {
                    JOptionPane.showMessageDialog(null, "IMEI " + imei + " đã tồn tại! Không thể thêm trùng.");
                    chiTietHDN.put(selectedRow + "", tmp);
                    return;
                }
                ProductDetail productDetail = new ProductDetail(imei, maphanloai, Double.parseDouble(giaNhap), true);
                list.add(productDetail);
                chiTietHDN.put(selectedRow + "", list);
                soLuong = "1";
            } else if (rbTheoKhoang.isSelected()) {
                List<ProductDetail> list = new ArrayList<>();
                for (String imeiNumber : generateImeiList(imeiFrom, imeiTo)) {
                    if (phieuNhapDAO.isImeiExistInDatabase(imeiNumber) || isImeiExistInChiTietHDN(imeiNumber)) {
                        JOptionPane.showMessageDialog(null, "IMEI " + imeiNumber + " đã tồn tại! Không thể thêm trùng.");
                        chiTietHDN.put(selectedRow + "", tmp);
                        return;
                    }
                    ProductDetail productDetail = new ProductDetail(imeiNumber, maphanloai, Double.parseDouble(giaNhap), true);
                    list.add(productDetail);
                }
                chiTietHDN.put(selectedRow + "", list);
            }
            modelChiTiet.setValueAt(maSP, selectedRow, 1);
            modelChiTiet.setValueAt(tenSP, selectedRow, 2);
            modelChiTiet.setValueAt(phienBanSo, selectedRow, 3);
            modelChiTiet.setValueAt(String.format("%,d", Integer.parseInt(giaNhap)).replace(",", "."), selectedRow, 4);
            modelChiTiet.setValueAt(soLuong, selectedRow, 5);
        }
    }

    private void updateTongTien() {
        long tongTien = 0;
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            String giaStr = modelChiTiet.getValueAt(i, 4).toString().replace(".", "");
            int gia = Integer.parseInt(giaStr);
            int soLuong = Integer.parseInt(modelChiTiet.getValueAt(i, 5).toString());
            tongTien += gia * soLuong;
        }
        tt = tongTien;
        String tongTienFormatted = String.format("%,d", tongTien).replace(",", ".");
        lbTongTien.setText("TỔNG TIỀN: " + tongTienFormatted + "đ");
    }
}