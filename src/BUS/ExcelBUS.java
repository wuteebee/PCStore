package BUS;

import DAO.EmployeeDAO;
import DAO.ProductDAO;
import DTO.Employee;
import DTO.Product;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelBUS {

    public void ExcelListEmployee(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> dsEmployee = employeeDAO.getAllEmployees();

        // 2. Tạo workbook + sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách nhân viên");

        // 3. Tạo style cho header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 4. Tạo header row
        String[] headers = {
            "Mã nhân viên",
            "Tên nhân viên",
            "Chức vụ",
            "Số điện thoại",
            "Lương",
            "Email"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 5. Ghi dữ liệu vào các row tiếp theo
        int rowIdx = 1;
        for (Employee nv : dsEmployee) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(nv.getId());
            row.createCell(1).setCellValue(nv.getName());
            row.createCell(2).setCellValue(nv.getPosition());
            row.createCell(3).setCellValue(nv.getPhoneNumber());
            row.createCell(4).setCellValue(nv.getLuong());
            row.createCell(5).setCellValue(nv.getEmail());
        }

        // 6. Auto–size tất cả các cột cho vừa khít nội dung
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
          
        }

        // 7. Ghi workbook ra file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

     public void ExcelListProduct(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        ProductDAO productDAO = new ProductDAO();
 
            List<Product> dsProducts = productDAO.getAllProducts();
      

        // 2. Tạo workbook + sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách sản phẩm");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 4. Tạo header row
        String[] headers = {
            "Mã sản phẩm",
            "Tên sản phẩm",
            "Loại sản phẩm",
            "Thương hiệu",
            "Mô tả",
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 5. Ghi dữ liệu vào các row tiếp theo
        int rowIdx = 1;
        for (Product pd : dsProducts) {
            if(pd.isTrangThai() == false) {
                continue;
            }
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(pd.getMaSp());
            row.createCell(1).setCellValue(pd.getTenSp());
            row.createCell(2).setCellValue(pd.getDanhMuc() != null ? pd.getDanhMuc().getTenDanhMuc() : "N/A");
            row.createCell(3).setCellValue(pd.getThuongHieu() != null ? pd.getThuongHieu().getTenThuongHieu() : "N/A");
            row.createCell(4).setCellValue(pd.getMoTaSanPham());
   
        }

        // 6. Auto–size tất cả các cột cho vừa khít nội dung
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
          
        }

        // 7. Ghi workbook ra file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
