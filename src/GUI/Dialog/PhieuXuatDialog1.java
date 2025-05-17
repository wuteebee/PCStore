package GUI.Dialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuXuatDialog1 extends JDialog {
    JCheckBox check = new JCheckBox();
    List<String> serialList;
    List<String> selectedList = new ArrayList<>();
    JButton confirm = new JButton("Confirm");
    PhieuXuatDialog1(PhieuXuatDialog parent, List<String> serialList)
    {
        super(parent, "Chọn số seri", true);
        this.serialList = serialList;
        setLayout(new GridLayout(10,1));
        setSize(parent.getButtonSize());
        setLocation(parent.getButtonLocation());
        getContentPane().setBackground(Color.white);
        initDialog();
        pack();
        setVisible(true);
    }

    private void initDialog()
    {
        for (int i = 0; i < serialList.size(); i++) {
            final String value = serialList.get(i);
            check = new JCheckBox(value);
            check.addActionListener(e->{
                JCheckBox cb = (JCheckBox) e.getSource();
                if (cb.isSelected())
                    selectedList.add(value);
                else
                    selectedList.remove(value);
            });
            add(check);;
        }
        add(confirm);
        confirm.addActionListener(e -> {
            for (int i = 0; i < selectedList.size(); i++) {
                System.out.println(selectedList.get(i));
            }
        });
    }

    public List<String> getSelectedList()
    {
            return selectedList;
    }
}
