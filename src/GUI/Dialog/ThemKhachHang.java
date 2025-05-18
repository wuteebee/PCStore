package GUI.Dialog;
import java.awt.*;

import java.sql.Date;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import BUS.EmployeeBUS;
import BUS.CustomerBUS;

import java.time.LocalDate;
import java.sql.Date;
import DTO.Customer;
import GUI.Panel.CustomerPanel;
import GUI.Panel.EmployeePanel;

public class ThemKhachHang {
    private JTextField txtName, txtEmail, txtPhone;
    private JDateChooser dateChooser;
    private CustomerBUS customerBUS;
    private CustomerPanel employeePanel;

 public ThemKhachHang(CustomerBUS bus, CustomerPanel employeePanel) {
        this.customerBUS = bus;
        this.employeePanel = employeePanel;
    }

    public void FormThemKhachHang(String formname,String textButton,Customer customer) {
        JDialog dialog = new JDialog((Frame) null, formname, true);
        dialog.setSize(520,550);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(8, 10, 8, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        String []labels={"Họ và tên","Số điện thoại","Email","Ngày tham gia"};

        txtName=new JTextField(18);
        
        txtName.setToolTipText("Nhập họ tên và nhấn Enter để tiếp tục");
        txtName.addActionListener(e -> txtPhone.requestFocus());txtEmail=new JTextField(18);
        
        txtEmail.setToolTipText("Nhập email và nhấn Enter để tiếp tục");txtPhone=new JTextField(18);
        
        txtPhone.setToolTipText("Nhập số điện thoại và nhấn Enter để tiếp tục");
        txtPhone.addActionListener(e -> txtEmail.requestFocus());dateChooser=new com.toedter.calendar.JDateChooser();
        dateChooser.setDateFormatString("yyyy/MM/dd"); // Định dạng ngày

        JComponent[] components={txtName,txtPhone,txtEmail,dateChooser};
   
        for(int i=0;i<labels.length;i++){
            gbc.gridx=0;
            gbc.gridy=i;
            gbc.anchor=GridBagConstraints.LINE_END;
            JLabel label=new JLabel(labels[i]);
            label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            dialog.add(label,gbc);

            gbc.gridx=1;
            gbc.anchor=GridBagConstraints.LINE_START;
            if (components[i] instanceof JTextField) {
                ((JTextField) components[i]).setPreferredSize(new Dimension(200,30));
                ((JTextField) components[i]).setBorder(BorderFactory.createLineBorder(Color.gray,1));
            } else if (components[i] instanceof JDateChooser) {
                  components[i].setPreferredSize(new Dimension(210,30));
            }

            dialog.add(components[i],gbc);
        }

        if(customer!=null){
            txtName.setText(customer.getName());
            txtEmail.setText(customer.getEmail());
            txtPhone.setText(customer.getPhoneNumber());
            dateChooser.setDate(java.sql.Date.valueOf(customer.getDateOfJoining()));
        }
        
        JButton btnSubmit = new JButton(textButton);
        btnSubmit.setPreferredSize(new Dimension(100,30));
        btnSubmit.setBackground(new Color(100,149,237));
        btnSubmit.setForeground(Color.white);
        btnSubmit.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));

       gbc.gridx=0;
         gbc.gridy=labels.length;
         gbc.gridwidth=2;
        gbc.anchor=GridBagConstraints.CENTER;
        dialog.add(btnSubmit,gbc);


        if(customer==null){
            btnSubmit.addActionListener(e-> saveCustomer(dialog,false,null));
        }else{
            btnSubmit.addActionListener(e-> saveCustomer(dialog,true,customer.getId()));
        }

        dialog.setVisible(true);

    }




    private void saveCustomer(JDialog dialog, boolean isEdit, String id) {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
    
        java.util.Date utilDate = dateChooser.getDate();
        java.sql.Date date = null;
        if (utilDate != null) {
            date = new java.sql.Date(utilDate.getTime());
        }
    
        // Kiểm tra dữ liệu đầu vào
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || date == null) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        dialog.dispose();
        CustomerBUS customerBUS = new CustomerBUS();
       boolean success=customerBUS.saveCustomer(name, email, phone, date, isEdit, id);
       if(success) {
            JOptionPane.showMessageDialog(dialog, "Lưu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
   
        } else {
            JOptionPane.showMessageDialog(dialog, "Lưu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}