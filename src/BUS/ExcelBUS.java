package BUS;

import DAO.BrandDAO;
import DAO.EmployeeDAO;
import DAO.PhieuNhapDAO;
import DAO.ProductDAO;
import DAO.SupplierDAO;
import DTO.Brand;
import DTO.Employee;
import DTO.HoaDonNhap;
import DTO.Product;
import DTO.Supplier;
import DTO.Brand;

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

     public void ExcelHoaDonNhap(String filePath) throws IOException{
        PhieuNhapDAO phieuNhapDAO=new PhieuNhapDAO();
        List<HoaDonNhap> danhsach =phieuNhapDAO.getAll();


          Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách sản phẩm");
     }

    public void ExcelListSuppliers(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> dsSuppliers = supplierDAO.getAllSuppliers();

        // 2. Tạo workbook + sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách nhà cung cấp");

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
            "Mã nhà cung cấp",
            "Tên nhà cung cấp",
            "Địa chỉ",
            "Số điện thoại",
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
        for (Supplier ncc : dsSuppliers) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(ncc.getId());
            row.createCell(1).setCellValue(ncc.getName());
            row.createCell(2).setCellValue(ncc.getAddress());
            row.createCell(3).setCellValue(ncc.getPhoneNumber());
            row.createCell(4).setCellValue(ncc.getEmail());
        }

        // 6. Auto-size cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 7. Ghi file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    public void ExcelListBrand(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        BrandDAO brandDAO = new BrandDAO();
        List<Brand> dsBrands = brandDAO.getAllBrands();

        // 2. Tạo workbook + sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách thương hiệu");

        // 3. Tạo style cho header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 4. Header
        String[] headers = {
            "Mã thương hiệu",
            "Tên thương hiệu",
            "Mã danh mục",
            "Trạng thái"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 5. Ghi dữ liệu
        int rowIdx = 1;
        for (Brand brand : dsBrands) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(brand.getMaThuongHieu());
            row.createCell(1).setCellValue(brand.getTenThuongHieu());
            row.createCell(2).setCellValue(brand.getmaDanhMuc());
            row.createCell(3).setCellValue(brand.isTrangThai() ? "Hoạt động" : "Không hoạt động");
        }

        // 6. Auto-size
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 7. Ghi ra file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
