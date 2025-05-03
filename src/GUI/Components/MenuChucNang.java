package GUI.Components;

import GUI.ActionListener.CustomerActionListener;
import GUI.ActionListener.EmployeeActionListener;
import GUI.ActionListener.ProductActionListener;
import GUI.ActionListener.ProductDetailActionListener;
import GUI.ActionListener.PromotionActionListener;
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
        else if(panel instanceof SupplierPanel) {
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


        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 10));
        // Adjust search field size if necessary
        searchField.setPreferredSize(new Dimension(150, 30));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 1));

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
            new Font("Arial", Font.BOLD, 10),
            new Color(100, 149, 237)
        ));

        return searchPanel;
    }

    public JTextField getTextField() {
        return searchField;
    }
}