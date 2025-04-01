package hoadonban;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class TableSanPham extends JPanel {
	String[] column = {"Sản phẩm","Số lượng", "Giá tiền"};
	Object[][] data = {};
	JTable table = new JTable(data, column);
	JScrollPane scrollPane = new JScrollPane(table);
	JLabel appliedDiscount = new JLabel();
	JLabel totalDiscount = new JLabel();
	JLabel totalSum = new JLabel();
	
	public TableSanPham()
	{ 
		setPreferredSize(new Dimension(502, 356));
		setLayout(new GridBagLayout());
		setBackground(Color.LIGHT_GRAY);
		
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints c1 = new GridBagConstraints();
		

		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(0,11, 0,11);
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(270);	
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(135);	
		table.getTableHeader().setReorderingAllowed(false);

		scrollPane.setPreferredSize(new Dimension(476, 255));
		add(scrollPane, c);
		
		appliedDiscount.setPreferredSize(new Dimension(157, 67));
		appliedDiscount.setBackground(Color.WHITE);
		appliedDiscount.setOpaque(true);
		
		totalDiscount.setPreferredSize(new Dimension(157, 67));
		totalDiscount.setBackground(Color.WHITE);
		totalDiscount.setOpaque(true);
		
		totalSum.setPreferredSize(new Dimension(135, 67));
		totalSum.setBackground(Color.WHITE);
		totalSum.setOpaque(true);
		
		c1.insets = new Insets(11, 11, 0, 0);
		c1.gridy = 1; 
		c1.gridwidth = 1;
		c1.gridheight = 1;
		add(appliedDiscount, c1);
		
		c1.gridx = 1;
		c1.gridy = 1; 
		c1.gridwidth = 1;
		c1.gridheight = 1;	
		c1.insets = new Insets(11, 16, 0, 0);
		add(totalDiscount, c1);
		
		c1.gridx = 2;
		c1.gridy = 1; 
		c1.gridwidth = 1;
		c1.gridheight = 1;
		c1.insets = new Insets(11, 9, 0, 11);
		add(totalSum, c1);
	}
}
