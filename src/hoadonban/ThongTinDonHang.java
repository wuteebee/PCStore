package hoadonban;

import javax.swing.*;
import java.awt.*;

public class ThongTinDonHang extends JPanel{
	JTextField HoKH = new JTextField("Ho Khach Hang");
	JTextField TenKH = new JTextField("Ten Khach Hang");
	JTextField SDT = new JTextField("So dien thoai");
	JTextField SDT1 = new JTextField("So dien thoai");
	TableSanPham form = new TableSanPham();
	
	public ThongTinDonHang()
	{ 
		setPreferredSize(new Dimension(622,620));
		setLayout(null);
		setVisible(true);
		setBackground(Color.WHITE);
		
		HoKH.setBorder(BorderFactory.createEmptyBorder(0, 15, 0 , 0));
		HoKH.setPreferredSize(new Dimension(187, 40));
		HoKH.setBackground(Color.LIGHT_GRAY);
		HoKH.setBounds(60, 42, 187, 40);
		add(HoKH);
		
		TenKH.setBorder(BorderFactory.createEmptyBorder(0, 15, 0 , 0));
		TenKH.setPreferredSize(new Dimension(293, 40));
		TenKH.setBackground(Color.LIGHT_GRAY);
		TenKH.setBounds(270, 42, 293, 40);
		add(TenKH);
		
		SDT.setBorder(BorderFactory.createEmptyBorder(0, 15, 0 , 0));
		SDT.setPreferredSize(new Dimension(322, 40));
		SDT.setBackground(Color.LIGHT_GRAY);
		SDT.setBounds(60, 130, 322, 40);
		add(SDT);
		
		SDT1.setBorder(BorderFactory.createEmptyBorder(0, 15, 0 , 0));
		SDT1.setPreferredSize(new Dimension(157, 40));
		SDT1.setBackground(Color.LIGHT_GRAY);
		SDT1.setBounds(405, 130, 157, 40);
		add(SDT1);
		
		form.setBounds(60, 216, 502, 356);
		add(form);
	}
}
