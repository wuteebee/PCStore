package GUI.ActionListener;

import BUS.PhieuNhapBUS;
import DTO.HoaDonNhap;
import GUI.Dialog.ThemPhieuNhap;
import GUI.Panel.PhieuNhapPanel;
import GUI.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhieuNhapActionListener implements ActionListener {
    private final PhieuNhapPanel panel;
    private final Main mainFrame;

    public PhieuNhapActionListener(PhieuNhapPanel panel, Main mainFrame) {
        this.panel = panel;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Thêm":
                new ThemPhieuNhap(mainFrame, panel, false, null).setVisible(true);
                break;
            case "Sửa":
                String id = panel.getSelectedPhieuId();
                if (!id.equals("-1")) {
                    HoaDonNhap hdn = new PhieuNhapBUS().getPhieuNhapById(id);
                    new ThemPhieuNhap(mainFrame, panel, true, hdn).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để sửa.");
                }
                break;
            case "Xóa":
                String idDel = panel.getSelectedPhieuId();
                if (!idDel.equals("-1")) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa phiếu này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        new PhieuNhapBUS().xoaPhieuNhap(idDel);
                        panel.refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để xóa.");
                }
                break;
            case "Chi tiết":
                String idDetail = panel.getSelectedPhieuId();
                if (!idDetail.equals("-1")) {
                    JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập: " + idDetail + "\n(Tạm thời chỉ hiển thị đơn giản)");
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu để xem chi tiết.");
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Hành động không hợp lệ: " + command);
        }
    }
}
