package GUI.Panel;

import BUS.InvoiceBUS;
import BUS.PhieuNhapBUS;
import BUS.ProductBUS;
import BUS.PromotionBUS;
import DTO.HoaDonNhap;
import DTO.Promotion;
import DTO.SalesInvoice;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class DashFinance extends JPanel {
    private List<Promotion> promotions;
    private List<SalesInvoice> salesInvoices;
    private List<HoaDonNhap> comInvoices;
    private List<Rectangle> rectangles;
    private List<Integer> values;
    private BigDecimal bigSales; /*= bdCon(salesInvoices.get(0).getTotalPayment());*/
    private BigDecimal bigCom; /*= bdCon(comInvoices.get(0).getTongTien());*/
    private BigDecimal bigPro;
    // lấy theo ngày tháng năm



    public void getBiggestSalesInvoice() {
        for(SalesInvoice si : salesInvoices) {
            BigDecimal tmp = bdCon(si.getTotalPayment());
            if(tmp.compareTo(bigSales) > 0) {
                bigSales = tmp;
            }
        }
    }

    public void getBiggestComInvoice() {
        for(HoaDonNhap ci : comInvoices) {
            BigDecimal tmp = bdCon(ci.getTongTien());
            if(tmp.compareTo(bigCom) > 0) {
                bigCom = tmp;
            }
        }
    }

    public DashFinance(InvoiceBUS salesInvoiceBUS, PhieuNhapBUS comInvoiceBUS, PromotionBUS promotionBUS) {
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        int height = dm.getHeight();
        int width = dm.getWidth();
        setPreferredSize(new Dimension(width - 250,height - 100));
        this.comInvoices = comInvoiceBUS.getAllPhieuNhap();
//        this.salesInvoices = salesInvoiceBUS.fetchSalesInvoice();
        this.promotions = promotionBUS.getAllPromotions();
        this.setOpaque(true);
        this.setBackground(Color.GREEN);
        getBiggestSalesInvoice();
    }


    private void generateSalesBars() {
        rectangles = new ArrayList<>();
        values = new ArrayList<>();
        Dimension size = getPreferredSize();
        int width = size.width;
        int height = size.height;
        int gap = width / 25;
        for (int i = 0; i < 12; ++i) {
            int value = (int) (Math.random() * 450);
            int x = (2 * i + 1) * gap;
            int y = height - value - 50;
            rectangles.add(new Rectangle(x, y, gap, value));
            values.add(value);
        }
    }

    private void generateComBars() {

    }

    public BigDecimal bdCon(double value) {
        MathContext mc = new MathContext(4, RoundingMode.HALF_UP); // 4 digits precision
        BigDecimal bd = new BigDecimal(value, mc);
        return bd;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLUE);
        for (Rectangle rect : rectangles) {
            g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }
}
