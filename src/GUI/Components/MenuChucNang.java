package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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

public class MenuChucNang {
    private static Color color = Color.WHITE;

    // Tạo ActionPanel cho các chức năng
    public JPanel createActionPanel(JPanel panel,Main MainFrame) {
        JPanel actionPanel = new JPanel(new GridLayout(1, 4, 10, 10)); 
        actionPanel.setPreferredSize(new Dimension(475, 100));  
        actionPanel.setBackground(Color.WHITE);  
        // System.out.println("MenuChucNang: " + panel.getClass().getSimpleName()); 
        Button buttonFactory = new Button();  
        JButton btnAdd = buttonFactory.createStyledButton("Thêm", "./resources/icon/add.png");
        JButton btnEdit = buttonFactory.createStyledButton("Sửa", "./resources/icon/edit.png");
        JButton btnDelete = buttonFactory.createStyledButton("Xóa", "./resources/icon/delete.png");
        JButton btnExport = buttonFactory.createStyledButton("Xuất Excel", "./resources/icon/export.svg");
        JButton btnDetail=new JButton("Chi tiết");
        JButton btnDS=buttonFactory.createStyledButton("Xem DS", null);

// Nếu panel là EmployeePanel, gán sự kiện cho các nút
        if (panel instanceof EmployeePanel) {
            EmployeePanel employeePanel = (EmployeePanel) panel; // Ép kiểu panel về EmployeePanel
            EmployeeActionListener actionListener = new EmployeeActionListener(employeePanel); // Truyền đúng kiểu vào ActionListener
            addActionListenerToButton(btnAdd, actionListener);
            addActionListenerToButton(btnEdit, actionListener);
            addActionListenerToButton(btnDelete, actionListener);
            addActionListenerToButton(btnExport, actionListener);
           

            // Thêm các nút vào actionPanel
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        }
        else if(panel instanceof CustomerPanel) {
            CustomerPanel customerPanel=(CustomerPanel) panel;

            CustomerActionListener actionListener=new CustomerActionListener(customerPanel);
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);  
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
        } 
        else if(panel instanceof ProductPanel){
            ProductPanel productPanel = (ProductPanel) panel; 
            ProductActionListener actionListener = new ProductActionListener(productPanel,MainFrame); 
            
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

            // Thêm các nút vào actionPanel
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnExport);
            actionPanel.add(btnDetail);
        }
        else if (panel instanceof ProductDetailPanel){
            ProductDetailPanel productPanel = (ProductDetailPanel) panel; 
            ProductDetailActionListener actionListener = new ProductDetailActionListener(productPanel,MainFrame); 
            
            btnAdd.addActionListener(actionListener);
            btnEdit.addActionListener(actionListener);  
            btnDelete.addActionListener(actionListener);
            btnExport.addActionListener(actionListener);
            btnDS.addActionListener(actionListener);
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnDS);

            // Thêm các nút vào actionPanel
            actionPanel.add(btnAdd);
            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
            actionPanel.add(btnDS);
        }
            // Nếu không phải là EmployeePanel hoặc CustomerPanel, không thêm nút nào

        


        // Gán border cho actionPanel
        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        
        return actionPanel;
    }

    private void addActionListenerToButton(JButton button, EmployeeActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    // Tạo panel tìm kiếm
    public static JPanel createSearchPanel() {

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); 
        searchPanel.setPreferredSize(new Dimension(475, 100));  
        searchPanel.setBackground(Color.WHITE); 

        JButton btnReset=new JButton("Làm mới");
        btnReset.setPreferredSize(new Dimension(90,30 ));
        btnReset.setFont(new Font("Arial", Font.BOLD, 12));


        JTextField searchField = new JTextField(20); 
        searchField.setFont(new Font("Arial", Font.PLAIN, 16)); 
        searchField.setPreferredSize(new Dimension(250, 40)); 
        searchField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 1)); 

        Button buttonFactory = new Button();
        JButton btnSearch = buttonFactory.createStyledButton("Tìm kiếm", "./resources/icon/find.svg");

        btnSearch.setHorizontalTextPosition(SwingConstants.RIGHT); 
        btnSearch.setVerticalTextPosition(SwingConstants.CENTER);
        btnSearch.setPreferredSize(new Dimension(100,40)); 
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
}
