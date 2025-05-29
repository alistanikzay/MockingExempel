package org.example;

public interface NotificationService {
    void sendBookingConfirmation(Booking booking) throws NotificationException;
    void sendCancellationConfirmation(Booking booking) throws NotificationException;

    void notifyUser(String s);

}
