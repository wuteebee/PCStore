package DAO;

import config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.Brand;
import DTO.Catalog;
import DTO.ThongSoKyThuat;
import config.DatabaseConnection;

public class AtributeDAO {
    private Connection conn;

    public AtributeDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<Catalog> getAllCatalogs() {
        List<Catalog> catalogs = new ArrayList<>();
        String sql = "SELECT * FROM danhmuc";
        Map<String, Catalog> catalogMap = new HashMap<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String idDanhMuc = rs.getString("idDanhMuc");
                String tenDanhMuc = rs.getString("tenDanhMuc");
                String idDanhMucCha = rs.getString("idDanhMucCha");
                boolean trangThai = rs.getBoolean("trangThai");

                Catalog danhmuc = new Catalog(idDanhMuc, tenDanhMuc, idDanhMucCha, trangThai);
                catalogs.add(danhmuc);
                catalogMap.put(idDanhMuc, danhmuc);

            }
            for (Catalog catalog : catalogs) {
                Catalog danhMucCha = catalog.getDanhMucCha();
                if (danhMucCha != null) {
                    Catalog cha = catalogMap.get(danhMucCha.getMaDanhMuc());
                    catalog.setDanhMucCha(cha);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        return catalogs;
    }

    public List<Brand> getAllBrand() {
        List<Brand> brands = new ArrayList<>();
        String sql = "SELECT * FROM thuonghieu";
        List<Catalog> catalogs = getAllCatalogs();
        Map<String, Catalog> catalogmap = new HashMap<>();
        for (Catalog tmp : catalogs) {
            catalogmap.put(tmp.getMaDanhMuc(), tmp);
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                String id = rs.getString("idThuongHieu");
                String name = rs.getString("tenThuongHieu");
                String idDanhMuc = rs.getString("idDanhMuc");
                boolean trangThai = rs.getBoolean("trangThai");
                Catalog tmp = null;
                if (idDanhMuc != null) {
                    tmp = catalogmap.get(idDanhMuc);
                }
                Brand brand = new Brand(id, name, tmp, trangThai);
                brands.add(brand);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return brands;

    }
 
    public List<ThongSoKyThuat>getAllTechnicalParameter(){
        List<ThongSoKyThuat>danhsach=new ArrayList<>();
        String sql="SELECT * FROM thongtinkythuat";
        try {
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(sql);
            while (rs.next()) {
                String idThongSo=rs.getString("thongtinkythuat");
                String idDanhMuc=rs.getString("idDanhMuc");
                String tenThongTin=rs.getString("tenThongTin");
                
                ThongSoKyThuat tmp=new ThongSoKyThuat(idThongSo,idDanhMuc,tenThongTin);
                danhsach.add(tmp);
                
            }
        } catch (Exception e) {
        
        }
        return danhsach;
    }

    
}
