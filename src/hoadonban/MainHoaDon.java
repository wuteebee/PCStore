package hoadonban;

import java.awt.*;
import javax.swing.*;

public class MainHoaDon extends JPanel{
	ThongTinDonHang thongTinDonHang = new ThongTinDonHang();
	ThongTinKhuyenMai discount = new ThongTinKhuyenMai();
	ThongTinSanPham product = new ThongTinSanPham();
	
	public MainHoaDon() {
		setPreferredSize(new Dimension(1080,(int) 769.5));
		setBackground(Color.WHITE);
		setLayout(new GridBagLayout());
		
		GridBagConstraints grid = new GridBagConstraints();
		GridBagConstraints grid1 = new GridBagConstraints();
		GridBagConstraints grid2 = new GridBagConstraints();
		GridBagConstraints grid3 = new GridBagConstraints();

		grid.gridx = 0;
		grid.gridy = 0;
		grid.gridheight = 3;
		grid.gridwidth = 1;
		add(thongTinDonHang,grid);
	
		grid1.insets = new Insets(0, 61, 0, 0);
		grid1.gridx = 1;
		grid1.gridy = 0; 
		grid1.gridheight = 1;
		grid1.gridwidth = 1;
		add(discount,grid1);
		
		grid2.insets = new Insets(55, 61, 0, 0);
		grid2.gridx = 1;
		grid2.gridy = 1;
		grid2.gridheight = 1;
		grid2.gridwidth = 1;
		add(product, grid2);
		
		grid3.insets = new Insets(55, 61, 0, 0);
		grid3.gridx = 1;
		grid3.gridy = 2;
		grid3.gridheight = 1;
		grid3.gridwidth = 1;
		JButton confirm = new JButton("Xác nhận tạo đơn hàng");
		confirm.setFocusable(false);
		confirm.setFont(new Font("Tahoma", Font.PLAIN, 20));
		confirm.setBackground(Color.WHITE);
		confirm.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		confirm.setPreferredSize(new Dimension(255, 76));
		add(confirm, grid3);
	}
}
