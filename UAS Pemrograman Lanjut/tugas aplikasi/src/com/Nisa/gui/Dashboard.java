package com.Nisa.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Dashboard");
        setSize(355, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Back");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.pink);
        JLabel headerLabel = new JLabel("Dashboard Waitress");
        headerPanel.add(headerLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel sidebarPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        sidebarPanel.setBackground(Color.pink);
        String[] sidebarItems = {"Manage menu", "Daftar Transaksi"};
        for (String item : sidebarItems) {
            JButton button = new JButton(item);
            button.setBackground(new Color(173, 216, 230));
            button.setForeground(Color.black);
            sidebarPanel.add(button);
        }
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new CardLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel homePanel = new JPanel();
        homePanel.add(new JLabel("Manage menu"));
        contentPanel.add(homePanel, "Manage menu");

        JPanel profilePanel = new JPanel();
        profilePanel.add(new JLabel("Daftar Transaksi"));
        contentPanel.add(profilePanel, "Daftar Transaksi");

        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.addActionListener(e -> {
                    if (button.getText().equals("Manage menu")) {
                        new ManageMenu().setVisible(true);
                    } else {
                        CardLayout cl = (CardLayout) contentPanel.getLayout();
                        cl.show(contentPanel, button.getText());
                    }
                });
            }
        }

        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.addActionListener(e -> {
                    if (button.getText().equals("Daftar Transaksi")) {
                        new daftar_Transaksi().setVisible(true);
                    } else {
                        CardLayout cl = (CardLayout) contentPanel.getLayout();
                        cl.show(contentPanel, button.getText());
                    }
                });
            }
        }
    }

    public Dashboard(String waiterss) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
