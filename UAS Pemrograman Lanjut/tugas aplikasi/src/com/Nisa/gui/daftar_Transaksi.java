package com.Nisa.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class daftar_Transaksi extends JFrame {
    private JTable tableTransaksi;
    private JPanel panelMain;
    private JLabel no;
    private JLabel nama;
    private JLabel total;
    private JLabel waktu;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:mysql://localhost:3306/manage_menu";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public daftar_Transaksi() {
        // Inisialisasi komponen
        setTitle("Daftar Transaksi");
        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Buat model tabel dengan nama kolom
        tableModel = new DefaultTableModel(new String[]{"No", "Nama Customer", "Total", "Waktu Transaksi"}, 0);
        tableTransaksi.setModel(tableModel);

        // Muat data dari database
        loadTransaksi();

        tableTransaksi.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = tableTransaksi.getSelectedRow();
                if (selectedRow >= 0) {
                    String noText = tableModel.getValueAt(selectedRow, 0).toString();
                    String namaText = tableModel.getValueAt(selectedRow, 1).toString();
                    String totalText = tableModel.getValueAt(selectedRow, 2).toString();
                    String waktuText = tableModel.getValueAt(selectedRow, 3).toString();
                    no.setText(noText);
                    nama.setText(namaText);
                    total.setText(totalText);
                    waktu.setText(waktuText);
                }
            }
        });

        setVisible(true);
    }

    public static void insertTransaction(String customer, String menu, double totalHarga, String tanggal) {
        {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {
                String query = String.format(
                        "INSERT INTO transaksi (nama_customer, harga_total, waktu_transaksi) VALUES ('%s', %.2f, '%s')",
                        customer, totalHarga, tanggal
                );
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTransaksi() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM transaksi")) {

            int no = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (resultSet.next()) {
                String namaCustomer = resultSet.getString("nama_customer");
                String hargaTotal = resultSet.getString("harga_total");
                String waktuTransaksi = resultSet.getString("waktu_transaksi");
                tableModel.addRow(new Object[]{no++, namaCustomer, hargaTotal, waktuTransaksi});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Set look and feel ke look and feel sistem
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Buat dan tampilkan form
        SwingUtilities.invokeLater(() -> new daftar_Transaksi());
    }
}

