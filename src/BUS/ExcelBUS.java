package BUS;

import DAO.AccountDAO;
import DAO.BrandDAO;
import DAO.CustomerDAO;
import DAO.EmployeeDAO;
import DAO.PermissionGroupDAO;
import DAO.PhieuNhapDAO;
import DAO.ProductDAO;
import DAO.PromotionDAO;
import DAO.SupplierDAO;
import DTO.Account;
import DTO.Brand;
import DTO.Customer;
import DTO.Employee;
import DTO.HoaDonNhap;
import DTO.PermissionGroup;
import DTO.Product;
import DTO.Promotion;
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

    public void ExcelListCustomer(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> dsCustomers = customerDAO.getAllCustomers();

        // 2. Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách khách hàng");

        // 3. Style header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 4. Header
        String[] headers = {
            "Mã KH", "Tên KH", "SĐT", "Email", "Ngày tham gia", "Trạng thái"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 5. Ghi dữ liệu
        int rowIdx = 1;
        for (Customer kh : dsCustomers) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(kh.getId());
            row.createCell(1).setCellValue(kh.getName());
            row.createCell(2).setCellValue(kh.getPhoneNumber());
            row.createCell(3).setCellValue(kh.getEmail());
            row.createCell(4).setCellValue(kh.getDateOfJoining().toString());
            row.createCell(5).setCellValue(kh.getTrangThai() ? "Hoạt động" : "Không hoạt động");
        }

        // 6. Auto-size cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 7. Ghi ra file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    public void ExcelListAccount(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        AccountDAO accountDAO = new AccountDAO();
        List<Account> dsAccounts = accountDAO.getAllAccounts();

        // 2. Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách tài khoản");

        // 3. Style header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 4. Header
        String[] headers = {
            "Mã tài khoản",
            "Mã nhân viên",
            "Mã nhóm quyền",
            "Tên đăng nhập",
            "Mật khẩu",
            "Trạng thái",
            "Mã OTP"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 5. Ghi dữ liệu
        int rowIdx = 1;
        for (Account acc : dsAccounts) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(acc.getIdTaiKhoan());
            row.createCell(1).setCellValue(acc.getIdNhanVien());
            row.createCell(2).setCellValue(acc.getIdNhomQuyen());
            row.createCell(3).setCellValue(acc.getTenDangNhap());
            row.createCell(4).setCellValue(acc.getMatKhau());
            row.createCell(5).setCellValue(acc.getTrangThai() == 1 ? "Hoạt động" : "Bị khóa");
            row.createCell(6).setCellValue(acc.getMaOTP());
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

    public void ExcelListPromotion(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        PromotionDAO promotionDAO = new PromotionDAO();
        List<Promotion> dsPromotions = promotionDAO.getAllPromotions();

        // 2. Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách khuyến mãi");

        // 3. Style header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 4. Header
        String[] headers = {
            "Mã KM", "Tên chương trình", "Giá trị", "Ngày bắt đầu", "Ngày kết thúc", "Loại", "Trạng thái"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 5. Ghi dữ liệu
        int rowIdx = 1;
        for (Promotion km : dsPromotions) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(km.getIdKhuyenMai());
            row.createCell(1).setCellValue(km.getTenKhuyenMai());
            row.createCell(2).setCellValue(km.getGiaTri());
            row.createCell(3).setCellValue(km.getNgayBatDau().toString());
            row.createCell(4).setCellValue(km.getNgayKetThuc().toString());
            row.createCell(5).setCellValue(km.getLoai());
            row.createCell(6).setCellValue(km.getTrangThai() == 1 ? "Hoạt động" : "Không hoạt động");
        }

        // 6. Auto-size cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 7. Ghi ra file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    public void ExcelListPermissionGroup(String filePath) throws IOException {
        // 1. Lấy dữ liệu
        PermissionGroupDAO pgDAO = new PermissionGroupDAO();
        List<PermissionGroup> dsGroups = pgDAO.getAllPermissionGroups();

        // 2. Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách nhóm quyền");

        // 3. Style header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 4. Header
        String[] headers = {
            "Mã nhóm quyền",
            "Tên nhóm quyền",
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
        for (PermissionGroup pg : dsGroups) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(pg.getIdNhomQuyen());
            row.createCell(1).setCellValue(pg.getTenNhomQuyen());
            row.createCell(2).setCellValue(pg.getTrangThai() == 1 ? "Hoạt động" : "Không hoạt động");
        }

        // 6. Auto-size cột
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
