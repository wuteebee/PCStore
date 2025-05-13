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
    private PhieuNhapPanel panel;

    public ThemPhieuNhap(PhieuNhapPanel panel, HoaDonNhap hdn) {
        this.panel = panel;
        this.isEdit = hdn != null;
        this.editingId = isEdit ? hdn.getIdHoaDonNhap() : null;
        
    }
    private void initComponent(){
        setTitle(isEdit ? "Sửa Phiếu Nhập" : "Thêm Phiếu Nhập");
        setSize(550,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout());

        

    }

}
