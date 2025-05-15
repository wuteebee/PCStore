package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.Supplier;
import config.DatabaseConnection;
//import config.H2DatabaseConnection;

public class SupplierDAO {
    private Connection conn;

    //public SupplierDAO() {
        //conn = H2DatabaseConnection.getConnection();
    //}

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Supplier supplier = new Supplier(
                    rs.getString("idNhaCungCap"),
                    rs.getString("tenNhaCungCap"),
                    rs.getString("soDienThoai"),
                    rs.getString("email"),
                    rs.getString("diaChi"),
                    rs.getInt("trangThai") == 1
                );
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    public Supplier getSupplierById(String id) {
        String sql = "SELECT * FROM NhaCungCap WHERE idNhaCungCap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Supplier(
                        rs.getString("idNhaCungCap"),
                        rs.getString("tenNhaCungCap"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getInt("trangThai") == 1
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertSupplier(Supplier supplier) {
        String sql = "INSERT INTO NhaCungCap (idNhaCungCap, tenNhaCungCap, soDienThoai, email, diaChi, trangThai) VALUES (?, ?, ?, ?, ?, 1)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.getId());
            ps.setString(2, supplier.getName());
            ps.setString(3, supplier.getPhoneNumber());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE NhaCungCap SET tenNhaCungCap = ?, soDienThoai = ?, email = ?, diaChi = ?, trangThai = ? WHERE idNhaCungCap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getPhoneNumber());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getAddress());
            ps.setInt(5, supplier.getTrangThai() ? 1 : 0);
            ps.setString(6, supplier.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSupplier(String id) {
        String sql = "UPDATE NhaCungCap SET trangThai = 0 WHERE idNhaCungCap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNextSupplierId() {
        String sql = "SELECT idNhaCungCap FROM NhaCungCap WHERE idNhaCungCap REGEXP '^NCC[0-9]+$' ORDER BY LENGTH(idNhaCungCap) DESC, idNhaCungCap DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("idNhaCungCap"); // Ví dụ: NCC005
                int number = Integer.parseInt(lastId.replace("NCC", ""));
                return String.format("NCC%03d", number + 1);  // => NCC006
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NCC001"; // Trường hợp chưa có dữ liệu
    }
    
}
