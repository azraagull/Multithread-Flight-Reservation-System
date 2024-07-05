package entities;

import java.util.HashMap;
import java.util.Map;

public class Flight {
    private final int flightId;
    private final String title;
    private final String departureTime;
    private final Map<String, Seat> seats = new HashMap<>();

    public Flight(int flightId, String title, String departureTime) { // Uçuş id, adı, zamanını tutar
        this.flightId = flightId;
        this.title = title;
        this.departureTime = departureTime;
        initSeats();
    }

    private void initSeats() { // 12 koltuklu uçak örneği 
        String[] seatIds = {
                "1-A", "1-B", "1-C", "1-D",
                "2-A", "2-B", "2-C", "2-D",
                "3-A", "3-B", "3-C", "3-D"
        };
        for (String seatId : seatIds) {
            seats.put(seatId, new Seat(seatId));
        }
    }

    public synchronized boolean reserveSeat(String seatId, String customerId) { // Kullamıcı Id ile koltuk rezerve eder
        Seat seat = seats.get(seatId);
        if (seat != null && !seat.isReserved()) {
            seat.setReserved(true, customerId);
            return true;
        }
        return false;
    }

    public synchronized boolean cancelReservation(String seatId, String customerId) { // Sadece rezerve eden kullanıcının iptal etmesini sağlar
        Seat seat = seats.get(seatId);
        if (seat != null && seat.isReserved() && seat.getCustomerId().equals(customerId)) {
            seat.setReserved(false, null);
            return true;
        }
        return false;
    }

    public int getFlightId() {
        return flightId;
    }

    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }

    public synchronized void querySeats() { // Koltukların rezerve olup olmadığının bilgisini döner
        seats.forEach(
                (id, seat) -> System.out.println("Seat " + id + ": " + (seat.isReserved() ? "Reserved" : "Available")));
    }

    public String getDetails() { // Uçus detayını döner
        StringBuilder details = new StringBuilder();
        details.append("Flight ID: ").append(flightId)
                .append(", Title: ").append(title)
                .append(", Departure Time: ").append(departureTime)
                .append("\nSeats:\n");
        seats.forEach((id, seat) -> details.append("Seat ").append(id).append(": ")
                .append(seat.isReserved() ? "Reserved by " + seat.getCustomerId() : "Available").append("\n"));
        return details.toString();
    }
}
