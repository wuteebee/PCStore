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

import GUI.ActionListener.EmployeeActionListener;
import GUI.Panel.EmployeePanel;


public class MenuChucNang {
    private static Color color = Color.WHITE;

    public JPanel createActionPanel(EmployeePanel panel) {
        JPanel actionPanel = new JPanel(new GridLayout(1, 4, 10, 10)); 
        actionPanel.setPreferredSize(new Dimension(475, 100));  
        actionPanel.setBackground(Color.LIGHT_GRAY);  

        Button buttonFactory = new Button();  
        JButton btnAdd = buttonFactory.createStyledButton("Thêm", "./resources/icon/add.svg");
        JButton btnEdit = buttonFactory.createStyledButton("Sửa", "./resources/icon/edit.svg");
        JButton btnDelete = buttonFactory.createStyledButton("Xóa", "./resources/icon/delete.svg");
        JButton btnExport = buttonFactory.createStyledButton("Xuất Excel", "./resources/icon/export.svg");

        // Tạo một đối tượng EmployeeActionListener và truyền vào EmployeePanel hiện tại
        EmployeeActionListener actionListener = new EmployeeActionListener(panel);

        // Gán sự kiện cho các nút
        btnAdd.addActionListener(actionListener);
        btnEdit.addActionListener(actionListener);
        btnDelete.addActionListener(actionListener);
        btnExport.addActionListener(actionListener);

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnExport);

        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));

        return actionPanel;
    }
    public static JPanel createSearchPanel() { // Đổi private -> public static
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); 
        searchPanel.setPreferredSize(new Dimension(475, 100));  
        searchPanel.setBackground(Color.WHITE); 

        JTextField searchField = new JTextField(20); 
        searchField.setFont(new Font("Arial", Font.PLAIN, 16)); 
        searchField.setPreferredSize(new Dimension(300, 40)); 
        searchField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2)); 

        Button buttonFactory = new Button();
        JButton btnSearch = buttonFactory.createStyledButton("Tìm kiếm", "./resources/icon/find.svg"); // Sửa tên nút

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
