package DAO;


import config.DatabaseConnection;
import DTO.DanhMuc;

import java.sql.*;
import java.util.*;

public class DanhMucDAO {

    private Connection conn = DatabaseConnection.getConnection();

    // Lấy toàn bộ danh sách ID danh mục (chỉ các danh mục đang hoạt động)
    public List<String> getAllDanhMucIds() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT idDanhMuc FROM DanhMuc WHERE trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getString("idDanhMuc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    // (Tuỳ chọn) Lấy toàn bộ danh mục nếu cần thêm thông tin (tên, danh mục cha...)
    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM DanhMuc";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DanhMuc dm = new DanhMuc();
                dm.setIdDanhMuc(rs.getString("idDanhMuc"));
                dm.setTenDanhMuc(rs.getString("tenDanhMuc"));
                dm.setIdDanhMucCha(rs.getString("idDanhMucCha"));
                dm.setTrangThai(rs.getInt("trangThai"));
                list.add(dm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
