package GUI.Panel;

import BUS.InvoiceBUS;
import BUS.PhieuNhapBUS;
import BUS.ProductBUS;
import BUS.PromotionBUS;
import DAO.ProductDAO;
import DTO.ComboProduct;
import DTO.HoaDonNhap;
import DTO.Promotion;
import DTO.SalesInvoice;
import DAO.InvoiceDAO;
import DAO.PromotionDAO;
import DAO.PhieuNhapDAO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.mysql.cj.protocol.a.MysqlTextValueDecoder.getTime;

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
    private List<Promotion> promos;
    private List<ComboProduct> compro;
    private Map<String, Double> promoMap;
    private Map<String, BigDecimal> comMap;
    private Map<String, BigDecimal> salesMap;
    private Map<String, BigDecimal> promoValues;


    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JComboBox<String> periodComboBox;
    private JPanel controlPanel;

    private BigDecimal max;

    public DashFinance() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1670, 890));

        sales.clear();
        coms.clear();
        proms.clear();
        salesRects.clear();
        promoRects.clear();
        comRects.clear();
        promoMap = new HashMap<>();
        comMap = new HashMap<>();
        promoValues = new HashMap<>();
        salesMap = new HashMap<>();

        initializePanel();
        setupComponents();
        getData();
        generateBars();
    }

    private void initializePanel() {
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    private void getData() {
        max = new BigDecimal(0);
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        String period = (String) periodComboBox.getSelectedItem();

        promos = new PromotionDAO().getAllPromotions();
        for (Promotion promotion : promos) {
            promoMap.put(promotion.getIdKhuyenMai(), promotion.getGiaTri());
        }

        if (!(startDate == null || endDate == null)) {
            long daysBetween = endDate.getTime() - startDate.getTime();
            comInvoices = new PhieuNhapDAO().getByDateRange((java.sql.Date) startDate, (java.sql.Date) endDate);
            salesInvoices = new InvoiceDAO().getByDateRange((java.sql.Date) startDate, (java.sql.Date) endDate);
            if (daysBetween / (1000 * 60 * 60 * 24) <= 365) {
                Calendar calendar = Calendar.getInstance();
                for (HoaDonNhap comInvoice : comInvoices) {
                    calendar.setTime(comInvoice.getNgayTao());
                    int monthNumber = calendar.get(Calendar.MONTH) + 1;
                    String monthName = Month.of(monthNumber).toString();

                    comMap.merge(monthName, bdCon(comInvoice.getTongTien()), BigDecimal::add);
                }

                for (SalesInvoice salesInvoice : salesInvoices) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
                    String monthName = salesInvoice.getDate().format(formatter);

                    if (comMap.containsKey(monthName)) {
                        BigDecimal a = comMap.get(monthName);
                        a = a.add(bdCon(salesInvoice.getTotalPayment()));
                        BigDecimal b = new BigDecimal(0);
                        if (new PromotionDAO().isComboPromotion(salesInvoice.getDid())) {
                            compro = new PromotionDAO().getComboProducts(salesInvoice.getDid());
                            ProductDAO c = new ProductDAO();
                            BigDecimal first = new BigDecimal(0);
                            BigDecimal second = new BigDecimal(0);
                            for (ComboProduct cp : compro) {
                                first = first.add(bdCon((new ProductDAO().getProductById(cp.getIdSanPham()).getGiasp())));
                                second = second.add(first.divide(bdCon(100 - promoMap.get(salesInvoice.getDid()))));
                            }
                            b = bdCon(salesInvoice.getTotalPayment()).subtract(first).add(second);
                        } else {
                            b = promoValues.get(monthName).add(bdCon(salesInvoice.getTotalPayment() / (100 - promoMap.get(salesInvoice.getDid()))));
                        }
                        comMap.put(monthName, a);
                        promoValues.put(monthName, b);
                    } else {
                        comMap.put(monthName, bdCon(salesInvoice.getTotalPayment()));
                        promoValues.put(monthName, bdCon(salesInvoice.getTotalPayment() / (100 - promoMap.get(salesInvoice.getDid()))));
                    }
                }
            } else {
                Calendar calendar = Calendar.getInstance();
                for (HoaDonNhap comInvoice : comInvoices) {
                    calendar.setTime(comInvoice.getNgayTao());
                    int yearNumber = calendar.get(Calendar.YEAR);
                    String yearString = Integer.toString(yearNumber);
                    comMap.merge(yearString, bdCon(comInvoice.getTongTien()), BigDecimal::add);
                }

                for (SalesInvoice salesInvoice : salesInvoices) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY");
                    String yearName = salesInvoice.getDate().format(formatter);

                    if (comMap.containsKey(yearName)) {
                        BigDecimal a = comMap.get(yearName);
                        a = a.add(bdCon(salesInvoice.getTotalPayment()));
                        BigDecimal b = new BigDecimal(0);
                        if (new PromotionDAO().isComboPromotion(salesInvoice.getDid())) {
                            compro = new PromotionDAO().getComboProducts(salesInvoice.getDid());
                            ProductDAO c = new ProductDAO();
                            BigDecimal first = new BigDecimal(0);
                            BigDecimal second = new BigDecimal(0);
                            for (ComboProduct cp : compro) {
                                first = first.add(bdCon((new ProductDAO().getProductById(cp.getIdSanPham()).getGiasp())));
                                second = second.add(first.divide(bdCon(100 - promoMap.get(salesInvoice.getDid()))));
                            }
                            b = bdCon(salesInvoice.getTotalPayment()).subtract(first).add(second);
                        } else {
                            b = promoValues.get(yearName).add(bdCon(salesInvoice.getTotalPayment() / (100 - promoMap.get(salesInvoice.getDid()))));
                        }
                        comMap.put(yearName, a);
                        promoValues.put(yearName, b);
                    } else {
                        comMap.put(yearName, bdCon(salesInvoice.getTotalPayment()));
                        promoValues.put(yearName, bdCon(salesInvoice.getTotalPayment() / (100 - promoMap.get(salesInvoice.getDid()))));
                    }
                }
            }
        } else if (startDate == null && endDate == null && !period.equals("None")) {
            comMap = new PhieuNhapDAO().getByPeriod(period);
            salesInvoices = new InvoiceDAO().getByPeriod(period);
            for (SalesInvoice invoice : salesInvoices) {
                String key;
                if ("Month".equals(period)) {
                    key = invoice.getDate().getYear() + "-" + String.format("%02d", invoice.getDate().getMonthValue());
                } else {
                    key = String.valueOf(invoice.getDate().getYear());
                }
                salesMap.put(key, bdCon(invoice.getTotalPayment()));
                promoValues.put(key, bdCon(invoice.getTotalPayment() / (100 - promoMap.get(invoice.getDid()))));
            }
        }

        max = getMaxValue();
    }

    private void generateSampleData() {
        sales.clear();
        coms.clear();
        proms.clear();
        salesRects.clear();
        promoRects.clear();
        comRects.clear();
        promoMap = new HashMap<>();
        comMap = new HashMap<>();
        promoValues = new HashMap<>();
        salesMap = new HashMap<>();

        Random random = new Random();

        promos = new ArrayList<>();
        promoMap = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            String promoId = "PROMO" + (i + 1);
            double promoValue = 0.1 + random.nextDouble() * 0.2;
            promos.add(new Promotion(promoId, "Promotion " + (i + 1), promoValue, null, null, "Type" + (i + 1), 1));
            promoMap.put(promoId, promoValue);
        }

        comInvoices = new ArrayList<>();
        comMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            int year = 2023;
            int month = i;
            int day = 1 + random.nextInt(28);
            calendar.set(year, month, day);
            Date utilDate = calendar.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            HoaDonNhap comInvoice = new HoaDonNhap();
            comInvoice.setIdHoaDonNhap("COM" + (i + 1));
            comInvoice.setNgayTao(sqlDate);
            comInvoice.setTongTien(300 + random.nextInt(1000));
            comInvoices.add(comInvoice);

            String key = Month.of(month + 1).toString();
            comMap.merge(key, bdCon(comInvoice.getTongTien()), BigDecimal::add);
        }

        salesInvoices = new ArrayList<>();
        promoValues = new HashMap<>();
        salesMap = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            int year = 2023;
            int month = i;
            int day = 1 + random.nextInt(28);
            LocalDate date = LocalDate.of(year, month + 1, day);
            SalesInvoice salesInvoice = new SalesInvoice();
            salesInvoice.setId("SALE" + (i + 1));
            salesInvoice.setDate(date);
            salesInvoice.setTotalPayment(800 + random.nextInt(2000));
            salesInvoice.setDid("PROMO" + (1 + random.nextInt(3)));
            salesInvoices.add(salesInvoice);

            String key = Month.of(month + 1).toString();
            salesMap.merge(key, bdCon(salesInvoice.getTotalPayment()), BigDecimal::add);
            promoValues.merge(key, bdCon(salesInvoice.getTotalPayment() / (1 - promoMap.get(salesInvoice.getDid()))), BigDecimal::add);
        }

        for (Map.Entry<String, BigDecimal> entry : comMap.entrySet()) {
            String key = entry.getKey();
            coms.add(entry.getValue());
            if (salesMap.containsKey(key)) {
                sales.add(salesMap.get(key));
                proms.add(promoValues.get(key));
            } else {
                sales.add(new BigDecimal("0"));
                proms.add(new BigDecimal("0"));
            }
        }

        max = getMaxValue();
        printData();
    }

    private void generateBars() {
        int width = 1670;
        int height = 890;
        int widthGap = 250;
        int heightGap = 50;
        double topGap = 0.12;

        printData();
        int dataSize = Math.max(Math.max(sales.size(), coms.size()), proms.size());
        int groupWidth = (width - widthGap) / dataSize;
        int barWidth = (groupWidth - 20) / 3;

        salesRects.clear();
        promoRects.clear();
        comRects.clear();

        BigDecimal chartHeight = BigDecimal.valueOf((height - heightGap) * (1 - topGap));
        BigDecimal scaleFactor = max.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ONE
                : chartHeight.divide(max, 10, RoundingMode.HALF_UP);

        for (int i = 0; i < dataSize; i++) {
            BigDecimal salesValue = i < sales.size() ? sales.get(i) : BigDecimal.ZERO;
            BigDecimal promoValue = i < proms.size() ? proms.get(i) : BigDecimal.ZERO;
            BigDecimal comValue = i < coms.size() ? coms.get(i) : BigDecimal.ZERO;

            int salesHeight = salesValue.multiply(scaleFactor).setScale(0, RoundingMode.HALF_DOWN).intValue();
            int promoHeight = promoValue.multiply(scaleFactor).setScale(0, RoundingMode.HALF_DOWN).intValue();
            int comHeight = comValue.multiply(scaleFactor).setScale(0, RoundingMode.HALF_DOWN).intValue();

            int baseX = widthGap + i * groupWidth + 10;

            salesRects.add(new Rectangle(
                    baseX,
                    height - heightGap - salesHeight,
                    barWidth,
                    salesHeight
            ));

            promoRects.add(new Rectangle(
                    baseX,
                    height - heightGap - promoHeight,
                    barWidth,
                    promoHeight - salesHeight
            ));

            comRects.add(new Rectangle(
                    baseX + barWidth + 5,
                    height - heightGap - comHeight,
                    barWidth,
                    comHeight
            ));
        }
    }

    private BigDecimal getMaxValue() {
        BigDecimal max = BigDecimal.ZERO;
        for (BigDecimal val : sales) max = max.max(val);
        for (BigDecimal val : proms) max = max.max(val);
        for (BigDecimal val : coms) max = max.max(val);
        return max;
    }

    public static BigDecimal bdCon(double value) {
        MathContext mc = new MathContext(4, RoundingMode.HALF_EVEN);
        return new BigDecimal(value, mc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        drawGridLines(g2D);
        drawPromoBars(g2D);
        drawSalesBars(g2D);
        drawComBars(g2D);
    }

    private void drawGridLines(Graphics2D g2D) {
        int width = 1670;
        int height = 890;
        int widthGap = 250;
        int heightGap = 50;
        double topGap = 0.10508;

        BigDecimal roundedMax = roundToNextHundred(max);
        BigDecimal step = roundedMax.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP);

        int chartAreaHeight = (int) (height * (1 - topGap) - heightGap);
        int lineGap = chartAreaHeight / 10;

        g2D.setColor(new Color(150, 150, 150));
        g2D.drawLine(widthGap, height - heightGap, width, height - heightGap);

        for (int i = 0; i <= 10; i++) {
            int yPos = height - heightGap - (i * lineGap);

            g2D.setColor(new Color(220, 220, 220));
            g2D.drawLine(widthGap, yPos, width, yPos);

            BigDecimal currentValue = step.multiply(BigDecimal.valueOf(i));
            String label = currentValue.setScale(0, RoundingMode.HALF_UP).toString();

            g2D.setColor(Color.BLACK);
            g2D.drawString(label, widthGap - 50, yPos + 5);
        }
    }

    private void drawSalesBars(Graphics2D g2D) {
        g2D.setColor(new Color(100, 149, 237));
        for (Rectangle rect : salesRects) {
            g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    private void drawPromoBars(Graphics2D g2D) {
        g2D.setColor(new Color(173, 216, 230));
        for (Rectangle rect : promoRects) {
            g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    private void drawComBars(Graphics2D g2D) {
        g2D.setColor(new Color(60, 103, 194));
        for (Rectangle rect : comRects) {
            g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    private void setupComponents() {
        this.controlPanel = controlPanel();
        add(controlPanel, BorderLayout.WEST);

        createToolTipWindow();

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

    private void createToolTipWindow() {
        if (tooltipWindow == null) {
            tooltipContent = new JPanel(new BorderLayout());
            tooltipContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tooltipContent.setBackground(Color.WHITE);
            tooltipContent.setPreferredSize(new Dimension(100, 30));

            tooltipWindow = new JWindow();
            tooltipWindow.setContentPane(tooltipContent);
            tooltipWindow.pack();
            tooltipWindow.setVisible(false);
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

    public void hideTooltip() {
        if (tooltipWindow != null) {
            tooltipWindow.setVisible(false);
        }
    }

    private BigDecimal roundToNextHundred(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return number.divide(BigDecimal.valueOf(100), 0, RoundingMode.UP)
                .multiply(BigDecimal.valueOf(100));
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

    private void createDateChoosers(JPanel panel) {
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        startDateChooser.setBounds(10, 10, 150, 35);
        endDateChooser.setBounds(10, 55, 150, 35);

        panel.add(startDateChooser);
        panel.add(endDateChooser);
    }

    private void createPeriodComboBox(JPanel panel) {
        String[] periods = {"None", "Tháng", "Năm"};
        periodComboBox = new JComboBox<>(periods);
        periodComboBox.setBounds(10, 100, 100, 35);
        panel.add(periodComboBox);
    }

    private JPanel controlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(175, 890));
        controlPanel.setLayout(new BorderLayout());

        // Panel chứa các control
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(0, 1, 10, 10));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Thêm các component filter vào đây
        createDateChoosers(filterPanel);
        createPeriodComboBox(filterPanel);
        createButtons(filterPanel);


        // Panel chú thích
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(3, 1, 10, 10));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Chú thích"));

        // Tạo các mục chú thích
        legendPanel.add(createLegendItem(new Color(70, 130, 180), "Doanh thu thực tế"));
        legendPanel.add(createLegendItem(new Color(135, 206, 250), "Doanh thu không KM"));
        legendPanel.add(createLegendItem(new Color(47, 79, 79), "Chi phí"));

        // Thêm các panel vào control panel
        controlPanel.add(filterPanel, BorderLayout.NORTH);
        controlPanel.add(legendPanel, BorderLayout.SOUTH);

        return controlPanel;
    }

    // Hàm tạo mục chú thích
    private JPanel createLegendItem(Color color, String text) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        itemPanel.setOpaque(false);

        JLabel colorLabel = new JLabel();
        colorLabel.setOpaque(true);
        colorLabel.setBackground(color);
        colorLabel.setPreferredSize(new Dimension(20, 20));

        JLabel textLabel = new JLabel(text);

        itemPanel.add(colorLabel);
        itemPanel.add(textLabel);

        return itemPanel;
    }

    private void createButtons(JPanel panel) {
        JButton confirmButton = new JButton("Xác nhận");
        JButton resetButton = new JButton("Chọn lại");

        confirmButton.addActionListener(e -> {
            generateBars();
        });

        resetButton.addActionListener(e -> {
            startDateChooser.setDate(null);
            endDateChooser.setDate(null);
            periodComboBox.setSelectedIndex(0);
        });

        confirmButton.setBounds(10, 150, 100, 35);
        resetButton.setBounds(10, 195, 100, 35);

        panel.add(confirmButton);
        panel.add(resetButton);
    }

    private void printData() {
        System.out.println("Promotions:");
        for (Promotion promotion : promos) {
            System.out.println(promotion);
        }

        System.out.println("\nCommercial Invoices:");
        for (HoaDonNhap comInvoice : comInvoices) {
            System.out.println(comInvoice);
        }

        System.out.println("\nSales Invoices:");
        for (SalesInvoice salesInvoice : salesInvoices) {
            System.out.println(salesInvoice);
        }

        System.out.println("\nPromo Map:");
        promoMap.forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("\nCommercial Map:");
        comMap.forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("\nSales Map:");
        salesMap.forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("\nPromo Values:");
        promoValues.forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("\nPurchase Amounts:");
        coms.forEach(value -> System.out.println(value));

        System.out.println("\nSales Amounts:");
        sales.forEach(value -> System.out.println(value));

        System.out.println("\nPromotion Amounts:");
        proms.forEach(value -> System.out.println(value));
    }
}