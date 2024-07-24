package com.Nisa.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Daftar_Menu extends JFrame {
    private JTable tableMenu;
    private JTextField tfMenu;
    private JTextField tfHarga;
    private JSpinner spinnerJumlah;
    private JButton btnTotalharga;
    private JButton btnTambah;
    private JButton btnHapus;
    private JTable tablePesanan;
    private JTextField tfFilter;
    private JButton btnFilter;
    private JPanel panelMain;
    private JButton btnPesan;
    private JButton closeButton;
    private JTextField textField1;

    private DefaultTableModel tableModel;
    private DefaultTableModel tablePesananModel;
    private List<String> selectedItems;
    private double totalHarga = 0.0;

    private static final String URL = "jdbc:mysql://localhost:3306/manage_menu";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Daftar_Menu() {
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        tableModel = new DefaultTableModel(new String[]{"Menu", "Harga"}, 0);
        tableMenu.setModel(tableModel);
        tablePesananModel = new DefaultTableModel(new String[]{"Menu", "Jumlah"}, 0);
        tablePesanan.setModel(tablePesananModel);
        spinnerJumlah.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        selectedItems = new ArrayList<>();

        loadMenu();

        btnTambah.addActionListener(e -> {
            int selectedRow = tableMenu.getSelectedRow();
            if (selectedRow >= 0) {
                String menu = tableModel.getValueAt(selectedRow, 0).toString();
                int jumlah = (int) spinnerJumlah.getValue();
                tablePesananModel.addRow(new Object[]{menu, jumlah});
                selectedItems.add(menu);
            }
        });

        btnHapus.addActionListener(e -> {
            int selectedRow = tablePesanan.getSelectedRow();
            if (selectedRow >= 0) {
                tablePesananModel.removeRow(selectedRow);
                selectedItems.remove(selectedRow);
            }
        });

        btnTotalharga.addActionListener(e -> {
            totalHarga = 0.0;
            for (int i = 0; i < tablePesananModel.getRowCount(); i++) {
                String menu = tablePesananModel.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(tablePesananModel.getValueAt(i, 1).toString());
                double price = getPriceForItem(menu);
                totalHarga += price * jumlah;
            }
            JOptionPane.showMessageDialog(panelMain, "Total Harga: " + totalHarga);
        });

        btnFilter.addActionListener(e -> {
            String filter = tfFilter.getText();
            filterMenu(filter);
        });

        btnPesan.addActionListener(e -> {
            String customer = textField1.getText();
            String tanggal = java.time.LocalDate.now().toString();
            StringBuilder menuBuilder = new StringBuilder();
            for (int i = 0; i < tablePesananModel.getRowCount(); i++) {
                String menu = tablePesananModel.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(tablePesananModel.getValueAt(i, 1).toString());
                menuBuilder.append(menu).append(" (").append(jumlah).append("), ");
            }
            String menu = menuBuilder.toString();
            daftar_Transaksi.insertTransaction(customer, menu, totalHarga, tanggal);
            new TotalOrder(totalHarga).setVisible(true);
            dispose();
        });

        tableMenu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = tableMenu.getSelectedRow();
                if (selectedRow >= 0) {
                    String menu = tableModel.getValueAt(selectedRow, 0).toString();
                    String harga = tableModel.getValueAt(selectedRow, 1).toString();
                    tfMenu.setText(menu);
                    tfHarga.setText(harga);
                }
            }
        });

        closeButton.addActionListener(e -> {
            dispose();

        });

    }

    private void loadMenu() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM menu")) {

            while (resultSet.next()) {
                String menu = resultSet.getString("menu");
                double harga = resultSet.getDouble("harga");
                tableModel.addRow(new Object[]{menu, harga});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getPriceForItem(String menu) {
        double price = 0.0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT harga FROM menu WHERE menu = '" + menu + "'")) {

            if (resultSet.next()) {
                price = resultSet.getDouble("harga");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    private void filterMenu(String filter) {
        tableModel.setRowCount(0);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM menu WHERE menu LIKE '%" + filter + "%'")) {

            while (resultSet.next()) {
                String menu = resultSet.getString("menu");
                double harga = resultSet.getDouble("harga");
                tableModel.addRow(new Object[]{menu, harga});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Daftar_Menu().setVisible(true));
    }
}

class TotalOrder extends JFrame {
    private JPanel panelMain;
    private JLabel lblTotal;
    private JButton btnShowTransactions;


    public TotalOrder(double totalHarga) {
        panelMain = new JPanel();
        lblTotal = new JLabel("Total Harga: " + totalHarga);
        btnShowTransactions = new JButton("Show Transactions");

        panelMain.add(lblTotal);
        panelMain.add(btnShowTransactions);

        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(300, 200));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        lblTotal.setText("Total Harga: " + totalHarga);

        btnShowTransactions.addActionListener(e -> new daftar_Transaksi().setVisible(true));
    }

    public static void main(String[] args) {

        new TotalOrder(0.0);
    }
}