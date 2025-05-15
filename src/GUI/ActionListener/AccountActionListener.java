package GUI.ActionListener;

import GUI.Components.Excel;
import GUI.Panel.AccountPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import BUS.ExcelBUS;

public class AccountActionListener implements ActionListener {
    private AccountPanel accountPanel;

    public AccountActionListener(AccountPanel accountPanel) {
        this.accountPanel = accountPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Thêm":
                accountPanel.openAddAccountDialog();
                break;
            case "Sửa":
                String idEdit = accountPanel.getSelectedAccountId();
                if (!idEdit.equals("-1")) {
                    accountPanel.openEditAccountDialog(idEdit);
                } else {
                    JOptionPane.showMessageDialog(accountPanel, "Vui lòng chọn một tài khoản để sửa!");
                }
                break;
            case "Xóa":
                String idDelete = accountPanel.getSelectedAccountId();
                if (!idDelete.equals("-1")) {
                    int confirm = JOptionPane.showConfirmDialog(accountPanel, 
                        "Bạn có chắc chắn muốn xóa tài khoản này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        accountPanel.openRemoveAccountDialog(idDelete);
                    }
                } else {
                    JOptionPane.showMessageDialog(accountPanel, "Vui lòng chọn một tài khoản để xóa!");
                }
                break;
            case "Xuất Excel":
                System.out.println("Xuất Excel");
                Excel panelExcel = new Excel();
                String filePath = panelExcel.ChooseFile();
                if (filePath != null) {
                    ExcelBUS excelBUS = new ExcelBUS();
                    try {
                        excelBUS.ExcelListAccount(filePath);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                break;
            case "Chi tiết":
                String idDetail = accountPanel.getSelectedAccountId();
                if (!idDetail.equals("-1")) {
                    new GUI.Dialog.AccountDetailDialog(accountPanel, idDetail).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(accountPanel, "Vui lòng chọn một tài khoản để xem chi tiết!");
                }
                break;
        }
    }
}