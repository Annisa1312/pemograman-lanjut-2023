package com.Nisa.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainScreen extends JFrame{
    private static final String URL = "jdbc:mysql://localhost:3306/data_restoran";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private JPanel panelMain;
    private Dashboard dashboard;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnwaitress;
    private JButton btncustomer;

    public MainScreen() throws SQLException {
        // set panel
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(325,340));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        // tombol customer
        btncustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = tfUsername.getText();
                String sql = "SELECT * FROM customer";
                try{
                    Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while(resultSet.next()){
                        String dataNama = resultSet.getString("user_name");
                        String dataPassword = resultSet.getString("password");
                    }

                }catch( Exception f){
                    System.out.println("gagal connect");
                }
                Daftar_Menu daftarMenu = new Daftar_Menu();
            }
        });

        // tombol waitress
        btnwaitress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //authenticateUser("customer", "customer");
                String sql = "SELECT * FROM waitress";
                String namaKasir = tfUsername.getText();
                System.out.println("tekan tombol");
                try{
                    Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while(resultSet.next()){
                        String nama = resultSet.getString("user_name");
                        if (nama.equals(namaKasir)){
                            Dashboard dashboard1 = new Dashboard();
                        }else{
                            FormatSalah formatSalah = new FormatSalah();
                        }
                    }
                }catch (Exception f){
                    System.out.println(f);
                }
            }

        });

    }

// panggil login
    public static void main(String[] args) throws Exception {
        MainScreen mainScreen = new MainScreen();
    }

    private void authenticateUser (String table, String role) {
        String username = tfUsername.getText();
        String password = String.valueOf(pfPassword.getPassword());
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM " + table + " WHERE username=? AND password=?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + " (" + role + ")");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid " + role + " username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
 }
}
}