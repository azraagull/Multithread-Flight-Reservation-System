package entities;

public class Seat {
    private final String seatId;
    private boolean reserved;
    private String customerId;

    public Seat(String seatId) {
        this.seatId = seatId;
        this.reserved = false;
        this.customerId = null;
    }

    public String getSeatId() {
        return seatId;
    }

    public boolean isReserved() {
        return reserved;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setReserved(boolean reserved, String customerId) {
        this.reserved = reserved;
        this.customerId = customerId;
    }
}