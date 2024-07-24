package com.Nisa.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageMenu extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/manage_menu";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private JPanel panelMenu;
    private JTable tableharga;
    private JTextField textmenu;
    private JTextField textharga;
    private JButton buttontambah;
    private JButton buttonupdate;
    private JButton buttonhapus;
    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private String selectedMenu = "";

    public ManageMenu() {
        this.setContentPane(panelMenu);
        this.setMinimumSize(new Dimension(450, 460));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Back");
        JMenuItem menuItemBack = new JMenuItem("Ya");
        menu.add(menuItemBack);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        menuItemBack.addActionListener(e -> {
            int option = JOptionPane.showOptionDialog(
                    panelMenu,
                    "Ke mana kamu mau pergi?",
                    "Pilih Tujuan",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Dashboard", "Login", "Cancel"},
                    "Dashboard"
            );
                if (option == 0) {
                    new Dashboard("Waiterss").setVisible(true);
                    dispose();
                } else if (option == 1) {
                    try {
                        new MainScreen().setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    dispose();
                }

        });

        buttontambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Menu = textmenu.getText();
                double harga = Double.parseDouble(textharga.getText());

                Menu menu = new Menu();
                menu.setMenu(Menu);
                menu.setHarga(harga);

                insertMenu(menu);
                refreshTable(getMenu());
                clearForm();
            }
        });
        tableharga.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int row = tableharga.getSelectedRow();

                if (row < 0)
                    return;

                String Menu = tableharga.getValueAt(row, 0).toString();

                if (selectedMenu.equals(Menu))
                    return;

                selectedMenu = Menu;

                String harga = tableharga.getValueAt(row, 1).toString();
                System.out.println(row);
                textmenu.setText(Menu);
                textharga.setText(harga);
            }
        });


        buttonupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMenu.isEmpty()) return;

                String Menu = textmenu.getText();
                double harga = Double.parseDouble(textharga.getText());

                Menu menu = new Menu();
                menu.setMenu(Menu);
                menu.setHarga(harga);

                updateMenu(menu);
                refreshTable(getMenu());
                clearForm();
            }
        });

        buttonhapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Menu = textmenu.getText();
                hapusMenu(Menu);
                refreshTable(getMenu());
                clearForm();
            }
        });

        refreshTable(getMenu());
    }

    private void clearForm() {
        textmenu.setText("");
        textharga.setText("");
    }

    public void refreshTable(List<Menu> arrayListMenu) {
        Object[][] data = new Object[arrayListMenu.size()][2];

        for (int i = 0; i < arrayListMenu.size(); i++) {
            data[i] = new Object[]{
                    arrayListMenu.get(i).getMenu(),
                    arrayListMenu.get(i).getHarga(),
            };
        }

        defaultTableModel = new DefaultTableModel(
                data,
                new String[]{"Menu","Harga"}
        );

        tableharga.setModel(defaultTableModel);
    }

    private static void executeSql(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ResultSet executeQuery(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void insertMenu(Menu menu) {
        String sql = "INSERT INTO menu (menu, harga) VALUES (" +
                "'" + menu.getMenu() + "', " +
                menu.getHarga() + ")";
        executeSql(sql);
    }

    private static void updateMenu(Menu menu) {
        String sql = "UPDATE menu SET " +
                "harga = " + menu.getHarga() +
                " WHERE menu = '" + menu.getMenu() + "'";
        executeSql(sql);
    }

    private static void hapusMenu(String Menu) {
        String sql = "DELETE FROM menu WHERE menu = '" + Menu + "'";
        executeSql(sql);
    }

    private static List<Menu> getMenu() {
        List<Menu> arrayListMenu = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM menu");

        try {
            while (resultSet.next()) {
                String Menu = resultSet.getString("Menu");
                double harga = resultSet.getDouble("Harga");

                Menu menu = new Menu();
                menu.setMenu(Menu);
                menu.setHarga(harga);

                arrayListMenu.add(menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return arrayListMenu;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManageMenu().setVisible(true);
            }
   });
}
}