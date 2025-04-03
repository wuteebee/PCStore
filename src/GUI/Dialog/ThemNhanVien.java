package GUI.Dialog;

import BUS.EmployeeBUS;
import DTO.Employee;
import GUI.Panel.EmployeePanel;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

public class ThemNhanVien {
    private JTextField txtName, txtEmail, txtPosition, txtPhone, txtSalary;
    private JComboBox<String> cbGender;
    private JDateChooser dateChooser;
    private EmployeeBUS employeeBUS;
    private EmployeePanel employeePanel;

   public ThemNhanVien(EmployeeBUS bus, EmployeePanel employeePanel) {
    this.employeeBUS = bus;
    this.employeePanel = employeePanel;
}

public void FormThemNv(String formname, String textButton, Employee nhanvien) {
    JDialog dialog = new JDialog((Frame) null, formname, true);
    dialog.setSize(520, 500);  // Điều chỉnh kích thước dialog sau khi bỏ "Giới tính"
    dialog.setLayout(new GridBagLayout());
    dialog.setLocationRelativeTo(null);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 10, 8, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;

    // Cập nhật mảng labels, bỏ "Giới tính"
    String[] labels = {"Họ và Tên:", "Email:", "Chức vụ:", "Số điện thoại:", "Ngày vào làm:"};

    // Các trường nhập
    txtName = new JTextField(18);
    txtEmail = new JTextField(18);
    txtPosition = new JTextField(18);
    txtPhone = new JTextField(18);
    dateChooser = new JDateChooser();
    dateChooser.setDateFormatString("yyyy/MM/dd");  // Định dạng ngày

    JComponent[] components = {txtName, txtEmail, txtPosition, txtPhone, dateChooser};
    
    // Thêm các trường vào dialog
    for (int i = 0; i < labels.length; i++) {
        gbc.gridx = 0;
        gbc.gridy = i;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel label = new JLabel(labels[i]);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        dialog.add(label, gbc);
    
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        if (components[i] instanceof JTextField) {
            ((JTextField) components[i]).setPreferredSize(new Dimension(200, 30));
            ((JTextField) components[i]).setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        } else if (components[i] instanceof JDateChooser) {
            components[i].setPreferredSize(new Dimension(210, 30));
        }
        dialog.add(components[i], gbc);
    }
    
    // Nếu nhanvien khác null, điền thông tin vào các trường nhập
    if (nhanvien != null) {
        txtName.setText(nhanvien.getName());
        txtEmail.setText(nhanvien.getEmail());
        txtPosition.setText(nhanvien.getPosition());
        txtPhone.setText(nhanvien.getPhoneNumber());
        dateChooser.setDate(java.sql.Date.valueOf(nhanvien.getDateOfJoining()));
    }

    JButton btnSave = new JButton(textButton);
    btnSave.setFont(new Font("Arial", Font.BOLD, 14));
    btnSave.setBackground(new Color(50, 150, 250));
    btnSave.setForeground(Color.WHITE);
    btnSave.setFocusPainted(false);
    btnSave.setPreferredSize(new Dimension(120, 35));

    gbc.gridx = 0;
    gbc.gridy = labels.length;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    dialog.add(btnSave, gbc);
if (nhanvien==null){
    btnSave.addActionListener(e -> saveEmployee(dialog,false));  // Truyền thêm nhanvien nếu cần để cập nhật
}
else{
    btnSave.addActionListener(e -> saveEmployee(dialog,true)); 
}

    dialog.setVisible(true);
}

private void saveEmployee(JDialog dialog, boolean capnhat) {
    String name = txtName.getText();
    String email = txtEmail.getText();
    String position = txtPosition.getText();
    String phone = txtPhone.getText();

    // Kiểm tra ngày
    Date date = dateChooser.getDate();
    System.err.println(date);
    LocalDate dateOfJoining = null;
    if (date != null) {
        dateOfJoining = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    // Kiểm tra các trường nhập liệu
    if (name.isEmpty() || email.isEmpty() || position.isEmpty() || phone.isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    } else {
        boolean success;
        if (capnhat) {
            // Cập nhật nhân viên nếu capnhat là true
            Employee updatedEmployee = new Employee(
                employeePanel.getSelectedEmployeeId(), 
                name, 
                position, 
                phone, 
                email, 
                dateOfJoining
            );
            success = employeeBUS.updateEmployee(updatedEmployee);
        } else {
            // Thêm nhân viên mới nếu capnhat là false
            Employee newEmployee = new Employee(
                name, 
                position, 
                phone, 
                email, 
                dateOfJoining
            );
            success = employeeBUS.addEmployee(newEmployee);
        }

        // Xử lý kết quả thêm hoặc cập nhật
        if (success) {
            String message = capnhat ? "Cập nhật nhân viên thành công!" : "Thêm nhân viên thành công!";
            JOptionPane.showMessageDialog(dialog, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();

            // Nếu panel nhân viên không phải null, tải lại danh sách nhân viên
            if (employeePanel != null) {
                employeePanel.loadEmployeeTable();
            }
        } else {
            String errorMessage = capnhat ? "Lỗi khi cập nhật nhân viên!" : "Lỗi khi thêm nhân viên!";
            JOptionPane.showMessageDialog(dialog, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}



    public void openEditEmployeeDialog(){

    }
}
