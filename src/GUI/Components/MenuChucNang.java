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

import GUI.ActionListener.CustomerActionListener;
import GUI.ActionListener.EmployeeActionListener;
import GUI.Panel.CustomerPanel;
import GUI.Panel.EmployeePanel;

public class MenuChucNang {
    private static Color color = Color.WHITE;

    // Tạo ActionPanel cho các chức năng
    public JPanel createActionPanel(JPanel panel) {
        JPanel actionPanel = new JPanel(new GridLayout(1, 4, 10, 10)); 
        actionPanel.setPreferredSize(new Dimension(475, 100));  
        actionPanel.setBackground(Color.LIGHT_GRAY);  
        // System.out.println("MenuChucNang: " + panel.getClass().getSimpleName()); 
        Button buttonFactory = new Button();  
        JButton btnAdd = buttonFactory.createStyledButton("Thêm", "./resources/icon/add.svg");
        JButton btnEdit = buttonFactory.createStyledButton("Sửa", "./resources/icon/edit.svg");
        JButton btnDelete = buttonFactory.createStyledButton("Xóa", "./resources/icon/delete.svg");
        JButton btnExport = buttonFactory.createStyledButton("Xuất Excel", "./resources/icon/export.svg");

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
        } else {
            // Nếu không phải là EmployeePanel hoặc CustomerPanel, không thêm nút nào

        }


        // Gán border cho actionPanel
        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        
        return actionPanel;
    }

    // Phương thức hỗ trợ gán ActionListener vào nút
    private void addActionListenerToButton(JButton button, EmployeeActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    // Tạo panel tìm kiếm
    public static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); 
        searchPanel.setPreferredSize(new Dimension(475, 100));  
        searchPanel.setBackground(Color.WHITE); 

        JTextField searchField = new JTextField(20); 
        searchField.setFont(new Font("Arial", Font.PLAIN, 16)); 
        searchField.setPreferredSize(new Dimension(300, 40)); 
        searchField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2)); 

        Button buttonFactory = new Button();
        JButton btnSearch = buttonFactory.createStyledButton("Tìm kiếm", "./resources/icon/find.svg");

        btnSearch.setHorizontalTextPosition(SwingConstants.RIGHT); 
        btnSearch.setVerticalTextPosition(SwingConstants.CENTER);
        btnSearch.setPreferredSize(new Dimension(120, 40)); 
        btnSearch.setFont(new Font("Arial", Font.BOLD, 16)); 

        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
            "Tìm kiếm", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 14), 
            new Color(100, 149, 237) 
        ));

        return searchPanel;
    }
}
