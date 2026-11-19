
package giaodien;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

import javax.swing.*;
import java.awt.*;

public class TrangChu extends JFrame {

    private CardLayout cardLayout;
    private JPanel content;
    private JLabel lblTieuDe;

    public TrangChu() {
        setTitle("PHAN MEM QUAN LY SINH VIEN");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // MENU BEN TRAI
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 700));
        sidebar.setBackground(new Color(25, 55, 90));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("  QUAN LY SINH VIEN");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 19));
        logo.setBorder(BorderFactory.createEmptyBorder(25, 10, 30, 5));
        sidebar.add(logo);

        addMenu(sidebar, "Trang chu", "home");
        addMenu(sidebar, "Quan ly sinh vien", "sinhvien");
        addMenu(sidebar, "Quan ly lop hoc", "lop");
        addMenu(sidebar, "Quan ly khoa", "khoa");
        addMenu(sidebar, "Quan ly mon hoc", "monhoc");
        addMenu(sidebar, "Quan ly diem", "diem");
        addMenu(sidebar, "Thong ke ket qua", "thongke");

        sidebar.add(Box.createVerticalGlue());
        addMenu(sidebar, "Thoat chuong trinh", "thoat");

        add(sidebar, BorderLayout.WEST);

        // KHU VUC HIEN THI
        JPanel right = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        lblTieuDe = new JLabel("TRANG CHU");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(25, 70, 120));

        JLabel user = new JLabel("Nguoi quan tri");
        user.setFont(new Font("Arial", Font.PLAIN, 14));

        top.add(lblTieuDe, BorderLayout.WEST);
        top.add(user, BorderLayout.EAST);

        right.add(top, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        content.setBackground(new Color(240, 245, 250));

        content.add(createDashboard(), "home");

        content.add(new CrudPanel(
    "QUAN LY SINH VIEN",
    new String[]{"MaSV", "HoTen", "NgaySinh",
        "GioiTinh", "DiaChi", "MaLop"}
), "sinhvien");

content.add(new CrudPanel(
    "QUAN LY LOP HOC",
    new String[]{"MaLop", "TenLop", "MaKhoa"}
), "lop");

content.add(new CrudPanel(
    "QUAN LY KHOA",
    new String[]{"MaKhoa", "TenKhoa"}
), "khoa");

content.add(new CrudPanel(
    "QUAN LY MON HOC",
    new String[]{"MaMH", "TenMH", "SoTinChi"}
), "monhoc");

   content.add(new CrudPanel(
    "QUAN LY DIEM",
    new String[]{"MaSV", "MaMH", "Diem"}
), "diem");

        content.add(createStatistics(), "thongke");

        right.add(content, BorderLayout.CENTER);
        add(right, BorderLayout.CENTER);

        cardLayout.show(content, "home");
    }

    // TAO NUT MENU
    private void addMenu(JPanel sidebar, String text, String page) {

        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 48));
        btn.setPreferredSize(new Dimension(230, 48));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(25, 55, 90));
        btn.setFont(new Font("Arial", Font.PLAIN, 15));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (page.equals("thoat")) {
                int result = JOptionPane.showConfirmDialog(
                        this, "Ban co muon thoat chuong trinh?",
                        "Xac nhan", JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else {
                cardLayout.show(content, page);
                lblTieuDe.setText(text.toUpperCase());
            }
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(45, 105, 160));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(25, 55, 90));
            }
        });

        sidebar.add(btn);
    }

    // TRANG CHU
    private JPanel createDashboard() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel welcome = new JLabel("CHAO MUNG DEN VOI HE THONG");
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcome.setForeground(new Color(25, 70, 120));

        JLabel description = new JLabel(
                "PHAN MEM QUAN LY SINH VIEN"
        );
        description.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(new Color(240, 245, 250));
        heading.add(welcome);
        heading.add(description);

        panel.add(heading, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 20, 20));
        cards.setBackground(new Color(240, 245, 250));

        cards.add(createCard("SINH VIEN", "Quan ly thong tin sinh vien"));
        cards.add(createCard("LOP HOC", "Quan ly danh sach lop"));
        cards.add(createCard("KHOA", "Quan ly cac khoa"));
        cards.add(createCard("MON HOC", "Quan ly mon hoc"));
        cards.add(createCard("DIEM", "Quan ly ket qua hoc tap"));
        cards.add(createCard("THONG KE", "Thong ke ket qua"));

        panel.add(cards, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCard(String title, String detail) {

        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230)),
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel name = new JLabel(title, SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.BOLD, 20));
        name.setForeground(new Color(30, 100, 160));

        JLabel info = new JLabel(detail, SwingConstants.CENTER);
        info.setFont(new Font("Arial", Font.PLAIN, 13));

        card.add(name);
        card.add(info);

        return card;
    }

    
private JPanel createStatistics() {

    JPanel panel = new JPanel(new BorderLayout(15, 15));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.setBackground(new Color(240, 245, 250));

    // TIEU DE
    JLabel title = new JLabel(
        "THONG KE KET QUA HOC TAP",
        SwingConstants.CENTER
    );
    title.setFont(new Font("Arial", Font.BOLD, 24));
    title.setForeground(new Color(25, 70, 120));

    // CAC O THONG KE
    JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
    cards.setBackground(new Color(240, 245, 250));

    JLabel lblSV = new JLabel("0", SwingConstants.CENTER);
    JLabel lblLop = new JLabel("0", SwingConstants.CENTER);
    JLabel lblKhoa = new JLabel("0", SwingConstants.CENTER);
    JLabel lblMon = new JLabel("0", SwingConstants.CENTER);

    cards.add(createStatCard("TONG SINH VIEN", lblSV));
    cards.add(createStatCard("TONG LOP", lblLop));
    cards.add(createStatCard("TONG KHOA", lblKhoa));
    cards.add(createStatCard("TONG MON HOC", lblMon));

    JPanel top = new JPanel(new BorderLayout(10, 15));
    top.setBackground(new Color(240, 245, 250));
    top.add(title, BorderLayout.NORTH);
    top.add(cards, BorderLayout.CENTER);

    // BANG KET QUA
    String[] columns = {
        "MaSV", "HoTen", "Diem trung binh", "Xep loai"
    };

    DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JTable table = new JTable(model);
    table.setRowHeight(30);

    JLabel lblBang = new JLabel("BANG THONG KE KET QUA SINH VIEN");
    lblBang.setFont(new Font("Arial", Font.BOLD, 17));

    JPanel center = new JPanel(new BorderLayout(10, 10));
    center.setBackground(Color.WHITE);
    center.add(lblBang, BorderLayout.NORTH);
    center.add(new JScrollPane(table), BorderLayout.CENTER);

    // NUT CAP NHAT
    JButton btnCapNhat = new JButton("CAP NHAT THONG KE");
    btnCapNhat.setFont(new Font("Arial", Font.BOLD, 14));
    btnCapNhat.setBackground(new Color(30, 100, 160));
    btnCapNhat.setForeground(Color.WHITE);

    btnCapNhat.addActionListener(e -> {
        capNhatThongKe(lblSV, lblLop, lblKhoa, lblMon, model);
    });

    JPanel bottom = new JPanel();
    bottom.setBackground(new Color(240, 245, 250));
    bottom.add(btnCapNhat);

    panel.add(top, BorderLayout.NORTH);
    panel.add(center, BorderLayout.CENTER);
    panel.add(bottom, BorderLayout.SOUTH);

    capNhatThongKe(lblSV, lblLop, lblKhoa, lblMon, model);

    return panel;
}

// TAO O THONG KE
private JPanel createStatCard(String name, JLabel value) {

    JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createLineBorder(
        new Color(200, 215, 230)
    ));

    JLabel title = new JLabel(name, SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 14));
    title.setForeground(new Color(30, 90, 150));

    value.setFont(new Font("Arial", Font.BOLD, 28));
    value.setForeground(new Color(30, 130, 80));

    card.add(title);
    card.add(value);

    return card;
}

// CAP NHAT DU LIEU THONG KE
private void capNhatThongKe(
    JLabel lblSV, JLabel lblLop,
    JLabel lblKhoa, JLabel lblMon,
    DefaultTableModel model) {

    try (Connection conn = DBConnection.getConnection()) {

        lblSV.setText(String.valueOf(demSoLuong(conn, "SINHVIEN")));
        lblLop.setText(String.valueOf(demSoLuong(conn, "LOP")));
        lblKhoa.setText(String.valueOf(demSoLuong(conn, "KHOA")));
        lblMon.setText(String.valueOf(demSoLuong(conn, "MONHOC")));

        model.setRowCount(0);

        String sql =
            "SELECT sv.MaSV, sv.HoTen, AVG(d.Diem) AS DiemTB "
            + "FROM SINHVIEN sv "
            + "LEFT JOIN DIEM d ON sv.MaSV = d.MaSV "
            + "GROUP BY sv.MaSV, sv.HoTen "
            + "ORDER BY sv.MaSV";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String maSV = rs.getString("MaSV");
                String hoTen = rs.getString("HoTen");

                double diem = rs.getDouble("DiemTB");
                boolean chuaCoDiem = rs.wasNull();

                String diemTB;
                String xepLoai;

                if (chuaCoDiem) {
                    diemTB = "Chua co diem";
                    xepLoai = "Chua xep loai";
                } else {

                    diemTB = String.format("%.2f", diem);

                    if (diem >= 8) {
                        xepLoai = "Gioi";
                    } else if (diem >= 6.5) {
                        xepLoai = "Kha";
                    } else if (diem >= 5) {
                        xepLoai = "Trung binh";
                    } else {
                        xepLoai = "Yeu";
                    }
                }

                model.addRow(new Object[]{
                    maSV, hoTen, diemTB, xepLoai
                });
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Loi thong ke: " + e.getMessage());
    }
}

// DEM SO LUONG
private int demSoLuong(Connection conn, String tableName)
        throws SQLException {

    String sql = "SELECT COUNT(*) FROM " + tableName;

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }
    }

    return 0;
}


    // CHAY CHUONG TRINH
    public static void main(String[] args) {

        DBConnection.testConnection();

        SwingUtilities.invokeLater(() -> {
            new TrangChu().setVisible(true);
        });
    }
}
