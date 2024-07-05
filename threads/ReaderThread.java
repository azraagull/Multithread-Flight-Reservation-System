package threads;

import database.ReservationDatabase;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReaderThread extends Thread {
    private final ReservationDatabase db;
    private final int flightId;
    private final ReentrantReadWriteLock lock;

    public ReaderThread(ReservationDatabase db, int flightId, ReentrantReadWriteLock lock) {
        super("ReaderThread-" + flightId);  // Thread ismi belirleme
        this.db = db;
        this.flightId = flightId;
        this.lock = lock;
    }

    @Override
    public void run() { // Kullanıcının uçuşu görüntelemesini sağlar gerekli logları da tutar 
        lock.readLock().lock();
        try {
            System.out.println(getName() + " querying flight " + flightId);
            db.queryReservation(flightId);
        } finally {
            System.out.println(getName() + " finished querying flight " + flightId);
            lock.readLock().unlock();
        }
    }
}