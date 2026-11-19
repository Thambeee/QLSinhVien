
package giaodien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CrudPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField[] txt;
    private String title;

    public CrudPanel(String title, String[] columns) {

        this.title = title;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 90, 150));
        add(lblTitle, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(Color.WHITE);

        JPanel input = new JPanel(new GridLayout(0, 4, 8, 8));
        input.setBorder(BorderFactory.createTitledBorder("Thông tin"));

        txt = new JTextField[columns.length];

        for (int i = 0; i < columns.length; i++) {
            txt[i] = new JTextField();

            input.add(new JLabel(columns[i] + ":"));
            input.add(txt[i]);
        }

        main.add(input, BorderLayout.NORTH);

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(28);

        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnMoi = new JButton("Làm mới");

        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnMoi);

        main.add(buttons, BorderLayout.SOUTH);
        add(main, BorderLayout.CENTER);

        loadData();

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());

        btnMoi.addActionListener(e -> {
            clearForm();
            loadData();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();

            if (row >= 0 && !e.getValueIsAdjusting()) {
for (int i = 0; i < txt.length; i++) {

    if (getTableName().equals("SINHVIEN") && i == 2) {

        ps.setDate(i + 1,
            java.sql.Date.valueOf(txt[i].getText().trim()));

    } else if (getTableName().equals("MONHOC") && i == 2) {

        ps.setInt(i + 1,
            Integer.parseInt(txt[i].getText().trim()));

    } else if (getTableName().equals("DIEM") && i == 2) {

        ps.setDouble(i + 1,
            Double.parseDouble(txt[i].getText().trim()));

    } else {

        ps.setString(i + 1, txt[i].getText().trim());
    }
}
            }
        });
    }

    private String getTableName() {
        if (title.contains("SINH VIEN")) return "SINHVIEN";
        if (title.contains("LOP HOC")) return "LOP";
        if (title.contains("KHOA")) return "KHOA";
        if (title.contains("MON HOC")) return "MONHOC";
        if (title.contains("DIEM")) return "DIEM";

        return "";
    }

private String getPrimaryKey() {
    String tableName = getTableName();

    switch (tableName) {
        case "SINHVIEN": return "MaSV";
        case "LOP": return "MaLop";
        case "KHOA": return "MaKhoa";
        case "MONHOC": return "MaMH";
        default: return "";
    }
}

    private void loadData() {

        model.setRowCount(0);

        String sql = "SELECT * FROM " + getTableName();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[txt.length];

                for (int i = 0; i < txt.length; i++) {
                    row[i] = rs.getObject(i + 1);
                }

                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    
private void them() {

    try {
        String tableName = getTableName();
        String sql = "";

        switch (tableName) {

            case "SINHVIEN":
                sql = "INSERT INTO SINHVIEN "
                    + "(MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
                break;

            case "LOP":
                sql = "INSERT INTO LOP (MaLop, TenLop, MaKhoa) "
                    + "VALUES (?, ?, ?)";
                break;

            case "KHOA":
                sql = "INSERT INTO KHOA (MaKhoa, TenKhoa) "
                    + "VALUES (?, ?)";
                break;

            case "MONHOC":
                sql = "INSERT INTO MONHOC (MaMH, TenMH, SoTinChi) "
                    + "VALUES (?, ?, ?)";
                break;

            case "DIEM":
                sql = "INSERT INTO DIEM (MaSV, MaMH, Diem) "
                    + "VALUES (?, ?, ?)";
                break;
        }

        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không xác định được bảng dữ liệu!");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < txt.length; i++) {
                ps.setString(i + 1, txt[i].getText().trim());
            }

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Thêm thành công!");

            clearForm();
            loadData();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Lỗi thêm: " + e.getMessage());
    }
}


private void sua() {

    int row = table.getSelectedRow();

    if (row < 0) {
        JOptionPane.showMessageDialog(this,
            "Hãy chọn dòng cần sửa!");
        return;
    }

    try {
        String tableName = getTableName();
        String sql = "";

        if (tableName.equals("DIEM")) {

            sql = "UPDATE DIEM SET Diem=? "
                + "WHERE MaSV=? AND MaMH=?";

        } else {

            sql = "UPDATE " + tableName + " SET ";

            for (int i = 1; i < txt.length; i++) {
                sql += model.getColumnName(i) + "=?";

                if (i < txt.length - 1) {
                    sql += ",";
                }
            }

            sql += " WHERE " + getPrimaryKey() + "=?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (tableName.equals("DIEM")) {

                ps.setString(1, txt[2].getText().trim());
                ps.setString(2, txt[0].getText().trim());
                ps.setString(3, txt[1].getText().trim());

            } else {

                for (int i = 1; i < txt.length; i++) {
                    ps.setString(i, txt[i].getText().trim());
                }

                ps.setString(txt.length,
                    txt[0].getText().trim());
            }

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Sửa thành công!");

            clearForm();
            loadData();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Lỗi sửa: " + e.getMessage());
    }
}
    
private void xoa() {

    int row = table.getSelectedRow();

    if (row < 0) {
        JOptionPane.showMessageDialog(this,
            "Hãy chọn dòng cần xóa!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Bạn có chắc chắn muốn xóa?",
        "Xác nhận",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    try {
        String tableName = getTableName();
        String sql = "";

        if (tableName.equals("DIEM")) {
            sql = "DELETE FROM DIEM WHERE MaSV=? AND MaMH=?";
        } else {
            sql = "DELETE FROM " + tableName
                + " WHERE " + getPrimaryKey() + "=?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (tableName.equals("DIEM")) {
                ps.setString(1, txt[0].getText().trim());
                ps.setString(2, txt[1].getText().trim());
            } else {
                ps.setString(1, txt[0].getText().trim());
            }

            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                    "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy dữ liệu cần xóa!");
            }

            clearForm();
            loadData();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Lỗi xóa: " + e.getMessage());
    }
}
private void clearForm() {
    for (JTextField field : txt) {
        field.setText("");
    }

    table.clearSelection();
}
}