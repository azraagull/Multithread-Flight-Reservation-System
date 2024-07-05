package gui;

import database.ReservationDatabase;
import entities.Flight;
import entities.Seat;
import threads.ReaderThread;
import threads.WriterThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReservationPanel extends JPanel {  // Rezervasyon  yapmak için bir panel arayüzü içerir
    private final ReservationDatabase db;
    private final ReentrantReadWriteLock lock;
    private final JComboBox<String> customerComboBox;
    private final JComboBox<String> flightComboBox;
    private final JPanel seatPanel;
    private JButton reserveButton;
    private JButton cancelButton;
    private String selectedCustomer;
    private int selectedFlightId;
    private String selectedSeat;
    private JButton previousSelectedButton;

    public ReservationPanel(ReservationDatabase db, ReentrantReadWriteLock lock) {
        this.db = db;
        this.lock = lock;

        customerComboBox = new JComboBox<>(new String[]{"Customer1", "Customer2", "Customer3"});
        flightComboBox = new JComboBox<>(new String[]{"Flight 101", "Flight 102"});

        seatPanel = new JPanel(new GridLayout(3, 2, 20, 20));

        reserveButton = new JButton("Reserve");
        reserveButton.setVisible(false);
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> handleSeatAction(selectedSeat, true, selectedCustomer, selectedFlightId)).start();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setVisible(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> handleSeatAction(selectedSeat, false, selectedCustomer, selectedFlightId)).start();
            }
        });

        JButton selectButton = new JButton("Select Flight");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             selectedCustomer = (String) customerComboBox.getSelectedItem();
                selectedFlightId = flightComboBox.getSelectedIndex();
                new Thread(() -> {
                    ReaderThread readerThread = new ReaderThread(db, selectedFlightId, selectedCustomer, lock);
                    readerThread.start();
                    try {
                        readerThread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    loadSeats();
                }).start();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Customer:"));
        topPanel.add(customerComboBox);
        topPanel.add(new JLabel("Flight:"));
        topPanel.add(flightComboBox);
        topPanel.add(selectButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(reserveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(seatPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadSeats() { // Arayüze koltukları ekler
        seatPanel.removeAll();
        Flight flight = db.getFlight(selectedFlightId);
        String[][] seatIds = {
            {"1-A", "1-B", "1-C", "1-D"},
            {"2-A", "2-B", "2-C", "2-D"},
            {"3-A", "3-B", "3-C", "3-D"}
        };

        for (int i = 0; i < seatIds.length; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 2, 50, 20));
            for (int j = 0; j < seatIds[i].length; j += 2) {
                JPanel seatGroup = new JPanel(new GridLayout(1, 1, 5, 5));
                for (int k = 0; k < 2; k++) {
                    if (j + k < seatIds[i].length) {
                        String seatId = seatIds[i][j + k];
                        JButton seatButton = new JButton(seatId);
                        Seat seat = flight.getSeat(seatId);
                        updateSeatButton(seatButton, seat);

                        seatButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (previousSelectedButton != null) {
                                    updateSeatButton(previousSelectedButton, flight.getSeat(previousSelectedButton.getText().split(" ")[0]));
                                }
                                selectedSeat = seat.getSeatId();
                                seatButton.setText(seat.getSeatId() + " (Selected)");
                                previousSelectedButton = seatButton;
                                if (seat.isReserved() && selectedCustomer.equals(seat.getCustomerId())) {
                                    cancelButton.setVisible(true);
                                    reserveButton.setVisible(false);
                                } else if (!seat.isReserved()) {
                                    cancelButton.setVisible(false);
                                    reserveButton.setVisible(true);
                                } else {
                                    cancelButton.setVisible(false);
                                    reserveButton.setVisible(false);
                                    JOptionPane.showMessageDialog(ReservationPanel.this, "This seat has been reserved by someone else.", "Reservation Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });

                        seatGroup.add(seatButton);
                    }
                }
                rowPanel.add(seatGroup);
            }
            seatPanel.add(rowPanel);
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private void handleSeatAction(String seatId, boolean isReservation, String customerId, int flightId) { // rezerve ve iptal işlemlerini yapar
        boolean success;
        if (isReservation) {
            WriterThread reserveThread = new WriterThread(db, flightId, seatId, customerId, lock, true);
            reserveThread.start();
            try {
                reserveThread.join();
                success = reserveThread.isSuccessful();
            } catch (InterruptedException e) {
                e.printStackTrace();
                success = false;
            }
            if (success) {
                loadSeats();
            } else {
                JOptionPane.showMessageDialog(this, "This seat has been reserved by someone else.", "Reservation Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            WriterThread cancelThread = new WriterThread(db, flightId, seatId, customerId, lock, false);
            cancelThread.start();
            try {
                cancelThread.join();
                success = cancelThread.isSuccessful();
            } catch (InterruptedException e) {
                e.printStackTrace();
                success = false;
            }
            if (success) {
                loadSeats();
            }
        }
    }

    private void updateSeatButton(JButton seatButton, Seat seat) { // Arayüzde koltukların üzerine durumlarının yazılmasını sağlar 
        if (seat.isReserved()) {
            if (selectedCustomer.equals(seat.getCustomerId())) {
                seatButton.setBackground(Color.GREEN);
                seatButton.setText(seat.getSeatId() + " (Your Reservation)");
            } else {
                seatButton.setBackground(Color.RED);
                seatButton.setText(seat.getSeatId() + " (Reserved)");
            }
        } else {
            seatButton.setBackground(Color.GRAY);
            seatButton.setText(seat.getSeatId());
        }
    }
}
