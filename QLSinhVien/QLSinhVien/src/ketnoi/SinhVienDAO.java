
package ketnoi;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class SinhVienDAO {

    // Hiển thị danh sách sinh viên
    public static void hienThi(DefaultTableModel model) {

        model.setRowCount(0);

        String sql = "SELECT * FROM SinhVien";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int soCot = meta.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[soCot];

                for (int i = 0; i < soCot; i++) {
                    row[i] = rs.getObject(i + 1);
                }

                model.addRow(row);
            }

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(
                null, "Lỗi hiển thị: " + e.getMessage()
            );
        }
    }

    // Thêm sinh viên
    public static boolean them(String maSV, String hoTen,
            String ngaySinh, String gioiTinh,
            String diaChi, String maLop) {

        String sql = "INSERT INTO SinhVien "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ps.setString(2, hoTen);
            ps.setDate(3, Date.valueOf(ngaySinh));
            ps.setString(4, gioiTinh);
            ps.setString(5, diaChi);
            ps.setString(6, maLop);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                null, "Lỗi thêm: " + e.getMessage()
            );
            return false;
        }
    }

    // Xóa sinh viên
    public static boolean xoa(String maSV) {

        String sql = "DELETE FROM SinhVien WHERE MaSV = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(
                null, "Lỗi xóa: " + e.getMessage()
            );
            return false;
        }
    }
}
