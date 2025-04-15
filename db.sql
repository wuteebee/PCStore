CREATE DATABASE PCStore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE PCStore;

CREATE TABLE IF NOT EXISTS ChucNang (
    idChucNang VARCHAR(20) PRIMARY KEY,
    tenChucNang VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS NhomQuyen (
    idNhomQuyen VARCHAR(20) PRIMARY KEY,
    tenNhomQuyen VARCHAR(50) NOT NULL,
    trangThai INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS ChiTietQuyen (
    idNhomQuyen VARCHAR(20) NOT NULL,
    idChucNang VARCHAR(20) NOT NULL,
    hanhDong VARCHAR(5) NOT NULL,
    PRIMARY KEY (idNhomQuyen, idChucNang),
    FOREIGN KEY (idNhomQuyen) REFERENCES NhomQuyen(idNhomQuyen),
    FOREIGN KEY (idChucNang) REFERENCES ChucNang(idChucNang)
);

-- Create DanhMuc table
CREATE TABLE IF NOT EXISTS DanhMuc (
    idDanhMuc VARCHAR(20) PRIMARY KEY,
    tenDanhMuc VARCHAR(50) NOT NULL,
    idDanhMucCha VARCHAR(20),  -- Thêm kiểu dữ liệu cho idDanhMucCha
        trangThai INT DEFAULT 1,
    FOREIGN KEY (idDanhMucCha) REFERENCES DanhMuc(idDanhMuc) -- Khóa ngoại tham chiếu chính nó
);

CREATE TABLE IF NOT EXISTS ThuongHieu (
    idThuongHieu VARCHAR(20) PRIMARY KEY,
    tenThuongHieu VARCHAR(50) NOT NULL,
    idDanhMuc VARCHAR(20),
    trangThai INT DEFAULT 1,
    CONSTRAINT fk_thuonghieu_danhmuc FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc)
);


-- Create NhaCungCap table
CREATE TABLE IF NOT EXISTS NhaCungCap (
    idNhaCungCap VARCHAR(20) PRIMARY KEY,
    tenNhaCungCap VARCHAR(255) NOT NULL,
    diaChi VARCHAR(500),
    soDienThoai VARCHAR(20),
    email VARCHAR(255),
    trangThai INT DEFAULT 1
);

-- Create SanPham table
CREATE TABLE IF NOT EXISTS SanPham (
    idSanPham VARCHAR(20) PRIMARY KEY,
    idDanhMuc VARCHAR(20) NOT NULL,
    tenSanPham VARCHAR(255) NOT NULL,
    idThuongHieu VARCHAR(20) NOT NULL,
	Gia DECIMAL(12,2) NOT NULL,
    moTaSanPham VARCHAR(1000),
    anhSanPham BLOB,
    trangThai INT DEFAULT 1,
    FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc),
    FOREIGN KEY (idThuongHieu) REFERENCES ThuongHieu(idThuongHieu)
);

-- Create ThongTinKyThuat table
CREATE TABLE IF NOT EXISTS ThongTinKyThuat (
    idDanhMuc VARCHAR(20) NOT NULL,
    idThongTin VARCHAR(20) PRIMARY KEY,
    tenThongTin VARCHAR(50) NOT NULL,
    FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc)
);


-- Create CauHinhLaptop table
-- Create CauHinhLaptop table
CREATE TABLE IF NOT EXISTS CauHinhLaptop (
    idSanPham VARCHAR(20),
    idThongTin VARCHAR(20),
    ThongTin VARCHAR(255),  -- Giả sử thông tin là chuỗi
    STTPL INT DEFAULT 0,    -- Giả sử STTPL mặc định là 0
    PRIMARY KEY (idSanPham, idThongTin,STTPL),
    FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham),
    FOREIGN KEY (idThongTin) REFERENCES ThongTinKyThuat(idThongTin)
);

-- Create CauHinhPC table
CREATE TABLE IF NOT EXISTS CauHinhPC (
    idSanPham VARCHAR(20),
    idThongTin VARCHAR(20),
    idLinhKien VARCHAR(20),
    STTPL INT DEFAULT 0,    -- Giả sử STTPL mặc định là 0
    PRIMARY KEY (idSanPham, idThongTin, idLinhKien,STTPL),
    FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham),
    FOREIGN KEY (idThongTin) REFERENCES ThongTinKyThuat(idThongTin),
    FOREIGN KEY (idLinhKien) REFERENCES SanPham(idSanPham)
);

-- Create PhanLoaiSP table (chỉnh sửa)
CREATE TABLE IF NOT EXISTS PhanLoaiSP (
    idPhanLoai INT PRIMARY KEY,  -- idPhanLoai là khóa chính
    idSanPham VARCHAR(20),
    STTPL INT DEFAULT 0,         -- Trạng thái phân loại sản phẩm
    Gia DECIMAL(12,2),           -- Giá sản phẩm
    soLuongTonKho INT DEFAULT 0, -- Số lượng tồn kho
    FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)
);

CREATE TABLE IF NOT EXISTS ChiTietSP (
    SerialNumber VARCHAR(50) PRIMARY KEY,  -- SerialNumber là khóa chính
    idPhanLoai INT,       
    giaNhap DECIMAL(12,2),         -- Giá nhập sản phẩm     
    trangThai INT DEFAULT 1   ,         -- Liên kết với PhanLoaiSP
    FOREIGN KEY (idPhanLoai) REFERENCES PhanLoaiSP(idPhanLoai) -- Khóa ngoại
);

-- Create KhuyenMai table
CREATE TABLE IF NOT EXISTS KhuyenMai (
    idKhuyenMai VARCHAR(20) PRIMARY KEY,
    giaTri DECIMAL(10,2) NOT NULL
);

-- Create KhuyenMaiCombo table
CREATE TABLE IF NOT EXISTS KhuyenMaiCombo (
    idKhuyenMai VARCHAR(20) NOT NULL,
    idSanPham VARCHAR(20) NOT NULL,
    PRIMARY KEY (idKhuyenMai, idSanPham),
    FOREIGN KEY (idKhuyenMai) REFERENCES KhuyenMai(idKhuyenMai),
    FOREIGN KEY (idSanPham) REFERENCES SanPham(idSanPham)
);

-- Create NhanVien table
CREATE TABLE IF NOT EXISTS NhanVien (
    IDNhanVien VARCHAR(20) PRIMARY KEY,
    TenNhanVien VARCHAR(50) NOT NULL,
    SDT VARCHAR(20),
    Mail VARCHAR(255),
    NgayVaoLam DATE,
    ViTri VARCHAR(50),
    Luong DECIMAL(10,2),
    trangThai INT DEFAULT 1

);

-- Create TaiKhoan table
CREATE TABLE IF NOT EXISTS TaiKhoan (
    idTaiKhoan VARCHAR(20) PRIMARY KEY,
    idNhanVien VARCHAR(20) NOT NULL,
    anhDaiDien BLOB,
    idNhomQuyen VARCHAR(20) NOT NULL,
    matKhau VARCHAR(50) NOT NULL,
    trangThai INT DEFAULT 1,
    FOREIGN KEY (idNhomQuyen) REFERENCES NhomQuyen(idNhomQuyen),
    FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien)
);

-- Create KhachHang table
CREATE TABLE IF NOT EXISTS KhachHang (
    idKhachHang INT AUTO_INCREMENT PRIMARY KEY,
    tenKhachHang VARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(20) NOT NULL,
       Mail VARCHAR(255),
           NgayThamGia DATE,
    trangThai INT DEFAULT 1
);


-- Create HoaDonXuat table
CREATE TABLE IF NOT EXISTS HoaDonXuat (
    idHoaDonXuat VARCHAR(20) PRIMARY KEY,
    idNhanVien VARCHAR(20) NOT NULL,
    idKhachHang INT NOT NULL,
    ngayTao DATE NOT NULL,
    tongTien DECIMAL(17,2) NOT NULL,
    idKhuyenMai VARCHAR(20),
    FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien),
    FOREIGN KEY (idKhachHang) REFERENCES KhachHang(idKhachHang),
    FOREIGN KEY (idKhuyenMai) REFERENCES KhuyenMai(idKhuyenMai)
);

-- Create ChiTietHoaDonXuat table
CREATE TABLE IF NOT EXISTS ChiTietHoaDonXuat (
    idChiTietHoaDonXuat VARCHAR(20) PRIMARY KEY,
    idHoaDonXuat VARCHAR(20) NOT NULL,
    SN VARCHAR(50) NOT NULL,  
	  donGia DECIMAL(12,2) NOT NULL, 
    FOREIGN KEY (idHoaDonXuat) REFERENCES HoaDonXuat(idHoaDonXuat),
    FOREIGN KEY (SN) REFERENCES ChiTietSP(SerialNumber)
);

-- Create HoaDonNhap table
CREATE TABLE IF NOT EXISTS HoaDonNhap (
    idHoaDonNhap VARCHAR(20) PRIMARY KEY,
    idNhanVien VARCHAR(20) NOT NULL,
    idNhaCungCap VARCHAR(20) NOT NULL,
    ngayTao DATE NOT NULL,
    tongTien DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien),
    FOREIGN KEY (idNhaCungCap) REFERENCES NhaCungCap(idNhaCungCap)
);

-- Create ChiTietHoaDonNhap table


CREATE TABLE IF NOT EXISTS ChiTietDonNhap (
    idDonHang INT,
    SN VARCHAR(50) NOT NULL,  
    donGia DECIMAL(12,2) NOT NULL, -- Đơn giá tại thời điểm mua
    thanhTien DECIMAL(15,2) NOT NULL, 
    PRIMARY KEY (idDonHang,SN),
    FOREIGN KEY (SN) REFERENCES ChiTietSP(SerialNumber)

);




-- Insert sample data into ThuongHieu

-- Thêm các giá trị vào bảng DanhMuc
INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES
('Laptop', 'Laptop', NULL),  -- Laptop không có danh mục cha
('DM002', 'PC', NULL),      -- PC không có danh mục cha
('DM003', 'Linh kiện', NULL), -- Linh kiện không có danh mục cha
('DM004', 'CPU', 'DM003'),  -- CPU là danh mục con của Linh kiện
('DM005', 'GPU', 'DM003'),  -- GPU là danh mục con của Linh kiện
('DM006', 'RAM', 'DM003'),  -- RAM là danh mục con của Linh kiện
('DM007', 'Storage', 'DM003'), -- Storage là danh mục con của Linh kiện
('DM008', 'ManHinh', 'DM003'), -- Màn hình là danh mục con của Linh kiện
('DM009', 'Case', 'DM003'),   -- Case là danh mục con của Linh kiện
('DM010', 'Nguồn', 'DM003');  -- Nguồn là danh mục con của Linh kiện
-- Chèn các thương hiệu vào bảng ThuongHieu
-- Chèn các thương hiệu vào bảng ThuongHieu

INSERT INTO DanhMuc (idDanhMuc, tenDanhMuc, idDanhMucCha) VALUES
('DM011', 'Mainboard', 'DM003'),
('DM012', 'Tản nhiệt', 'DM003'),
('DM013', 'SSD', 'DM003'),
('DM014', 'Card âm thanh', 'DM003'),
('DM015', 'Card mạng', 'DM003');


INSERT INTO ThuongHieu (idThuongHieu, tenThuongHieu, idDanhMuc)
VALUES
('TH001', 'Apple', NULL),
('TH002', 'Dell', NULL),
('TH003', 'HP', NULL),
('TH004', 'Lenovo', NULL),
('TH005', 'Asus', NULL),
('TH006', 'Corsair', NULL),  -- Case
('TH007', 'NZXT', NULL),    -- Case
('TH008', 'Fractal Design', 'DM009'), -- Case
('TH009', 'Cooler Master', NULL), -- Case
('TH010', 'Phanteks', 'DM009'), -- Case
('TH011','DeepCool','DM012'),
('TH012', 'Seasonic', 'DM010'), -- Nguồn
('TH013', 'EVGA', NULL),     -- Nguồn
('TH014', 'Cooler Master', 'DM010'), -- Nguồn
('TH015', 'Intel', NULL),    -- CPU
('TH016', 'AMD', 'DM004'),      -- CPU
('TH017', 'NVIDIA', 'DM005'),   -- GPU
('TH018', 'G.Skill', 'DM006'),  -- RAM
('TH019', 'Kingston', NULL), -- RAM
('TH020', 'Western Digital', NULL), -- Storage
('TH021', 'Seagate', NULL),  -- Storage
('TH023', 'Samsung', NULL),
('TH024','Acer', NULL),
('TH022', 'Crucial', 'DM007'),  -- Storage
('TH025','MSI', NULL),
('TH026','Gigabyte', NULL),
('TH027','ASRock','DM011'),
('TH028','Noctua','DM012'),
('TH029','Creative', NULL),
('TH030','BlasterX', NULL),
('TH031','FiiO', NULL),
('TH032','TP-Link', NULL),
('TH033','D-Link', NULL),
('TH034','custom','DM002');


-- Insert sample data into ThongTinKyThuat
INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES
('Laptop', 'CPU', 'Bộ vi xử lý'),
('Laptop', 'GPU', 'Card đồ họa'),
('Laptop', 'RAM', 'Bộ nhớ RAM'),
('Laptop', 'Storage', 'Bộ nhớ lưu trữ'),
('Laptop', 'ManHinh', 'Màn hình'),
('Laptop', 'TanSoQuet', 'Tần số quét màn hình'),
('Laptop', 'Pin', 'Dung lượng pin'),
('Laptop', 'CanNang', 'Trọng lượng'),
('Laptop', 'CongKetNoi', 'Cổng kết nối'),
('Laptop', 'WiFi', 'Chuẩn WiFi'),
('Laptop', 'Bluetooth', 'Phiên bản Bluetooth'),
('Laptop', 'HeDieuHanh', 'Hệ điều hành');


INSERT INTO ThongTinKyThuat (idDanhMuc, idThongTin, tenThongTin) VALUES
('DM002', 'TS001', 'CPU'),
('DM002', 'TS002', 'Mainboard'),
('DM002', 'TS003', 'RAM'),
('DM002', 'TS004', 'Ổ cứng SSD'),
('DM002', 'TS005', 'Ổ cứng HDD'),
('DM002', 'TS006', 'Card đồ họa (GPU)'),
('DM002', 'TS007', 'Nguồn (PSU)'),
('DM002', 'TS008', 'Vỏ Case'),
('DM002', 'TS009', 'Tản nhiệt CPU'),
('DM002', 'TS010', 'Hệ điều hành'),
('DM002', 'TS011', 'Mạng LAN'),
('DM002', 'TS012', 'Cổng kết nối'),
('DM002', 'TS013', 'Kích thước'),
('DM002', 'TS014', 'Trọng lượng');


-- Sản phẩm cho Laptop
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham)
VALUES
('LAP001', 'Laptop', 'Apple MacBook Pro 14-inch', 'TH001', 56799000, 'Laptop Apple MacBook Pro 14-inch với chip M1 Pro, màn hình Retina 14-inch'),
('LAP002', 'Laptop', 'Dell XPS 13', 'TH002', 35199000, 'Laptop Dell XPS 13 với màn hình InfinityEdge, vi xử lý Intel Core i7'),
('LAP003', 'Laptop', 'HP Spectre x360', 'TH003', 37199000, 'Laptop HP Spectre x360 với màn hình cảm ứng, vi xử lý Intel Core i7'),
('LAP004', 'Laptop', 'Lenovo ThinkPad X1 Carbon', 'TH004', 42599000, 'Laptop Lenovo ThinkPad X1 Carbon, màn hình 14-inch, vi xử lý Intel Core i7'),
('LAP005', 'Laptop', 'Asus ZenBook 13', 'TH005', 25199000, 'Laptop Asus ZenBook 13, vi xử lý Intel Core i5, màn hình 13-inch');



INSERT INTO CauHinhLaptop (idSanPham, idThongTin, ThongTin)
VALUES
('LAP001', 'CPU', 'Apple M1 Pro 8-core'),
('LAP001', 'RAM', '16GB RAM'),
('LAP001', 'Storage', '512GB SSD'),
('LAP001', 'ManHinh', '14-inch Retina Display'),
('LAP001', 'Pin', '17 hours battery life'),

('LAP002', 'CPU', 'Intel Core i7-1165G7'),
('LAP002', 'RAM', '8GB RAM'),
('LAP002', 'Storage', '512GB SSD'),
('LAP002', 'ManHinh', '13.4-inch InfinityEdge'),
('LAP002', 'Pin', '12 hours battery life'),

('LAP003', 'CPU', 'Intel Core i7-1165G7'),
('LAP003', 'RAM', '16GB RAM'),
('LAP003', 'Storage', '1TB SSD'),
('LAP003', 'ManHinh', '13.3-inch Full HD'),
('LAP003', 'Pin', '13 hours battery life'),

('LAP004', 'CPU', 'Intel Core i7-1165G7'),
('LAP004', 'RAM', '16GB RAM'),
('LAP004', 'Storage', '512GB SSD'),
('LAP004', 'ManHinh', '14-inch Full HD'),
('LAP004', 'Pin', '15 hours battery life'),

('LAP005', 'CPU', 'Intel Core i5-1135G7'),
('LAP005', 'RAM', '8GB RAM'),
('LAP005', 'Storage', '256GB SSD'),
('LAP005', 'ManHinh', '13.3-inch Full HD'),
('LAP005', 'Pin', '10 hours battery life');


-- Sản phẩm cho CPU
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham)
VALUES
('SP001', 'DM004', 'Intel Core i9-13900K', 'TH015', 7490000, 'Bộ vi xử lý Intel Core i9-13900K, 16 lõi, 24 luồng, xung nhịp tối đa 5.8GHz'),
('SP002', 'DM004', 'AMD Ryzen 9 7900X', 'TH016', 5890000, 'Bộ vi xử lý AMD Ryzen 9 7900X, 12 lõi, 24 luồng, xung nhịp tối đa 5.6GHz'),
('SP003', 'DM004', 'Intel Core i7-12700K', 'TH015', 4990000, 'Bộ vi xử lý Intel Core i7-12700K, 12 lõi, 20 luồng, xung nhịp tối đa 5.0GHz'),
('SP004', 'DM004', 'AMD Ryzen 7 5800X', 'TH016', 3990000, 'Bộ vi xử lý AMD Ryzen 7 5800X, 8 lõi, 16 luồng, xung nhịp tối đa 4.7GHz'),
('SP005', 'DM004', 'Intel Core i5-12600K', 'TH015', 3290000, 'Bộ vi xử lý Intel Core i5-12600K, 6 lõi, 12 luồng, xung nhịp tối đa 4.9GHz');


-- Sản phẩm cho GPU
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham)
VALUES
('SP006', 'DM005', 'NVIDIA GeForce RTX 3080', 'TH017', 6990000, 'Card đồ họa NVIDIA GeForce RTX 3080, 10GB GDDR6X'),
('SP007', 'DM005', 'AMD Radeon RX 6900 XT', 'TH016', 7990000, 'Card đồ họa AMD Radeon RX 6900 XT, 16GB GDDR6'),
('SP008', 'DM005', 'NVIDIA GeForce RTX 3070', 'TH017', 4990000, 'Card đồ họa NVIDIA GeForce RTX 3070, 8GB GDDR6'),
('SP009', 'DM005', 'AMD Radeon RX 6800 XT', 'TH016', 5990000, 'Card đồ họa AMD Radeon RX 6800 XT, 16GB GDDR6'),
('SP010', 'DM005', 'NVIDIA GeForce RTX 3060', 'TH017', 3990000, 'Card đồ họa NVIDIA GeForce RTX 3060, 12GB GDDR6');


-- Sản phẩm cho RAM
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham)
VALUES
('SP011', 'DM006', 'Corsair Vengeance LPX 16GB', 'TH006', 799000, 'RAM Corsair Vengeance LPX 16GB (2x8GB), DDR4 3200MHz'),
('SP012', 'DM006', 'G.Skill Ripjaws V 32GB', 'TH018', 1499000, 'RAM G.Skill Ripjaws V 32GB (2x16GB), DDR4 3600MHz'),
('SP013', 'DM006', 'Kingston HyperX Fury 16GB', 'TH019', 699000, 'RAM Kingston HyperX Fury 16GB (2x8GB), DDR4 3200MHz'),
('SP014', 'DM006', 'Corsair Dominator Platinum 32GB', 'TH006', 1999000, 'RAM Corsair Dominator Platinum 32GB (2x16GB), DDR4 3200MHz'),
('SP015', 'DM006', 'G.Skill Trident Z RGB 16GB', 'TH018', 899000, 'RAM G.Skill Trident Z RGB 16GB (2x8GB), DDR4 3600MHz');


-- Sản phẩm cho Storage
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham)
VALUES
('SP016', 'DM007', 'Samsung 970 EVO Plus 1TB', 'TH020', 3490000, 'SSD Samsung 970 EVO Plus 1TB, NVMe M.2, tốc độ đọc 3500MB/s'),
('SP017', 'DM007', 'Western Digital Black SN850 500GB', 'TH022', 2490000, 'SSD WD Black SN850 500GB, PCIe Gen 4.0, tốc độ đọc 7000MB/s'),
('SP018', 'DM007', 'Seagate FireCuda 520 1TB', 'TH021', 2990000, 'SSD Seagate FireCuda 520 1TB, NVMe M.2, tốc độ đọc 5000MB/s'),
('SP019', 'DM007', 'Kingston KC2500 500GB', 'TH019', 1990000, 'SSD Kingston KC2500 500GB, NVMe M.2, tốc độ đọc 3500MB/s'),
('SP020', 'DM007', 'Corsair MP600 1TB', 'TH006', 3490000, 'SSD Corsair MP600 1TB, PCIe Gen 4.0, tốc độ đọc 4950MB/s');

-- Sản phẩm cho Màn hình
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) 
VALUES
('SP021', 'DM008', 'LG 27GN950-B', 'TH023', 7990000, 'Màn hình LG 27GN950-B, 27 inch, 4K UHD, 144Hz, IPS'),
('SP022', 'DM008', 'Dell Alienware AW2521H', 'TH002', 5490000, 'Màn hình Dell Alienware AW2521H, 24.5 inch, Full HD, 360Hz, IPS'),
('SP023', 'DM008', 'Samsung Odyssey G7 32', 'TH023', 6990000, 'Màn hình cong Samsung Odyssey G7 32, 2560x1440, 240Hz, QLED'),
('SP024', 'DM008', 'Asus ROG Swift PG259QN', 'TH005', 5990000, 'Màn hình Asus ROG Swift PG259QN, 24.5 inch, Full HD, 360Hz, IPS'),
('SP025', 'DM008', 'Acer Predator X27', 'TH024', 9990000, 'Màn hình Acer Predator X27, 27 inch, QHD, 165Hz, IPS');





-- Sản phẩm cho SSD
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) 
VALUES
('SP036', 'DM013', 'Samsung 990 PRO 1TB', 'TH023', 4790000, 'SSD Samsung 990 PRO 1TB, NVMe M.2, tốc độ đọc 7450MB/s'),
('SP037', 'DM013', 'Kingston NV2 1TB', 'TH019', 2190000, 'SSD Kingston NV2 1TB, NVMe M.2, tốc độ đọc 3500MB/s'),
('SP038', 'DM013', 'Western Digital SN770 1TB', 'TH020', 2990000, 'SSD WD SN770 1TB, NVMe M.2, tốc độ đọc 5150MB/s'),
('SP039', 'DM013', 'Seagate FireCuda 530 1TB', 'TH021', 5490000, 'SSD Seagate FireCuda 530 1TB, NVMe PCIe 4.0, tốc độ đọc 7300MB/s'),
('SP040', 'DM013', 'Crucial P5 Plus 500GB', 'TH022', 1890000, 'SSD Crucial P5 Plus 500GB, NVMe PCIe 4.0, tốc độ đọc 6600MB/s');

-- Sản phẩm cho Card âm thanh
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) 
VALUES
('SP041', 'DM014', 'Creative Sound Blaster AE-7', 'TH029', 4290000, 'Card âm thanh Creative Sound Blaster AE-7, PCIe, Hi-Res Audio'),
('SP042', 'DM014', 'ASUS Xonar AE', 'TH005', 1990000, 'Card âm thanh ASUS Xonar AE, 7.1, hỗ trợ Hi-Res'),
('SP043', 'DM014', 'EVGA NU Audio Pro', 'TH013', 5290000, 'Card âm thanh EVGA NU Audio Pro, DAC chất lượng cao, PCIe'),
('SP044', 'DM014', 'Sound BlasterX G6', 'TH030', 3490000, 'Card âm thanh Sound BlasterX G6, hỗ trợ gaming, DAC chất lượng cao'),
('SP045', 'DM014', 'FiiO K5 Pro', 'TH031', 3890000, 'Amp/DAC FiiO K5 Pro, hỗ trợ Hi-Res Audio, phù hợp audiophile');


-- Sản phẩm cho Card mạng
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) 
VALUES
('SP046', 'DM015', 'ASUS PCE-AX3000', 'TH005', 1290000, 'Card mạng ASUS PCE-AX3000, WiFi 6, Bluetooth 5.0, PCIe'),
('SP047', 'DM015', 'TP-Link Archer TX3000E', 'TH032', 1090000, 'Card mạng TP-Link Archer TX3000E, WiFi 6, Bluetooth 5.0, PCIe'),
('SP048', 'DM015', 'Intel AX200', 'TH015', 990000, 'Card mạng Intel AX200, WiFi 6, Bluetooth 5.1, M.2 module'),
('SP049', 'DM015', 'Gigabyte GC-WBAX200', 'TH026', 1190000, 'Card mạng Gigabyte GC-WBAX200, WiFi 6, Bluetooth 5.0, PCIe'),
('SP050', 'DM015', 'D-Link DGE-560T', 'TH033', 790000, 'Card mạng D-Link DGE-560T, Gigabit Ethernet PCIe, hỗ trợ VLAN');

-- Sản phẩm cho Case
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) 
VALUES
('SP026', 'DM009', 'NZXT H510', 'TH009', 1800000, 'Case NZXT H510, Mid Tower, hỗ trợ ATX, thiết kế tối giản'),
('SP027', 'DM009', 'Corsair 4000D Airflow', 'TH010', 2300000, 'Case Corsair 4000D Airflow, Mid Tower, thiết kế tối ưu luồng khí'),
('SP028', 'DM009', 'Fractal Design Meshify C', 'TH011', 2500000, 'Case Fractal Design Meshify C, Mid Tower, thiết kế lưới, hỗ trợ ATX'),
('SP029', 'DM009', 'Cooler Master MasterBox Q300L', 'TH012', 1200000, 'Case Cooler Master MasterBox Q300L, MicroATX, hỗ trợ các kích thước bo mạch nhỏ'),
('SP030', 'DM009', 'Phanteks Eclipse P400A', 'TH013', 2000000, 'Case Phanteks Eclipse P400A, Mid Tower, thiết kế thông gió tốt');

-- Sản phẩm cho Tản nhiệt
INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham) 
VALUES
('SP031', 'DM012', 'Noctua NH-D15', 'TH030', 2590000, 'Tản nhiệt khí Noctua NH-D15, hiệu năng cao, phù hợp CPU mạnh'),
('SP032', 'DM012', 'Corsair iCUE H150i ELITE', 'TH006', 3990000, 'Tản nhiệt nước Corsair iCUE H150i ELITE, 360mm, RGB'),
('SP033', 'DM012', 'NZXT Kraken X73', 'TH007', 3490000, 'Tản nhiệt nước NZXT Kraken X73, 360mm, RGB LED'),
('SP034', 'DM012', 'DeepCool AS500 Plus', 'TH011', 1290000, 'Tản nhiệt khí DeepCool AS500 Plus, 140mm, hiệu suất cao'),
('SP035', 'DM012', 'Cooler Master Hyper 212', 'TH009', 890000, 'Tản nhiệt khí Cooler Master Hyper 212, 120mm, phổ biến');

INSERT INTO SanPham (idSanPham, idDanhMuc, tenSanPham, idThuongHieu, Gia, moTaSanPham)
VALUES
    ('PC001', 'DM002', 'PC Gaming High-End', 'TH006', 25000000, 'Máy tính chơi game cao cấp với cấu hình mạnh mẽ'),
    ('PC002', 'DM002', 'PC Workstation', 'TH002', 20000000, 'Máy tính làm việc chuyên nghiệp, ổn định'),
    ('PC003', 'DM002', 'PC Văn Phòng', 'TH003', 12000000, 'Máy tính cho công việc văn phòng, cấu hình vừa phải'),
    ('PC004', 'DM002', 'PC Mini ITX', 'TH007', 18000000, 'PC nhỏ gọn nhưng hiệu năng cao cho không gian hạn chế'),
    ('PC005', 'DM002', 'PC Custom Build', 'TH034', 30000000, 'Máy tính được lắp ráp theo yêu cầu, tối ưu hóa hiệu suất');


-- Dữ liệu CauHinhPC đã chỉnh chuẩn idThongTin từ bảng ThongTinKyThuat
INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL)
VALUES
    ('PC001', 'TS001', 'SP001', 0),  -- CPU: Intel Core i9-11900K
    ('PC001', 'TS003', 'SP011', 0),  -- RAM: Corsair Vengeance 32GB
    ('PC001', 'TS006', 'SP006', 0),  -- GPU: NVIDIA GeForce RTX 3080

    ('PC002', 'TS001', 'SP003', 1),  -- CPU: Intel Xeon E5-1650 v4
    ('PC002', 'TS003', 'SP012', 1),  -- RAM: Samsung 64GB DDR4
    ('PC002', 'TS006', 'SP007', 1),  -- GPU: NVIDIA Quadro RTX 4000

    ('PC003', 'TS001', 'SP005', 0),  -- CPU: Intel Core i5-11400
    ('PC003', 'TS003', 'SP013', 0),  -- RAM: Kingston HyperX Fury 16GB
    ('PC003', 'TS006', 'SP008', 0),  -- GPU: Integrated Intel UHD

    ('PC004', 'TS001', 'SP004', 0),  -- CPU: AMD Ryzen 7 5800X
    ('PC004', 'TS003', 'SP011', 0),  -- RAM: Corsair Vengeance 16GB
    ('PC004', 'TS006', 'SP009', 0),  -- GPU: NVIDIA GeForce RTX 3070

    ('PC005', 'TS001', 'SP005', 0),  -- CPU: AMD Ryzen 9 5950X
    ('PC005', 'TS003', 'SP014', 0),  -- RAM: G.Skill Ripjaws 64GB
    ('PC005', 'TS006', 'SP010', 0);  -- GPU: NVIDIA GeForce RTX 3090

		
		
		
		
		
		UPDATE sanpham 
SET anhSanPham = 'data:image/jpeg;base64,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'
WHERE idSanPham = 'LAP001';




INSERT INTO PhanLoaiSP (idPhanLoai, idSanPham, STTPL, Gia, soLuongTonKho) VALUES
(1, 'LAP001', 0, 56799000, 10),
(2, 'LAP001', 1, 58999000, 10),
(3, 'LAP001', 2, 60999000, 10);


UPDATE sanpham
SET anhSanPham='data:image/jpeg;base64,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mz0C3K5/LNbMc0cq5idXHqpzTAkoozSZoAWiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigDD8Ya3H4b8Ianq8p4s7Z5QPUgcD88V83/AAis2j0PUtcvCTNf3GDJ1JC8sfxYn8q9B/aX182Hga10aJv3mqXADIDyY4/mP5naKo+H9IXSPC2n6W6g+TAol95Dy36k10YeN5XObEO0bFppIZhv+1MgB6FhgH6Gpoo4lXMKIgbk+WoGfeof7OtcECBRk7uPX1q2BxXcrrc4G10GgU8CjFOxTEJilxQBTsUDEApcUoFOApAGKXFLilxQMQClxTsUuKAEGQeCR9DRIqzrtnRJV9JFDD9afilxUjRj3fhHw5fZN3oOnyE9/ICn9MVjXXwp8H3H3NPntSe9tdMP0ORXY4p2Klxi+halJbM80uvglpDZNlrF/b+gljjkH9Kx7r4IainNjrlnN6CaFoz+YzXseKXFZujB9ClWmup4HdfCXxha58u1tboD/nhdD+RxWVceD/E1j811oWooB1Kwbh/47mvpHHtSjIOQSPoazeGh0NFiJdT5XnUwNi6geFvSaIqf1FT2eqXlqQbO/nhb/phcMv8AI19QSDzV2zASL6SAN/Osu78LeHr7/j70LTpc9/s6g/mMVLwvZmixPdHjWl/EPxjaMwt/FF7EsSmTE0vmA47YPWt/Tvjt45t/9dd2l4PSe1A/9BxXV3Xws8IXGcaY9se32a4Zf0JNZFx8GdIbmx1fUbc+kgjlH8hWbw8+haxEOpo2X7RmrrgX+hWc3qYZmQ/kQa6Cz/aM0eTH2/Q76394nWX/AArzuf4PajGP9C121l9p4GjP6E1kXfwv8WQgiO3tLof9MLoDP4NiodOouhaqU31PebX46eBrgqJ9Rnsy3/PxbMP1GRW/p/xF8H6ngWfiTTZCexuAp/XFfJl34O8U2Y/f6DfhR3jj8wfmpNYtxBPAcXtrNFj/AJ7Qlf5iptJdC7xezPu2C9tbqPda3MM6+scgYfpVivge3uzC261naJvWKUqf0Nb1j468Vab/AMeXiPU4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TTEc1Dp2o6gciMoh/ik+Uf4mta18MwR4a7czN/dHC1u0UWAihhjgjCwRrGvooxUtFFMAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAGOiuu11DA9iM1m3Hh7Tbg7vs4ib+9Edn8q1aKAOak8NXUXNjfn/dmX+oqq8GrWn+vsvNUfxQnP6da6+ilYDi11ODO2TdE392QYNWo5Uk+4wP0ro5raC4XbcQpKPR1BrKm8L6e53QLLbN/0xfA/I0WGU80jMFGScU6TwvqI/wCPXXHjHo8Ab+tOt/CT79+oanPc/wCyqhBQBSkvlB2oC7HpipY9N1K+wSv2eM934P5da6O1sLazUC3hVPfGT+dWqLCMW18OWsPNwWuG9DwPyrWjiSJAsSBFHZRipKKYBRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAJRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAf/2Q=='
WHERE idSanPham="LAP002";


INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, giaNhap)
VALUES 
('SN000001', 1, 45439200),
('SN000002', 1, 45439200),
('SN000003', 1, 45439200),
('SN000004', 1, 45439200),
('SN000005', 1, 45439200),
('SN000006', 1, 45439200),
('SN000007', 1, 45439200),
('SN000008', 1, 45439200),
('SN000009', 1, 45439200),
('SN000010', 1, 45439200);

-- Cho idPhanLoai = 2
INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, giaNhap)
VALUES 
('SN000011', 2, 47199200),
('SN000012', 2, 47199200),
('SN000013', 2, 47199200),
('SN000014', 2, 47199200),
('SN000015', 2, 47199200),
('SN000016', 2, 47199200),
('SN000017', 2, 47199200),
('SN000018', 2, 47199200),
('SN000019', 2, 47199200),
('SN000020', 2, 47199200);

-- Cho idPhanLoai = 3
INSERT INTO ChiTietSP (SerialNumber, idPhanLoai, giaNhap)
VALUES 
('SN000021', 3, 48799200),
('SN000022', 3, 48799200),
('SN000023', 3, 48799200),
('SN000024', 3, 48799200),
('SN000025', 3, 48799200),
('SN000026', 3, 48799200),
('SN000027', 3, 48799200),
('SN000028', 3, 48799200),
('SN000029', 3, 48799200),
('SN000030', 3, 48799200);
