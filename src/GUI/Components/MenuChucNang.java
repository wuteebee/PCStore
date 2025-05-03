package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import GUI.Main;
import GUI.ActionListener.CustomerActionListener;
import GUI.ActionListener.EmployeeActionListener;
import GUI.ActionListener.ProductActionListener;
import GUI.ActionListener.ProductDetailActionListener;
import GUI.Panel.CustomerPanel;
import GUI.Panel.EmployeePanel;
import GUI.Panel.ProductDetailPanel;
import GUI.Panel.ProductPanel;
import GUI.Panel.SupplierPanel;
import GUI.Panel.PhieuNhapPanel;

public class MenuChucNang {
    private static Color color = Color.WHITE;
    private static JTextField searchField;
    private static JButton btnSearch = new Button().createStyledButton("Tìm", null);
    private static JButton btnReset=new Button().createStyledButton("Làm mới", null);
    
    
    public JPanel createActionPanel(JPanel panel, Main MainFrame) {
        btnReset.setPreferredSize(new Dimension(90, 30));
        btnSearch.setPreferredSize(new Dimension(90, 30));
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setPreferredSize(new Dimension(630, 70));
        actionPanel.setBackground(Color.WHITE);

        Button buttonFactory = new Button();
        JButton btnAdd = buttonFactory.createStyledButton("Thêm", "./resources/icon/add.png");
        JButton btnEdit = buttonFactory.createStyledButton("Sửa", "./resources/icon/edit.png");
        JButton btnDelete = buttonFactory.createStyledButton("Xóa", "./resources/icon/delete.png");
        JButton btnExport = buttonFactory.createStyledButton("Excel", "./resources/icon/export.svg");
        JButton btnDetail = buttonFactory.createStyledButton("Chi tiết",null );
        JButton btnDS = buttonFactory.createStyledButton("Xem DS", null);

        switch (panel.getClass().getSimpleName()) {
            case "EmployeePanel": {
                EmployeePanel employeePanel = (EmployeePanel) panel;
                EmployeeActionListener actionListener = new EmployeeActionListener(employeePanel);
                addActionListenerToButton(btnAdd, actionListener);
                addActionListenerToButton(btnEdit, actionListener);
                addActionListenerToButton(btnDelete, actionListener);
                addActionListenerToButton(btnExport, actionListener);

                actionPanel.add(btnAdd);
                actionPanel.add(btnEdit);
                actionPanel.add(btnDelete);
                actionPanel.add(btnExport);
                break;
            }

            case "CustomerPanel": {
                CustomerPanel customerPanel = (CustomerPanel) panel;
                CustomerActionListener actionListener = new CustomerActionListener(customerPanel);
                btnAdd.addActionListener(actionListener);
                btnEdit.addActionListener(actionListener);
                btnDelete.addActionListener(actionListener);
                btnExport.addActionListener(actionListener);

                actionPanel.add(btnAdd);
                actionPanel.add(btnEdit);
                actionPanel.add(btnDelete);
                actionPanel.add(btnExport);
                break;
            }

            case "SupplierPanel": {
                SupplierPanel supplierPanel = (SupplierPanel) panel;
                btnAdd.addActionListener(e -> supplierPanel.openAddSupplierDialog());
                btnEdit.addActionListener(e -> {
                    String id = supplierPanel.getSelectedSupplierId();
                    if (!id.equals("-1")) supplierPanel.openEditSupplierDialog(id);
                });
                btnDelete.addActionListener(e -> {
                    String id = supplierPanel.getSelectedSupplierId();
                    if (!id.equals("-1")) supplierPanel.openRemoveSupplierDialog(id);
                });

                actionPanel.add(btnAdd);
                actionPanel.add(btnEdit);
                actionPanel.add(btnDelete);
                actionPanel.add(btnExport);
                break;
            }

            case "PhieuNhapPanel": {
                PhieuNhapPanel phieuNhapPanel = (PhieuNhapPanel) panel;

                btnAdd.addActionListener(e -> {
                    new GUI.Dialog.ThemPhieuNhap(MainFrame, phieuNhapPanel, false, null).setVisible(true);
                });

                btnEdit.addActionListener(e -> {
                    String id = phieuNhapPanel.getSelectedPhieuId();
                    if (!id.equals("-1")) {
                        DTO.HoaDonNhap hdn = new BUS.PhieuNhapBUS().getPhieuNhapById(id);
                        new GUI.Dialog.ThemPhieuNhap(MainFrame, phieuNhapPanel, true, hdn).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để sửa.");
                    }
                });

                btnDelete.addActionListener(e -> {
                    String id = phieuNhapPanel.getSelectedPhieuId();
                    if (!id.equals("-1")) {
                        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa phiếu này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            new BUS.PhieuNhapBUS().xoaPhieuNhap(id);
                            phieuNhapPanel.refreshTable();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để xóa.");
                    }
                });

                btnExport.addActionListener(e -> {
                    JOptionPane.showMessageDialog(null, "Chức năng Xuất Excel đang được phát triển.");
                });

                btnDetail.addActionListener(e -> {
                    String id = phieuNhapPanel.getSelectedPhieuId();
                    if (!id.equals("-1")) {
                        JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập: " + id + "\n(Tạm thời chỉ hiển thị đơn giản)");
                    } else {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu để xem chi tiết.");
                    }
                });

                actionPanel.add(btnAdd);
                actionPanel.add(btnEdit);
                actionPanel.add(btnDelete);
                actionPanel.add(btnExport);
                actionPanel.add(btnDetail);
                break;
            }

            case "ProductPanel": {
                ProductPanel productPanel = (ProductPanel) panel;
                ProductActionListener actionListener = new ProductActionListener(productPanel, MainFrame);

                btnAdd.addActionListener(actionListener);
                btnEdit.addActionListener(actionListener);
                btnDelete.addActionListener(actionListener);
                btnExport.addActionListener(actionListener);
                btnDetail.addActionListener(actionListener);
  
                btnReset.addActionListener(actionListener);
                btnSearch.addActionListener(actionListener);

                actionPanel.add(btnAdd);
                actionPanel.add(btnEdit);
                actionPanel.add(btnDelete);
                actionPanel.add(btnExport);
                actionPanel.add(btnDetail);

                break;
            }

            case "ProductDetailPanel": {
                ProductDetailPanel productPanel = (ProductDetailPanel) panel;
                ProductDetailActionListener actionListener = new ProductDetailActionListener(productPanel, MainFrame);

                btnAdd.addActionListener(actionListener);
                btnEdit.addActionListener(actionListener);
                btnDelete.addActionListener(actionListener);
                btnDS.addActionListener(actionListener);

                actionPanel.add(btnAdd);
                actionPanel.add(btnEdit);
                actionPanel.add(btnDelete);
                actionPanel.add(btnDS);
                break;
            }

            default:
                break;
        }

        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        return actionPanel;
    }

    private void addActionListenerToButton(JButton button, EmployeeActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    // Tạo panel tìm kiếm
    public static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        // Reduced size from (350, 100) to (300, 70)
        searchPanel.setPreferredSize(new Dimension(440, 70));
        searchPanel.setBackground(Color.WHITE);

        btnReset.setText("Làm mới");
        // Optionally adjust btnReset size if needed
        // btnReset.setPreferredSize(new Dimension(70, 30));
        // btnReset.setFont(new Font("Arial", Font.BOLD, 10));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 10));
        // Adjust search field size if necessary
        searchField.setPreferredSize(new Dimension(150, 30));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 1));

        btnSearch.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnSearch.setVerticalTextPosition(SwingConstants.CENTER);
        // Optionally adjust btnSearch size if needed
        // btnSearch.setPreferredSize(new Dimension(80, 30));
        // btnSearch.setFont(new Font("Arial", Font.BOLD, 10));
        btnSearch.setActionCommand("search");

        searchPanel.add(btnReset);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Tìm kiếm",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 10),
            new Color(100, 149, 237)
        ));

        return searchPanel;
    }

    public JTextField getTextField() {
        return searchField;
    }
}
