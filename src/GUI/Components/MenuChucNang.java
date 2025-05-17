package GUI.Components;

import GUI.ActionListener.*;

import BUS.PermissionBUS;
import GUI.ActionListener.*;
import GUI.Main;
import GUI.Panel.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MenuChucNang {
    private static Color color = Color.WHITE;
    private static JTextField searchField;
    private static JButton btnSearch = new Button().createStyledButton("Tìm", null);
    private static JButton btnReset = new Button().createStyledButton("Làm mới", null);
    private static PermissionBUS permissionBUS = new PermissionBUS();

    public JPanel createActionPanel(JPanel panel, Main MainFrame) {
        JPanel actionPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        actionPanel.setPreferredSize(new Dimension(475, 100));
        actionPanel.setBackground(Color.WHITE);
        Button buttonFactory = new Button();
        JButton btnFinance = buttonFactory.createStyledButton("Tài chính", "./resources/icon/add.png");
        JButton btnEmployee = buttonFactory.createStyledButton("Nhân viên", "./resources/icon/add.png");
        JButton btnProduct = buttonFactory.createStyledButton("Sản phẩm", "./resources/icon/add.png");

        JButton btnAdd = buttonFactory.createStyledButton("Thêm", "./resources/icon/add.png");
        JButton btnEdit = buttonFactory.createStyledButton("Sửa", "./resources/icon/edit.png");
        JButton btnDelete = buttonFactory.createStyledButton("Xóa", "./resources/icon/delete.png");
        JButton btnExport = buttonFactory.createStyledButton("Xuất Excel", "./resources/icon/export.svg");
        JButton btnDetail = buttonFactory.createStyledButton("Chi tiết", null);
        JButton btnDS = buttonFactory.createStyledButton("Xem DS", null);

        // Lấy idNhomQuyen từ tài khoản hiện tại
        String idNhomQuyen = MainFrame.getCurrentAccount().getIdNhomQuyen();

        if (panel instanceof EmployeePanel) {
            EmployeePanel employeePanel = (EmployeePanel) panel;
            EmployeeActionListener actionListener = new EmployeeActionListener(employeePanel);
            addActionListenerToButton(btnAdd, actionListener);
            addActionListenerToButton(btnEdit, actionListener);
            addActionListenerToButton(btnDelete, actionListener);
            addActionListenerToButton(btnExport, actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Nhân viên", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Nhân viên", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Nhân viên", "Xoa");
            checkAndDisableButton(btnExport, idNhomQuyen, "Nhân viên", "Xuat");
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        } else if (panel instanceof CustomerPanel) {
            CustomerPanel customerPanel = (CustomerPanel) panel;
            CustomerActionListener actionListener = new CustomerActionListener(customerPanel);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Khách hàng", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Khách hàng", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Khách hàng", "Xoa");
            checkAndDisableButton(btnExport, idNhomQuyen, "Khách hàng", "Xuat");
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        } else if (panel instanceof ProductPanel) {
            ProductPanel productPanel = (ProductPanel) panel;
            ProductActionListener actionListener = new ProductActionListener(productPanel, MainFrame);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDetail.addActionListener(actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Sản phẩm", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Sản phẩm", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Sản phẩm", "Xoa");
            checkAndDisableButton(btnExport, idNhomQuyen, "Sản phẩm", "Xuat");
            checkAndDisableButton(btnDetail, idNhomQuyen, "Sản phẩm", "Xem");
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
            actionPanel.add(btnDetail);
        } else if (panel instanceof PhieuNhapPanel) {
            PhieuNhapPanel phieuNhapPanel = (PhieuNhapPanel) panel;
            PhieuNhapActionListener actionListener = new PhieuNhapActionListener(phieuNhapPanel, MainFrame);
            btnAdd.addActionListener(actionListener);

            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDetail.addActionListener(actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Phiếu nhập", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Phiếu nhập", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Phiếu nhập", "Xoa");
            checkAndDisableButton(btnExport, idNhomQuyen, "Phiếu nhập", "Xuat");
            actionPanel.add(btnAdd);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        } else if (panel instanceof PromotionPanel) {
            PromotionPanel promotionPanel = (PromotionPanel) panel;
            PromotionActionListener actionListener = new PromotionActionListener(promotionPanel);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Khuyến mãi và ưu đãi", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Khuyến mãi và ưu đãi", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Khuyến mãi và ưu đãi", "Xoa");
            checkAndDisableButton(btnExport, idNhomQuyen, "Khuyến mãi và ưu đãi", "Xuat");
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        } else if (panel instanceof ProductDetailPanel) {
            ProductDetailPanel productPanel = (ProductDetailPanel) panel;
            ProductDetailActionListener actionListener = new ProductDetailActionListener(productPanel, MainFrame);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDS.addActionListener(actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Sản phẩm", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Sản phẩm", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Sản phẩm", "Xoa");
            checkAndDisableButton(btnDS, idNhomQuyen, "Sản phẩm", "Xem");
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnDS);
        } else if (panel instanceof AccountPanel) {
            AccountPanel accountPanel = (AccountPanel) panel;
            AccountActionListener actionListener = new AccountActionListener(accountPanel);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Tài khoản", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Tài khoản", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Tài khoản", "Xoa");
            checkAndDisableButton(btnExport, idNhomQuyen, "Tài khoản", "Xuat");
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        } else if (panel instanceof PermissionPanel) {
            PermissionPanel permissionPanel = (PermissionPanel) panel;
            PermissionActionListener actionListener = new PermissionActionListener(permissionPanel);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDetail.addActionListener(actionListener);
            checkAndDisableButton(btnAdd, idNhomQuyen, "Phân quyền", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Phân quyền", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Phân quyền", "Xoa");
            checkAndDisableButton(btnExport, idNhomQuyen, "Phân quyền", "Xuat");
            checkAndDisableButton(btnDetail, idNhomQuyen, "Phân quyền", "Xem");
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
            actionPanel.add(btnDetail);
        } else if (panel instanceof SaleInvoicePanel) {
            checkAndDisableButton(btnAdd, idNhomQuyen, "Phiếu xuất", "Tao");
            checkAndDisableButton(btnEdit, idNhomQuyen, "Phiếu xuất", "Sua");
            checkAndDisableButton(btnDelete, idNhomQuyen, "Phiếu xuất", "Xoa");
            checkAndDisableButton(btnDS, idNhomQuyen, "Phiếu xuất", "Xem");

            SaleInvoiceActionListener actionListener = new SaleInvoiceActionListener((SaleInvoicePanel) panel, MainFrame);

            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnDetail.addActionListener(actionListener);

            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnDetail);
        }

        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        return actionPanel;
    }

    private void addActionListenerToButton(JButton button, EmployeeActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    private void checkAndDisableButton(JButton button, String idNhomQuyen, String chucNang, String hanhDong) {
//        boolean hasPermission = permissionBUS.hasPermission(idNhomQuyen, permissionBUS.getChucNangIdByName(chucNang), hanhDong);
        boolean hasPermission = true;
        System.out.println("Quyền: " + chucNang + " - " + hanhDong + " = " + hasPermission);
        if (!hasPermission) {
            button.setEnabled(false);
        }
    }

    public static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        searchPanel.setPreferredSize(new Dimension(475, 100));
        searchPanel.setBackground(Color.WHITE);

        JButton btnReset = new JButton("Làm mới");
        btnReset.setPreferredSize(new Dimension(90, 30));
        btnReset.setFont(new Font("Arial", Font.BOLD, 12));

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 1));

        Button buttonFactory = new Button();
        JButton btnSearch = buttonFactory.createStyledButton("Tìm kiếm", "./resources/icon/find.svg");

        btnSearch.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnSearch.setVerticalTextPosition(SwingConstants.CENTER);
        btnSearch.setPreferredSize(new Dimension(100, 40));
        btnSearch.setFont(new Font("Arial", Font.BOLD, 12));

        searchPanel.add(btnReset);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Tìm kiếm",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(100, 149, 237)
        ));


        return searchPanel;
    }

    public JTextField getTextField() {
        return searchField;
    }
}