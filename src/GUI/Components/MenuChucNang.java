package GUI.Components;

import GUI.ActionListener.AccountActionListener;
import GUI.ActionListener.CustomerActionListener;
import GUI.ActionListener.DashboardActionListener;
import GUI.ActionListener.EmployeeActionListener;
import GUI.ActionListener.PhieuNhapActionListener;
import GUI.ActionListener.ProductActionListener;
import GUI.ActionListener.ProductDetailActionListener;
import GUI.ActionListener.PromotionActionListener;

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

        if (panel instanceof EmployeePanel) {
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
        }
        else if (panel instanceof DashboardPanel) {
            DashboardPanel dashboardPanel = (DashboardPanel) panel;
            DashboardActionListener actionListener = new DashboardActionListener(dashboardPanel);
            btnFinance.addActionListener(actionListener);
            btnEmployee.addActionListener(actionListener);
            btnProduct.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            actionPanel.add(btnFinance);
            actionPanel.add(btnProduct);
            actionPanel.add(btnEmployee);
            actionPanel.add(btnExport);
        }
        else if (panel instanceof CustomerPanel) {
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
        } 
        else if (panel instanceof ProductPanel) {
            ProductPanel productPanel = (ProductPanel) panel; 
            ProductActionListener actionListener = new ProductActionListener(productPanel, MainFrame); 
            
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDetail.addActionListener(actionListener);
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
            actionPanel.add(btnDetail);
        }
        
        else if (panel instanceof PhieuNhapPanel) {
            PhieuNhapPanel phieuNhapPanel = (PhieuNhapPanel) panel;
            PhieuNhapActionListener actionListener = new PhieuNhapActionListener(phieuNhapPanel,MainFrame);
            btnAdd.addActionListener(actionListener);
       
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDetail.addActionListener(actionListener);

            actionPanel.add(btnAdd);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        }
        else if (panel instanceof PhieuNhapPanel) {
            PhieuNhapPanel phieuNhapPanel = (PhieuNhapPanel) panel;

            // btnEdit.addActionListener(e -> {
            //     String id = phieuNhapPanel.getSelectedPhieuId();
            //     if (!id.equals("-1")) {
            //         DTO.HoaDonNhap hdn = new BUS.PhieuNhapBUS().getPhieuNhapById(id);
            //         new GUI.Dialog.ThemPhieuNhap(MainFrame, phieuNhapPanel, true, hdn).setVisible(true);
            //     } else {
            //         JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để sửa.");
            //     }
            // });

            // btnDelete.addActionListener(e -> {
            //     String id = phieuNhapPanel.getSelectedPhieuId();
            //     if (!id.equals("-1")) {
            //         int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa phiếu này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            //         if (confirm == JOptionPane.YES_OPTION) {
            //             new BUS.PhieuNhapBUS().xoaPhieuNhap(id);
            //             phieuNhapPanel.refreshTable();
            //         }
            //     } else {
            //         JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để xóa.");
            //     }
            // });

            // btnExport.addActionListener(e -> {
            //     JOptionPane.showMessageDialog(null, "Chức năng Xuất Excel đang được phát triển.");
            // });

            // btnDetail.addActionListener(e -> {
            //     String id = phieuNhapPanel.getSelectedPhieuId();
            //     if (!id.equals("-1")) {
            //         JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập: " + id + "\n(Tạm thời chỉ hiển thị đơn giản)");
            //     } else {
            //         JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu để xem chi tiết.");
            //     }
            // });

            // actionPanel.add(btnAdd);
            // actionPanel.add(btnEdit);
            // actionPanel.add(btnDelete);
            // actionPanel.add(btnExport);
            // actionPanel.add(btnDetail);
        } else if (panel instanceof PromotionPanel) {
            PromotionPanel promotionPanel = (PromotionPanel) panel;
            PromotionActionListener actionListener = new PromotionActionListener(promotionPanel);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        }
        else if (panel instanceof ProductDetailPanel) {
            ProductDetailPanel productPanel = (ProductDetailPanel) panel; 
            ProductDetailActionListener actionListener = new ProductDetailActionListener(productPanel, MainFrame); 
            
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDS.addActionListener(actionListener);
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnDS);
        }
        else if (panel instanceof SaleInvoicePanel)
        {
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnDS);
        }

        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        
        return actionPanel;
    }

    private void addActionListenerToButton(JButton button, EmployeeActionListener actionListener) {
        button.addActionListener(actionListener);
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