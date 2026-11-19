
package giaodien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LopPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaLop, txtTenLop, txtMaKhoa;

    public LopPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("QUAN LY LOP HOC", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(30, 90, 150));

        add(title, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(Color.WHITE);

        JPanel input = new JPanel(new GridLayout(3, 2, 8, 8));
        input.setBorder(BorderFactory.createTitledBorder("Thong tin lop hoc"));

        txtMaLop = new JTextField();
        txtTenLop = new JTextField();
        txtMaKhoa = new JTextField();

        input.add(new JLabel("Ma lop:"));
        input.add(txtMaLop);

        input.add(new JLabel("Ten lop:"));
        input.add(txtTenLop);

        input.add(new JLabel("Ma khoa:"));
        input.add(txtMaKhoa);

        main.add(input, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Ma lop", "Ten lop", "Ma khoa"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(28);

        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnMoi = new JButton("Lam moi");

        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnMoi);

        main.add(buttons, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);

        loadData();

        btnThem.addActionListener(e -> themLop());
        btnSua.addActionListener(e -> suaLop());
        btnXoa.addActionListener(e -> xoaLop());

        btnMoi.addActionListener(e -> {
            clearForm();
            loadData();
        });

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row >= 0) {
                    txtMaLop.setText(model.getValueAt(row, 0).toString());
                    txtTenLop.setText(model.getValueAt(row, 1).toString());
                    txtMaKhoa.setText(model.getValueAt(row, 2).toString());
                }
            }
        });
    }

    private void loadData() {

        model.setRowCount(0);

        String sql = "SELECT MaLop, TenLop, MaKhoa FROM LOP";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaLop"),
                    rs.getString("TenLop"),
                    rs.getString("MaKhoa")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Loi tai du lieu: " + e.getMessage());
        }
    }

    private boolean kiemTra() {

        if (txtMaLop.getText().trim().isEmpty()
                || txtTenLop.getText().trim().isEmpty()
                || txtMaKhoa.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Vui long nhap day du thong tin!");
            return false;
        }

        return true;
    }

    private void themLop() {

        if (!kiemTra()) return;

        String sql = "INSERT INTO LOP (MaLop, TenLop, MaKhoa) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtMaLop.getText().trim());
            ps.setString(2, txtTenLop.getText().trim());
            ps.setString(3, txtMaKhoa.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Them lop thanh cong!");

            clearForm();
            loadData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Loi them lop: " + e.getMessage());
        }
    }

    private void suaLop() {

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hay chon lop can sua!");
            return;
        }

        if (!kiemTra()) return;

        String sql = "UPDATE LOP SET TenLop=?, MaKhoa=? WHERE MaLop=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtTenLop.getText().trim());
            ps.setString(2, txtMaKhoa.getText().trim());
            ps.setString(3, txtMaLop.getText().trim());

            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Sua lop thanh cong!");
            } else {
                JOptionPane.showMessageDialog(this, "Khong tim thay lop!");
            }

            clearForm();
            loadData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Loi sua lop: " + e.getMessage());
        }
    }

    private void xoaLop() {

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hay chon lop can xoa!");
            return;
        }

        String maLop = txtMaLop.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa lop " + maLop + "?",
                "Xac nhan",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM LOP WHERE MaLop=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLop);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Xoa lop thanh cong!");

            clearForm();
            loadData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Loi xoa lop: " + e.getMessage());
        }
    }

    private void clearForm() {

        txtMaLop.setText("");
        txtTenLop.setText("");
        txtMaKhoa.setText("");

        table.clearSelection();
    }
}