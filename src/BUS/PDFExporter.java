package BUS;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import DAO.ProductDAO;
import DTO.HoaDonNhap;
import DTO.Product;
import DTO.ProductDetail;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class PDFExporter {

    public static void xuatHoaDon(HoaDonNhap hd) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn PDF");

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            System.out.println("Người dùng đã hủy lưu tệp.");
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) {
            filePath += ".pdf";
        }

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

   // Tải font tiếng Việt
BaseFont baseFont = BaseFont.createFont("resources/fonts/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
Font titleFont = new Font(baseFont, 16, Font.BOLD);
Font normalFont = new Font(baseFont, 12);

// Font mỏng hơn cho ghi chú
BaseFont thinBaseFont = BaseFont.createFont("resources/fonts/Roboto-Light.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
Font thinFont = new Font(thinBaseFont, 11); // nhỏ hơn một chút và mỏng hơn


            // Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN NHẬP HÀNG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15f);
            document.add(title);

            // Thông tin hóa đơn
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            document.add(new Paragraph("Mã hóa đơn: " + hd.getIdHoaDonNhap(), normalFont));
            document.add(new Paragraph("Nhân viên: " + hd.getNhanVien().getName(), normalFont));
            document.add(new Paragraph("Nhà cung cấp: " + hd.getNhaCungCap().getName(), normalFont));
            document.add(new Paragraph("Ngày lập: " + sdf.format(hd.getNgayTao()), normalFont));
            document.add(new Paragraph(" ")); // khoảng trắng

            // Bảng chi tiết sản phẩm
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{1.2f, 2.5f, 4.5f, 3.5f, 2.5f, 2.0f});

            String[] headers = {"STT", "Mã sản phẩm", "Tên sản phẩm", "Cấu hình", "Đơn giá", "Số lượng"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingTop(6f);
                cell.setPaddingBottom(6f);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            ProductDAO productDAO = new ProductDAO();
            ProductBUS productBUS = new ProductBUS();
            PhieuNhapBUS phieuNhapBUS = new PhieuNhapBUS();

            Map<String, Integer> Danhsach = phieuNhapBUS.getSoLuongTheoPhanLoai(hd.getIdHoaDonNhap());
            int stt = 1;
            for (Map.Entry<String, Integer> entry : Danhsach.entrySet()) {
                String idPhanLoai = entry.getKey();
                int soLuong = entry.getValue();

                String masp = productDAO.getProductIDbyMaPhanLoai(Integer.parseInt(idPhanLoai));
                Product product = productBUS.getProductDetail(masp);
                List<ProductDetail> listpd = productBUS.getProductDetailList(Integer.parseInt(idPhanLoai));
                int cauHinh = productDAO.getphienbanbyIdPL(Integer.parseInt(idPhanLoai));
                double donGia = phieuNhapBUS.getGiabySN(listpd.get(0).getSerialNumber());

                table.addCell(createCell(String.valueOf(stt++), normalFont));
                table.addCell(createCell(masp, normalFont));
                table.addCell(createCell(product.getTenSp(), normalFont));
                table.addCell(createCell(String.valueOf(cauHinh + 1), normalFont));
                table.addCell(createCell(String.format("%,.0f", donGia), normalFont));
                table.addCell(createCell(String.valueOf(soLuong), normalFont));
            }

            document.add(table);

            // Tổng tiền
            Paragraph tongTien = new Paragraph("Tổng tiền: " + String.format("%,.0f VND", hd.getTongTien()), normalFont);
            tongTien.setAlignment(Element.ALIGN_RIGHT);
            tongTien.setSpacingBefore(10f);
            document.add(tongTien);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

          PdfPTable tableKy = new PdfPTable(3);
tableKy.setWidthPercentage(100f);
tableKy.setSpacingBefore(30f);

// Dòng tiêu đề
String[] roles = {"Người lập phiếu", "Nhân viên nhận", "Nhà cung cấp"};
for (String role : roles) {
    PdfPCell cell = new PdfPCell(new Phrase(role, normalFont));
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tableKy.addCell(cell);
}

// Dòng (Ký và ghi rõ họ tên)
for (int i = 0; i < 3; i++) {
    PdfPCell cell = new PdfPCell(new Phrase("(Ký và ghi rõ họ tên)", thinFont));
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tableKy.addCell(cell);
}

document.add(tableKy);

            document.close();
            System.out.println("Xuất hóa đơn PDF thành công: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(5f);
        cell.setPaddingBottom(5f);
        return cell;
    }
}
