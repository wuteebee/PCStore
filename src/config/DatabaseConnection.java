package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/PCStore";
    private static final String USER = "root"; 
    private static final String PASSWORD = "507061";

    public static Connection getConnection() {
        try {
            // Đăng ký MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Kết nối với cơ sở dữ liệu và trả về đối tượng Connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // Nếu không tìm thấy driver, ném ra RuntimeException
            throw new RuntimeException("Không tìm thấy Driver JDBC!", e);
        } catch (SQLException e) {
            // Nếu kết nối thất bại, ném ra RuntimeException
            throw new RuntimeException("Lỗi kết nối đến database!", e);
        }
    }
}
