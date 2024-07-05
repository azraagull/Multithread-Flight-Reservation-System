package threads;

import database.ReservationDatabase;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WriterThread extends Thread {
    private final ReservationDatabase db;
    private final int flightId;
    private final String seatId;
    private final String customerId;
    private final ReentrantReadWriteLock lock;
    private final boolean isReservation;
    private boolean success;

    public WriterThread(ReservationDatabase db, int flightId, String seatId, String customerId, ReentrantReadWriteLock lock, boolean isReservation) {
        super("WriterThread-" + flightId + "-" + seatId);
        this.db = db;
        this.flightId = flightId;
        this.seatId = seatId;
        this.customerId = customerId;
        this.lock = lock;
        this.isReservation = isReservation;
    }

    @Override
    public void run() {  // Kullanıcının rezerve ve iptal işlemlerini yapabilmesi için tasarlanmıştır. Konsolda işlemlerin düzgün bir logunun tutulması sağlanmıştır
        lock.writeLock().lock();
        try {
            if (isReservation) {
                System.out.println(getName() + " reserving seat " + seatId + " on flight " + flightId + " for customer " + customerId);
                success = db.makeReservation(flightId, seatId, customerId);
                if (success) {
                    System.out.println(getName() + " reserved seat " + seatId + " on flight " + flightId + " for customer " + customerId);
                } else {
                    System.out.println(getName() + " failed to reserve seat " + seatId + " on flight " + flightId + " for customer " + customerId);
                }
            } else {
                System.out.println(getName() + " cancelling reservation for seat " + seatId + " on flight " + flightId + " for customer " + customerId);
                success = db.cancelReservation(flightId, seatId, customerId);
                if (success) {
                    System.out.println(getName() + " cancelled reservation for seat " + seatId + " on flight " + flightId + " for customer " + customerId);
                } else {
                    System.out.println(getName() + " failed to cancel reservation for seat " + seatId + " on flight " + flightId + " for customer " + customerId);
                }
            }
        } finally {
            System.out.println(getName() + " finished " + (isReservation ? "reserving" : "cancelling reservation for") + " seat " + seatId + " on flight " + flightId + " for customer " + customerId);
            lock.writeLock().unlock();
        }
    }

    public boolean isSuccessful() {
        return success;
    }
}