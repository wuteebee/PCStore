package hoadonban;

import javax.swing.*;
import java.awt.*;
public class ThongTinKhuyenMai extends JPanel{
	JLabel points = new JLabel("Điểm tích lũy: ");
	String[] fodder = {"Combo", "Hóa đơn"};
	JComboBox list = new JComboBox(fodder);
	public ThongTinKhuyenMai()
	{ 
		class listRenderer extends JLabel implements ListCellRenderer
		{
			private String _title;
			listRenderer(String title)
			{
				_title = title;
			}
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				 if (index == -1 && value == null)
					 {
					 setOpaque(true);
					 setText(_title);
					 }
		         else
		        	 {
		        	 setOpaque(false);
		        	 setText(value.toString());
		        	 }
				this.setBorder(BorderFactory.createEmptyBorder(0,8,0,0));
		        return this;
			} 
		}
		
		setPreferredSize(new Dimension (255,216));
		setVisible(true);
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		list.setRenderer(new listRenderer("Chọn loại khuyến mãi"));
        list.setSelectedIndex(-1);
		list.setPreferredSize(new Dimension(187, 40));
		
		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		
		c1.insets = new Insets(0, 34, 0, 34);
		points.setPreferredSize(new Dimension(187,40));
		points.setHorizontalAlignment(JLabel.CENTER);
		points.setFont(new Font("Tahoma", Font.PLAIN, 15));
		points.setBackground(Color.LIGHT_GRAY);
		points.setOpaque(true);
		add(points, c1);
		
		c2.gridx = 0;
		c2.gridy = 1;
		c2.insets = new Insets(48, 34, 0, 34);
		list.setFont(new Font("Tahoma", Font.PLAIN, 15));
		list.setBackground(Color.LIGHT_GRAY);
		list.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		add(list, c2);
	}
}
