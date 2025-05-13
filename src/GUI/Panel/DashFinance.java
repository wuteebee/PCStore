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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DashFinance extends JPanel {

    private List<Rectangle> salesRects = new ArrayList<>();
    private List<Rectangle> promoRects = new ArrayList<>();
    private List<Rectangle> comRects = new ArrayList<>();
    private ArrayList<BigDecimal> sales = new ArrayList<>();
    private ArrayList<BigDecimal> coms = new ArrayList<>();
    private ArrayList<BigDecimal> proms = new ArrayList<>();

    private JWindow tooltipWindow;
    private JPanel tooltipContent;

    private List<SalesInvoice> salesInvoices;
    private List<HoaDonNhap> comInvoices;
    private Map<String, Double> promotions;

    private int max;

    public DashFinance(InvoiceBUS salesInvoiceBUS, PhieuNhapBUS comInvoiceBUS, PromotionBUS promotionBUS) {
        initializePanel();
        setupComponents();
        generateSampleData();
        generateBars();
        setupEventListeners();
    }

    private void initializePanel() {
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    private void setupComponents() {
        createToolTipWindow();
    }

    private void setupEventListeners() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateTooltip(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hideTooltip();
            }
        });
    }

    private void generateSampleData() {
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            double salesValue = 1000 + random.nextDouble() * 9000;
            sales.add(bdCon(salesValue));

            double promoValue = salesValue * (1.1 + random.nextDouble() * 0.2);
            proms.add(bdCon(promoValue));
        }

        for (int i = 0; i < 12; i++) {
            double comValue = 500 + random.nextDouble() * 1000;
            coms.add(bdCon(comValue));
        }

        max = Math.max(getMaxValue(proms), getMaxValue(coms));
        max = Math.max(max, getMaxValue(sales));
    }

    private void generateBars() {
        int width = 1670;
        int height = 890;
        int widthGap = 200;
        int heightGap = 50;
        double topGap = 0.12;
        int barWidth = (width - widthGap) / 36;

        for (int i = 0; i < 12; ++i) {

            int salesValue = sales.size() > i ? (int) ((sales.get(i).intValue() * height / max) * (1 - topGap)) : 0;
            int promoValue = proms.size() > i ? (int) ((proms.get(i).intValue() * height / max) * (1 - topGap)) : 0;
            int comValue = coms.size() > i ? (int) ((coms.get(i).intValue() * height / max) * (1 - topGap)) : 0;

            int baseX = widthGap + 3 * i * barWidth;
            int salesX = baseX;
            int comX = baseX + barWidth;

            int salesY = height - heightGap;
            int promoY = salesY - salesValue;
            int comY = height - heightGap;

            salesRects.add(new Rectangle(salesX, salesY - salesValue, barWidth, salesValue));
            promoRects.add(new Rectangle(salesX, promoY - (promoValue - salesValue), barWidth, promoValue - salesValue));
            comRects.add(new Rectangle(comX, comY - comValue, barWidth, comValue));


        }
    }


    private int getMaxValue(ArrayList<BigDecimal> values) {
        int max = 0;
        for (BigDecimal value : values) {
            int intValue = value.intValue();
            if (intValue > max) {
                max = intValue;
            }
        }
        return max;
    }

    private BigDecimal bdCon(double value) {
        MathContext mc = new MathContext(4, RoundingMode.HALF_EVEN);
        return new BigDecimal(value, mc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        drawGridLines(g2D);
        drawSalesBars(g2D);
        drawPromoBars(g2D);
        drawComBars(g2D);
    }

    private void drawGridLines(Graphics2D g2D) {
        double topGap = 0.12;
        int width = 1670;
        int height = 890;
        int widthGap = 75;
        int heightGap = 50;
        int lineGap = (int) (height * (1 - topGap)) / 10;
        g2D.setColor(Color.RED);
        for (int i = 1; i <= 10; i++) {
            g2D.setColor(Color.RED);
            g2D.drawLine(widthGap, height - 3 - heightGap - i * lineGap, width, height - 3 - heightGap - i * lineGap);
            int figure = roundToNextHundred(max) / 10;
            g2D.drawString(String.valueOf(figure * i), widthGap, height - 5 - heightGap - i * lineGap);
        }
    }

    private void drawSalesBars(Graphics2D g2D) {
        g2D.setColor(Color.BLACK);
        for (Rectangle rect : salesRects) {
            g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    private void drawPromoBars(Graphics2D g2D) {
        g2D.setColor(Color.GRAY);
        for (Rectangle rect : promoRects) {
            g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    private void drawComBars(Graphics2D g2D) {
        g2D.setColor(Color.BLUE);
        for (Rectangle rect : comRects) {
            g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    private void createToolTipWindow() {
        tooltipContent = new JPanel(new BorderLayout());
        tooltipContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tooltipContent.setBackground(Color.WHITE);
        tooltipContent.setPreferredSize(new Dimension(100, 30));

        tooltipWindow = new JWindow();
        tooltipWindow.setContentPane(tooltipContent);
        tooltipWindow.pack();
        tooltipWindow.setVisible(false);

        tooltipWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hideTooltip();
            }
        });
    }

    public static int roundToNextHundred(int number) {
        if (number % 100 == 0) {
            return number; // Already a multiple of 100
        } else {
            return ((number / 100) + 1) * 100;
        }
    }

    private void updateTooltip(MouseEvent e) {
        Point mousePoint = e.getPoint();
        int promoIndex = getHoverIndex(promoRects, mousePoint);
        int comIndex = getHoverIndex(comRects, mousePoint);
        int salesIndex = getHoverIndex(salesRects, mousePoint);

        if (promoIndex != -1) {
            showTooltip(proms.get(promoIndex), promoRects.get(promoIndex));
        } else if (comIndex != -1) {
            showTooltip(coms.get(comIndex), comRects.get(comIndex));
        } else if (salesIndex != -1) {
            showTooltip(sales.get(salesIndex), salesRects.get(salesIndex));
        } else {
            hideTooltip();
        }
    }

    private int getHoverIndex(List<Rectangle> rects, Point point) {
        for (int i = 0; i < rects.size(); i++) {
            if (rects.get(i).contains(point)) {
                return i;
            }
        }
        return -1;
    }

    private void showTooltip(BigDecimal value, Rectangle rect) {
        tooltipContent.removeAll();
        tooltipContent.add(new JLabel(value.setScale(0, RoundingMode.HALF_UP).toPlainString()), BorderLayout.CENTER);
        tooltipContent.revalidate();
        tooltipContent.repaint();

        Point panelLocation = getLocationOnScreen();
        int x = panelLocation.x + rect.x;
        int y = panelLocation.y + rect.y - tooltipContent.getHeight() - 10;

        tooltipWindow.setLocation(x, y);
        tooltipWindow.pack();
        tooltipWindow.setVisible(true);
    }

    private void hideTooltip() {
        tooltipWindow.setVisible(false);
    }

    @Override
    public void setSize(Dimension size) {
        super.setSize(size);
        regenerateBars();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        regenerateBars();
    }

    private void regenerateBars() {
        salesRects.clear();
        promoRects.clear();
        comRects.clear();
        generateBars();
        repaint();
    }
}