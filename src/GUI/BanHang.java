package GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class BanHang extends JPanel {
    ThongTinDonHang thongTinDonHang = new ThongTinDonHang();
    ThongTinKhuyenMai discount = new ThongTinKhuyenMai();
    ThongTinSanPham product = new ThongTinSanPham();

    public BanHang() {
        setPreferredSize(new Dimension(1080, (int) 769.5));
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
        add(thongTinDonHang, grid);

        grid1.insets = new Insets(0, 61, 0, 0);
        grid1.gridx = 1;
        grid1.gridy = 0;
        grid1.gridheight = 1;
        grid1.gridwidth = 1;
        add(discount, grid1);

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

    class ThongTinDonHang extends JPanel {
        JTextField HoKH = new JTextField("Ho Khach Hang");
        JTextField TenKH = new JTextField("Ten Khach Hang");
        JTextField SDT = new JTextField("So dien thoai");
        JTextField SDT1 = new JTextField("So dien thoai");
        TableSanPham form = new TableSanPham();

        public ThongTinDonHang() {
            setPreferredSize(new Dimension(622, 620));
            setLayout(null);
            setVisible(true);
            setBackground(Color.WHITE);

            HoKH.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            HoKH.setPreferredSize(new Dimension(187, 40));
            HoKH.setBackground(Color.LIGHT_GRAY);
            HoKH.setBounds(60, 42, 187, 40);
            add(HoKH);

            TenKH.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            TenKH.setPreferredSize(new Dimension(293, 40));
            TenKH.setBackground(Color.LIGHT_GRAY);
            TenKH.setBounds(270, 42, 293, 40);
            add(TenKH);

            SDT.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            SDT.setPreferredSize(new Dimension(322, 40));
            SDT.setBackground(Color.LIGHT_GRAY);
            SDT.setBounds(60, 130, 322, 40);
            add(SDT);

            SDT1.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            SDT1.setPreferredSize(new Dimension(157, 40));
            SDT1.setBackground(Color.LIGHT_GRAY);
            SDT1.setBounds(405, 130, 157, 40);
            add(SDT1);

            form.setBounds(60, 216, 502, 356);
            add(form);
        }

        class TableSanPham extends JPanel {
            String[] column = {"Sản phẩm", "Số lượng", "Giá tiền"};
            Object[][] data = {{"abc", "4", "15000 dong"}};
            JTable table = new JTable(data, column);
            JScrollPane scrollPane = new JScrollPane(table);
            JLabel appliedDiscount = new JLabel();
            JLabel totalDiscount = new JLabel();
            JLabel totalSum = new JLabel();

            public TableSanPham() {
                setPreferredSize(new Dimension(502, 356));
                setLayout(new GridBagLayout());
                setBackground(Color.LIGHT_GRAY);

                GridBagConstraints c = new GridBagConstraints();
                GridBagConstraints c1 = new GridBagConstraints();


                c.gridwidth = 3;
                c.gridheight = 1;
                c.insets = new Insets(0, 11, 0, 11);
                table.setFillsViewportHeight(true);
                table.getColumnModel().getColumn(0).setPreferredWidth(270);
                table.getColumnModel().getColumn(1).setPreferredWidth(50);
                table.getColumnModel().getColumn(2).setPreferredWidth(135);
                table.getTableHeader().setReorderingAllowed(false);
                class cellRenderer extends JLabel implements TableCellRenderer {
                    cellRenderer() {
                        setBackground(Color.white);
                        setOpaque(true);
                        setBorder(BorderFactory.createEmptyBorder());
                        setHorizontalAlignment(CENTER);
                    }

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus,
                                                                   int row, int column) {
                        if (row == (table.getRowCount() - 1)) {
                            setText(value.toString());
                            setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.black));
                        } else {
                            setText(value.toString());
                            setBorder(BorderFactory.createLineBorder(Color.black, 1));
                        }
                        return this;
                    }
                }


                    class headRenderer extends JLabel implements TableCellRenderer {
                        headRenderer() {
                            setBackground(Color.WHITE);
                            setOpaque(true);
                            setBorder(BorderFactory.createLineBorder(Color.black, 1));
                            setHorizontalAlignment(CENTER);
                        }

                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                            setText(value.toString());
                            return this;
                        }
                    }

                table.setFocusable(false);
                table.setCellSelectionEnabled(false);
                table.setDefaultEditor(Object .class,null);
                table.setShowGrid(false);
                table.setIntercellSpacing(new Dimension(0,0));
                for(int i = 0;i< 3;i++)

                    {
                        table.getColumnModel().getColumn(i).setHeaderRenderer(new headRenderer());
                        table.setDefaultRenderer(table.getColumnClass(i), new cellRenderer());
                    }
                scrollPane.setPreferredSize(new Dimension(476,255));
                scrollPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
                add(scrollPane, c);

                appliedDiscount.setPreferredSize(new Dimension(157,67));
                appliedDiscount.setBackground(Color.WHITE);
                appliedDiscount.setOpaque(true);

                totalDiscount.setPreferredSize(new Dimension(157,67));
                totalDiscount.setBackground(Color.WHITE);
                totalDiscount.setOpaque(true);

                totalSum.setPreferredSize(new Dimension(135,67));
                totalSum.setBackground(Color.WHITE);
                totalSum.setOpaque(true);

                    c1.insets =new

                    Insets(11,11,0,0);

                    c1.gridy =1;
                    c1.gridwidth =1;
                    c1.gridheight =1;

                    add(appliedDiscount, c1);

                    c1.gridx =1;
                    c1.gridy =1;
                    c1.gridwidth =1;
                    c1.gridheight =1;
                    c1.insets =new

                    Insets(11,16,0,0);

                    add(totalDiscount, c1);

                    c1.gridx =2;
                    c1.gridy =1;
                    c1.gridwidth =1;
                    c1.gridheight =1;
                    c1.insets =new

                    Insets(11,9,0,11);

                    add(totalSum, c1);
            }
        }
    }

        class ThongTinKhuyenMai extends JPanel {
            JLabel points = new JLabel("Điểm tích lũy: ");
            String[] fodder = {"Combo", "Hóa đơn"};
            JComboBox list = new JComboBox(fodder);

            public ThongTinKhuyenMai() {
                class listRenderer extends JLabel implements ListCellRenderer {
                    private String _title;

                    listRenderer(String title) {
                        _title = title;
                    }

                    @Override
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                                  boolean cellHasFocus) {
                        if (index == -1 && value == null) {
                            setText(_title);
                            this.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
                        } else {
                            setText(value.toString());
                            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 8, 0, 0)));
                        }
                        return this;
                    }
                }

                setPreferredSize(new Dimension(255, 216));
                setVisible(true);
                setLayout(new GridBagLayout());
                setBackground(Color.WHITE);

                list.setRenderer(new listRenderer("Chọn loại khuyến mãi"));
                list.setSelectedIndex(-1);
                list.setPreferredSize(new Dimension(187, 40));
                GridBagConstraints c1 = new GridBagConstraints();
                GridBagConstraints c2 = new GridBagConstraints();

                c1.insets = new Insets(0, 34, 0, 34);
                points.setPreferredSize(new Dimension(187, 40));
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
                list.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                add(list, c2);
            }
        }

        class ThongTinSanPham extends JPanel {

            JLabel foo = new JLabel();
            JLabel foo1 = new JLabel();

            public ThongTinSanPham() {
                setPreferredSize(new Dimension(255, 216));
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

    class TaoHoaDonBan extends JPanel {
        JLabel image = new JLabel();
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextField field3 = new JTextField();
        JTextField field4 = new JTextField();
        JTextField field5 = new JTextField();
        Icon confirmIcon = new ImageIcon("src//resources//Tick.png");
        Icon cancelIcon = new ImageIcon("src//resources//Cross.png");
        JButton confirm = new JButton(confirmIcon);
        JButton cancel = new JButton(cancelIcon);

        public TaoHoaDonBan() throws IOException {
            setPreferredSize(new Dimension(576, 510));
            setVisible(true);
            setBackground(Color.WHITE);
            setLayout(null);

            image.setBounds(42, 42, 180, 180);
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
    }
