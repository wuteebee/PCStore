package GUI.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import GUI.Panel.PromotionPanel;

public class PromotionActionListener implements ActionListener {
    private PromotionPanel panel;

    public PromotionActionListener(PromotionPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
  
        switch (command) {
            case "Thêm":
                panel.openAddPromotionDialog();
                break;
            case "Sửa":
                String id = panel.getSelectedPromotionId();
                if (!id.equals("-1")) {
                    panel.openEditPromotionDialog(id);
                } else {
                    JOptionPane.showMessageDialog(panel, "Vui lòng chọn khuyến mãi cần chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Xóa":
                id = panel.getSelectedPromotionId();
                if (id.equals("-1")) {
                    JOptionPane.showMessageDialog(panel, "Vui lòng chọn khuyến mãi cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                } else {
                    int result = JOptionPane.showConfirmDialog(
                        panel,
                        "Bạn có chắc chắn muốn xóa khuyến mãi " + id + "?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    if (result == JOptionPane.YES_OPTION) {
                        panel.openRemovePromotionDialog(id);
                    }
                }
                break;
            case "Xuất Excel":
                JOptionPane.showMessageDialog(panel, "Chức năng Xuất Excel đang được phát triển.");
                break;
        }
    }
}