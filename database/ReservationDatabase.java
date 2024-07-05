package database;

import entities.Flight;
import java.util.HashMap;
import java.util.Map;

public class ReservationDatabase {
    private final Map<Integer, Flight> flights = new HashMap<>();

    public ReservationDatabase() {
        initFlights();
    }

    private void initFlights() {
        // Örnek Uçuşlar
        flights.put(0, new Flight(0, "Flight 101", "10:00 AM"));
        flights.put(1, new Flight(1, "Flight 102", "12:00 PM"));
    }

    public Flight getFlight(int flightId) { // Uçuşları getirir
        return flights.get(flightId);
    }

    public void queryReservation(int flightId) { // Uçuş detaylarını getirir
        Flight flight = flights.get(flightId);
        if (flight != null) {
            System.out.println(flight.getDetails());
        } else {
            System.out.println("Flight not found.");
        }
    }

    public boolean makeReservation(int flightId, String seatId, String customerId) { // Koltuk rezerve eder
        Flight flight = flights.get(flightId);
        if (flight != null && flight.reserveSeat(seatId, customerId)) { // Flight sınıfındaki senkron rezerve metodunu kullanır
            return true;
        }
        return false;
    }

    public boolean cancelReservation(int flightId, String seatId, String customerId) { // Rezerve koltuğu iptal eder
        Flight flight = flights.get(flightId);
        if (flight != null && flight.cancelReservation(seatId, customerId)) { // Flight sınıfındaki senkron rezerve iptal metodunu kullanır
            return true;
        }
        return false;
    }
}
