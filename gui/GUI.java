package gui;

import database.ReservationDatabase;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GUI extends JFrame {
    private final ReservationDatabase db;
    private final ReentrantReadWriteLock lock;

    public GUI(ReservationDatabase db, ReentrantReadWriteLock lock) {
        this.db = db;
        this.lock = lock;

        setTitle("Flight Reservation System");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ReservationPanel reservationPanel1 = new ReservationPanel(db, lock); // Senkron yapıyı daha iyi görüntüleyebilmek için arayüzde iki adet panel kullanılır
        ReservationPanel reservationPanel2 = new ReservationPanel(db, lock);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.add(reservationPanel1);
        mainPanel.add(reservationPanel2);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
