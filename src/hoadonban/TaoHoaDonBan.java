package hoadonban;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class TaoHoaDonBan extends JPanel {
	JLabel image = new JLabel();
	JTextField field1 = new JTextField();
	JTextField field2 = new JTextField();
	JTextField field3 = new JTextField();
	JTextField field4 = new JTextField();
	JTextField field5 = new JTextField();
	BufferedImage confirmIcon = ImageIO.read(this.getClass().getResource("Tick.png"));
	BufferedImage cancelIcon = ImageIO.read(this.getClass().getResource("Cross.png"));
	JButton confirm = new JButton(new ImageIcon(confirmIcon));
	JButton cancel = new JButton(new ImageIcon(cancelIcon));
	
	public TaoHoaDonBan() throws IOException
	{ 
		setPreferredSize(new Dimension(576, 510));
		setVisible(true);
		setBackground(Color.WHITE);
		setLayout(null);
	
		image.setBounds(42,42,180,180);
		image.setBackground(Color.LIGHT_GRAY);
		image.setOpaque(true);
		
		field1.setBounds(264, 70, 270, 40);
		field1.setBackground(Color.LIGHT_GRAY);
		field1.setOpaque(true);
		
		field2.setBounds(264, 140, 270, 40);
		field2.setBackground(Color.LIGHT_GRAY);
		field2.setOpaque(true);
		
		field3.setBounds(42, 264, 492, 40);
		field3.setBackground(Color.LIGHT_GRAY);
		field3.setOpaque(true);
		
		field4.setBounds(42, 346, 338, 40);
		field4.setBackground(Color.LIGHT_GRAY);
		field4.setOpaque(true);
		
		field5.setBounds(414, 346, 120, 40);
		field5.setBackground(Color.LIGHT_GRAY);
		field5.setOpaque(true);
		
		confirm.setBounds(494, 428, 40, 40);
		confirm.setBackground(Color.GREEN);
		confirm.setOpaque(true);
		
		cancel.setBounds(414, 428, 40, 40);
		cancel.setBackground(Color.RED);
		cancel.setOpaque(true);
		
		add(image);
		add(field1);
		add(field2);
		add(field3);
		add(field4);
		add(field5); 
		add(confirm);
		add(cancel);
	}
}
