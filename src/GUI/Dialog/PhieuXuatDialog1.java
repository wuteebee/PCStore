package GUI.Dialog;

import net.miginfocom.layout.Grid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuXuatDialog1 extends JDialog {
    JPanel panel = new JPanel();
    JScrollPane scroll = new JScrollPane();
    JCheckBox check = new JCheckBox();
    JCheckBox checkAll = new JCheckBox();
    List<String> serialList;
    List<String> selectedList = new ArrayList<>();
    JButton confirm = new JButton("Confirm");
    PhieuXuatDialog1(PhieuXuatDialog parent, List<String> serialList)
    {
        super(parent, true);
        this.serialList = serialList;
        setLayout(new GridBagLayout());
        setSize(170, 200);
        setLocation(parent.getButtonLocation());
        getContentPane().setBackground(Color.white);
        initDialog();
        setVisible(true);
    }

    private void initDialog()
    {
        GridLayout layout = new GridLayout(serialList.size() + 1,0);
        panel.setLayout(layout);
        JButton cancel = new JButton("Cancel");

        checkAll = new JCheckBox("Select all");
        panel.add(checkAll);
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
            panel.add(check);;
        }

        checkAll.addActionListener(e -> {
            if (checkAll.isSelected()) {
                for (Component x : panel.getComponents()) {
                    if (x instanceof JCheckBox && !((JCheckBox) x).getText().equals("Select all")) {
                        ((JCheckBox) x).setSelected(true);
                        selectedList.add(((JCheckBox) x).getText());
                    }
                }
            }
            else {
                for (Component x : panel.getComponents()) {
                    if (x instanceof JCheckBox && !((JCheckBox) x).getText().equals("Select all")) {
                        ((JCheckBox) x).setSelected(false);
                    }
                }
                selectedList.removeAll(selectedList);
            }
        });
        scroll.setViewportView(panel);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.weighty = 0.8;
        c.weightx = 1;
        add(scroll, c);
        c.weighty=0.1;
        c.gridy = 1;
        add(confirm, c);
        c.gridy = 2;
        add(cancel, c);
        confirm.addActionListener(e -> {
            dispose();
        });
        cancel.addActionListener(e -> {
            selectedList.removeAll(selectedList);
            dispose();
        });
    }

    public List<String> getSelectedList()
    {
            return selectedList;
    }
}
