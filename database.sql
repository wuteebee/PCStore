CREATE DATABASE PCStore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE PCStore;

CREATE TABLE IF NOT EXISTS ChucNang (
    idChucNang VARCHAR(20) PRIMARY KEY,
    tenChucNang VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS NhomQuyen (
    idNhomQuyen VARCHAR(20) PRIMARY KEY,
    tenNhomQuyen VARCHAR(50) NOT NULL
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
    FOREIGN KEY (idDanhMucCha) REFERENCES DanhMuc(idDanhMuc) -- Khóa ngoại tham chiếu chính nó
);

CREATE TABLE IF NOT EXISTS ThuongHieu (
    idThuongHieu VARCHAR(20) PRIMARY KEY,
    tenThuongHieu VARCHAR(50) NOT NULL,
    idDanhMuc VARCHAR(20),
    CONSTRAINT fk_thuonghieu_danhmuc FOREIGN KEY (idDanhMuc) REFERENCES DanhMuc(idDanhMuc)
);


-- Create NhaCungCap table
CREATE TABLE IF NOT EXISTS NhaCungCap (
    idNhaCungCap VARCHAR(20) PRIMARY KEY,
    tenNhaCungCap VARCHAR(255) NOT NULL,
    diaChi VARCHAR(500),
    soDienThoai VARCHAR(20),
    email VARCHAR(255)
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
    idPhanLoai INT,                        -- Liên kết với PhanLoaiSP
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
    Luong DECIMAL(10,2)

);

-- Create TaiKhoan table
CREATE TABLE IF NOT EXISTS TaiKhoan (
    idTaiKhoan VARCHAR(20) PRIMARY KEY,
    idNhanVien VARCHAR(20) NOT NULL,
    anhDaiDien BLOB,
    idNhomQuyen VARCHAR(20) NOT NULL,
    matKhau VARCHAR(50) NOT NULL,
    FOREIGN KEY (idNhomQuyen) REFERENCES NhomQuyen(idNhomQuyen),
    FOREIGN KEY (idNhanVien) REFERENCES NhanVien(idNhanVien)
);

-- Create KhachHang table
CREATE TABLE IF NOT EXISTS KhachHang (
    idKhachHang VARCHAR(20) PRIMARY KEY,
    tenKhachHang VARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(20) NOT NULL
);

-- Create HoaDonXuat table
CREATE TABLE IF NOT EXISTS HoaDonXuat (
    idHoaDonXuat VARCHAR(20) PRIMARY KEY,
    idNhanVien VARCHAR(20) NOT NULL,
    idKhachHang VARCHAR(20) NOT NULL,
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


-- Chèn cấu hình cho các sản phẩm PC với idLinhKien trực tiếp
INSERT INTO CauHinhPC (idSanPham, idThongTin, idLinhKien, STTPL)
VALUES
    ('PC001', 'CPU', 'SP001', 0),  -- Intel Core i9-11900K
    ('PC001', 'RAM', 'SP011', 0),  -- Corsair Vengeance 32GB
    ('PC001', 'GPU', 'SP006', 0),  -- NVIDIA GeForce RTX 3080
    ('PC002', 'CPU', 'SP003', 1),  -- Intel Xeon E5-1650 v4
    ('PC002', 'RAM', 'SP012', 1),  -- Samsung 64GB DDR4
    ('PC002', 'GPU', 'SP007', 1),  -- NVIDIA Quadro RTX 4000
    ('PC003', 'CPU', 'SP005', 0),  -- Intel Core i5-11400
    ('PC003', 'RAM', 'SP013', 0),  -- Kingston HyperX Fury 16GB
    ('PC003', 'GPU', 'SP008', 0),  -- Integrated Intel UHD
    ('PC004', 'CPU', 'SP004', 0),  -- AMD Ryzen 7 5800X
    ('PC004', 'RAM', 'SP011', 0),  -- Corsair Vengeance 16GB
    ('PC004', 'GPU', 'SP009', 0),  -- NVIDIA GeForce RTX 3070
    ('PC005', 'CPU', 'SP005', 0),  -- AMD Ryzen 9 5950X
    ('PC005', 'RAM', 'SP014', 0),  -- G.Skill Ripjaws 64GB
    ('PC005', 'GPU', 'SP010', 0);  -- NVIDIA GeForce RTX 3090


