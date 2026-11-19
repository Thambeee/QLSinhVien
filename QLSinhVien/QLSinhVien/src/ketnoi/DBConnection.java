
package ketnoi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {

        String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=QLSinhVien;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";

        String user = "sa";

        // Thay bằng mật khẩu sa bạn đã đăng nhập SSMS thành công
        String password = "Tham123456";

        try {
            Connection conn = DriverManager.getConnection(
                    url, user, password
            );

            System.out.println("Ket noi SQL Server thanh cong!");
            return conn;

        } catch (SQLException e) {
            System.out.println("Ket noi that bai: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        getConnection();
    }
}
