package GUI.Components;

import GUI.ActionListener.BrandActionListener;
import GUI.ActionListener.CustomerActionListener;
import GUI.ActionListener.EmployeeActionListener;
import GUI.ActionListener.PhieuNhapActionListener;
import GUI.ActionListener.ProductActionListener;
import GUI.ActionListener.ProductDetailActionListener;
import GUI.ActionListener.PromotionActionListener;
import GUI.ActionListener.SupplierActionListener;
import GUI.Main;
import GUI.Panel.*;
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

public class MenuChucNang {
    private static Color color = Color.WHITE;
    private static JTextField searchField;
    private static JButton btnSearch = new Button().createStyledButton("Tìm", null);
    private static JButton btnReset=new Button().createStyledButton("Làm mới", null);
    // Tạo ActionPanel cho các chức năng
    public JPanel createActionPanel(JPanel panel, Main MainFrame) {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,2,2)); 
        actionPanel.setPreferredSize(new Dimension(640,75));  
        actionPanel.setBackground(Color.WHITE);  
        Button buttonFactory = new Button();  
        JButton btnAdd = buttonFactory.createStyledButton("Thêm", "./resources/icon/add.png");
        JButton btnEdit = buttonFactory.createStyledButton("Sửa", "./resources/icon/edit.png");
        JButton btnDelete = buttonFactory.createStyledButton("Xóa", "./resources/icon/delete.png");
        JButton btnExport = buttonFactory.createStyledButton("Xuất Excel", "./resources/icon/export.svg");
        JButton btnDetail = buttonFactory.createStyledButton("Chi tiết",null);
        JButton btnDS = buttonFactory.createStyledButton("Xem DS", null);

        // Nếu panel là EmployeePanel, gán sự kiện cho các nút
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
        if (panel instanceof SupplierPanel) {
            SupplierPanel supplierPanel = (SupplierPanel) panel;
            SupplierActionListener actionListener = new SupplierActionListener(supplierPanel);

            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);

            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        }
        else if (panel instanceof BrandPanel) {
            BrandPanel brandPanel = (BrandPanel) panel;
            BrandActionListener actionListener = new BrandActionListener(brandPanel, MainFrame);

            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);

            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        }
        else if (panel instanceof PhieuNhapPanel) {
            PhieuNhapPanel phieuNhapPanel = (PhieuNhapPanel) panel;
            PhieuNhapActionListener actionListener = new PhieuNhapActionListener(phieuNhapPanel, MainFrame);

            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);  // Nếu bạn có chức năng Xuất Excel
            btnDetail.addActionListener(actionListener);

            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
            actionPanel.add(btnDetail);
        }
        else if (panel instanceof PromotionPanel) {
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

        // Gán border cho actionPanel
        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        
        return actionPanel;
    }

    private void addActionListenerToButton(JButton button, EmployeeActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    // Tạo panel tìm kiếm
    public static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)); 
        searchPanel.setPreferredSize(new Dimension(435, 75));  
        searchPanel.setBackground(Color.WHITE); 



    searchField = new JTextField(); 
        searchField.setFont(new Font("Arial", Font.PLAIN, 16)); 
        searchField.setPreferredSize(new Dimension(230, 30)); 
        searchField.setBorder(BorderFactory.createLineBorder(new Color(80, 149, 237), 1)); 
      
        Button buttonFactory = new Button();
         btnSearch = buttonFactory.createStyledButton("Tìm", "./resources/icon/find.svg");
        btnReset = buttonFactory.createStyledButton("Làm mới", null);
        btnSearch.setPreferredSize(new Dimension(90, 35));
        btnReset.setPreferredSize(new Dimension(90, 35));
        searchPanel.add(btnReset);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(80, 149, 237), 1),
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