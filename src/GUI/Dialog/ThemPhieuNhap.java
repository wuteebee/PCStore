package GUI.Dialog;

import BUS.PhieuNhapBUS;
import DTO.HoaDonNhap;
import GUI.Panel.PhieuNhapPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class ThemPhieuNhap extends JDialog {
    private JTextField txtId, txtIdNhanVien, txtIdNhaCungCap, txtTongTien;
    private PhieuNhapPanel parentPanel;
    private PhieuNhapBUS bus;
    private boolean isEdit;
    private String editingId;

    public ThemPhieuNhap(Frame owner, PhieuNhapPanel parentPanel, boolean isEdit, HoaDonNhap data) {
        super(owner, isEdit ? "Cập nhật phiếu nhập" : "Thêm phiếu nhập", true);
        this.parentPanel = parentPanel;
        this.bus = new PhieuNhapBUS();
        this.isEdit = isEdit;
        this.editingId = isEdit ? data.getIdHoaDonNhap() : null;

        initUI(data);
    }

    private void initUI(HoaDonNhap data) {
        setSize(400, 350);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(20);
        txtIdNhanVien = new JTextField(20);
        txtIdNhaCungCap = new JTextField(20);
        txtTongTien = new JTextField(20);

        int y = 0;
        addLabelAndField("Mã phiếu nhập:", txtId, gbc, y++);
        addLabelAndField("Mã nhân viên:", txtIdNhanVien, gbc, y++);
        addLabelAndField("Mã nhà cung cấp:", txtIdNhaCungCap, gbc, y++);
        addLabelAndField("Tổng tiền:", txtTongTien, gbc, y++);

        if (isEdit && data != null) {
            txtId.setText(data.getIdHoaDonNhap());
            txtId.setEditable(false);
            txtIdNhanVien.setText(data.getIdNhanVien());
            txtIdNhaCungCap.setText(data.getIdNhaCungCap());
            txtTongTien.setText(String.valueOf(data.getTongTien()));
        }

        JButton btnSave = new JButton(isEdit ? "Cập nhật" : "Thêm");
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            if (savePhieuNhap()) {
                parentPanel.refreshTable();
                dispose();
            }
        });
    }

    private void addLabelAndField(String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private boolean savePhieuNhap() {
        String id = txtId.getText().trim();
        String idNV = txtIdNhanVien.getText().trim();
        String idNCC = txtIdNhaCungCap.getText().trim();
        String tongTienStr = txtTongTien.getText().trim();

        if (id.isEmpty() || idNV.isEmpty() || idNCC.isEmpty() || tongTienStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        double tongTien;
        try {
            tongTien = Double.parseDouble(tongTienStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tổng tiền không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        HoaDonNhap hdn = new HoaDonNhap(id, idNV, idNCC, Date.valueOf(LocalDate.now()), tongTien);

        boolean success;
        if (isEdit) {
            success = bus.themPhieuNhap(hdn); // Trong DB bạn có thể dùng update nếu cần
        } else {
            success = bus.themPhieuNhap(hdn);
        }

        if (!success) {
            JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }
}
