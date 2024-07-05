import database.ReservationDatabase;
import gui.GUI;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {
        ReservationDatabase db = new ReservationDatabase();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        new GUI(db, lock); 
    }
}