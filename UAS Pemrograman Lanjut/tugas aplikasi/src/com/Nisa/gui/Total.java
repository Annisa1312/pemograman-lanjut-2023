package com.Nisa.gui;
import javax.swing.*;
import java.awt.*;

class Total extends JFrame {
    private JPanel panelMain;
    private JLabel lblTotal;

    public Total(double totalHarga) {
        panelMain = new JPanel();
        lblTotal = new JLabel("Total Harga: " + totalHarga);

        panelMain.add(lblTotal);

        this.setContentPane(panelMain);

        this.setMinimumSize(new Dimension(300, 200));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Total(0.0);
    }
}
