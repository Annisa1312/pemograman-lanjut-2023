package com.nisa.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame {
    private static final String URL  = "jdbc:mysql://localhost:3306/contoh" ;
    private static final String USER = "root";
    private static final String PASSWORD  = "";
    private JPanel panelMain;
    private JTable jTabelMahasiswa;
    private JTextField textFieldNIM;
    private JTextField textFieldNAMA;
    private JTextField textFieldIPK;
    private JButton buttonAdd;
    private JButton buttonUpdate;
    private JButton buttonDelete;
    private JTextField textFieldFilter;
    private JButton buttonFilter;
    private JButton buttonClear;
    private JScrollPane scrollpane;
    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private String selectedNim = "";

    public MainScreen(){
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();


        refreshTable(
                getMahasiswa()
        );

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = textFieldNIM.getText();
                String nama = textFieldNAMA.getText();
                double ipk = Double.parseDouble(textFieldIPK.getText());

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);

                insertMahasiswa(mahasiswa);
                clearFrom();
                refreshTable(getMahasiswa());
            }
        });

        this.jTabelMahasiswa.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event){
                        int row = jTabelMahasiswa.getSelectedRow();

                        if (row < 0)
                            return;

                        String nim = jTabelMahasiswa.getValueAt(row, 0).toString();

                        if (selectedNim.equals(nim))
                            return;

                        selectedNim = nim;

                        String nama = jTabelMahasiswa.getValueAt(row, 1).toString();
                        String ipk = jTabelMahasiswa.getValueAt(row, 2).toString();

                        textFieldNIM.setText(nim);
                        textFieldNAMA.setText(nama);
                        textFieldIPK.setText(ipk);
                    }
                }
        );
        buttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = textFieldNIM.getText();
                String nama = textFieldNAMA.getText();
                double ipk = Double.parseDouble(textFieldIPK.getText());

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);

                updateMahasiswa(mahasiswa);
                clearFrom();
                refreshTable(getMahasiswa());
            }
        });
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = textFieldNIM.getText();
                deleteMahasiswa(nim);
                clearFrom();
                refreshTable(getMahasiswa());
            }
        });
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFrom();
            }
        });
        buttonFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = textFieldFilter.getText();

                refreshTable(filterMahasiswa(nama));
            }
        });
    }

    public static void main(String [] args) {
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);

    }

    private void  clearFrom() {
        textFieldNAMA.setText("");
        textFieldNIM.setText("");
        textFieldIPK.setText("");
    }
    public void refreshTable(List<Mahasiswa> arrayLishMahasiswa) {
        Object [][] data = new Object [arrayLishMahasiswa.size()][3];

        for(int i = 0; i < arrayLishMahasiswa.size(); i++) {
            data[i] = new Object[]{
                    arrayLishMahasiswa.get(i).getNim(),
                    arrayLishMahasiswa.get(i).getNama(),
                    arrayLishMahasiswa.get(i).getIpk()
                    };
        }

        defaultTableModel = new DefaultTableModel(
                data,
                new String[] {"NIM", "Nama", "Ipk"}
        );

        jTabelMahasiswa.setModel(defaultTableModel);
    }

    private static void executeSql(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            Statement statement =  connection.createStatement();

            statement.executeUpdate(sql);

        } catch (Exception e) {

        }
    }
    private static ResultSet executeQuery(String query) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            Statement statement =  connection.createStatement();

            return statement.executeQuery(query);


        } catch (Exception e) {
            return null;
        }
    }
    private static void insertMahasiswa(Mahasiswa mahasiswa) {
        String sql = "insert into mahasiswa values (" +
                "'" + mahasiswa.getNim() + "', " +
                "'" + mahasiswa.getNama() + "', " +
                "'" + mahasiswa.getIpk() + "')";

        executeSql(sql);
    }
    private static void updateMahasiswa(Mahasiswa mahasiswa) {
        String sql = "update mahasiswa set " +
                "nama = '"+ mahasiswa.getNama() +"', " +
                "ipk = '"+ mahasiswa.getIpk() +"' " +
                "Where nim = '"+ mahasiswa.getNim() +"'";

        executeSql(sql);
    }

    private static void deleteMahasiswa(String nim) {
        String sql = "delete from mahasiswa " +
                "Where nim = '"+ nim +"'";

        executeSql(sql);
    }
    private static List<Mahasiswa> getMahasiswa() {
        List<Mahasiswa> arrayListMahasiswa = new ArrayList<>();

        ResultSet resultSet = executeQuery("select * from mahasiswa");

        try {
            while (resultSet.next()) {
                String nim = resultSet.getString("nim");
                String nama = resultSet.getString("nama");
                double ipk = Double.parseDouble(resultSet.getString("ipk"));

                System.out.print(nim);
                System.out.print(nama);
                System.out.print(ipk);
                System.out.println();

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);

                arrayListMahasiswa.add(mahasiswa);

                    }
        }catch (Exception e) {
            return null;
        }

      return arrayListMahasiswa;
    }
    private static List<Mahasiswa> filterMahasiswa(String filterNama) {
        List<Mahasiswa> arrayListMahasiswa = new ArrayList<>();

        ResultSet resultSet = executeQuery("select * from mahasiswa where nama like '%" + filterNama + "%'");

        try {
            while (resultSet.next()) {
                String nim = resultSet.getString("nim");
                String nama = resultSet.getString("nama");
                double ipk = Double.parseDouble(resultSet.getString("ipk"));

                System.out.print(nim);
                System.out.print(nama);
                System.out.print(ipk);
                System.out.println();

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);

                arrayListMahasiswa.add(mahasiswa);

            }
        }catch (Exception e) {
            return null;
        }

        return arrayListMahasiswa;
    }
}



