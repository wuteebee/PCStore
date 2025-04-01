package hoadonban;

import java.awt.*;
import javax.swing.*;

public class ThongTinSanPham extends JPanel {

	JLabel foo = new JLabel();
	JLabel foo1 = new JLabel();
	public ThongTinSanPham() {
		setPreferredSize(new Dimension (255,216));
		setVisible(true);
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		c1.insets = new Insets(0, 34, 0, 34);
		foo.setPreferredSize(new Dimension(187, 40));
		foo.setBackground(Color.LIGHT_GRAY);
		foo.setOpaque(true);
		add(foo, c1);
		
		c2.gridx = 0;
		c2.gridy = 1;
		c2.insets = new Insets(48, 34, 0, 34);
		foo1.setPreferredSize(new Dimension(187, 40));
		foo1.setBackground(Color.LIGHT_GRAY);
		foo1.setOpaque(true);
		add(foo1, c2);
	}

}
