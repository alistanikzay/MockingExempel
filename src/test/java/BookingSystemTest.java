import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class BookingSystemTest {

    @Mock
    private TimeProvider timeProvider;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private NotificationService notificationService;

    private BookingSystem bookingSystem;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testRoom = new Room("A101", 10);
        bookingSystem = new BookingSystem(timeProvider, roomRepository, notificationService);
    }

    @Test
    void shouldBookRoomSuccessfully() throws NotificationException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusHours(1);
        LocalDateTime end = now.plusHours(2);

        when(timeProvider.getCurrentTime()).thenReturn(now);
        when(roomRepository.findById("A101")).thenReturn(Optional.of(testRoom));

        doNothing().when(notificationService).sendBookingConfirmation(any(Booking.class));

        boolean result = bookingSystem.bookRoom("A101", start, end, "user@example.com");

        assertThat(result).isTrue();
        verify(notificationService).sendBookingConfirmation(any(Booking.class));
    }

    @Test
    void shouldNotBookRoomInThePast() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastStart = now.minusHours(2);
        LocalDateTime pastEnd = now.minusHours(1);

        when(timeProvider.now()).thenReturn(now);

        assertThatThrownBy(() ->
                bookingSystem.bookRoom("A101", pastStart, pastEnd, "user@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("start time must be in the future");
    }

    @Test
    void shouldNotBookRoomWhenEndBeforeStart() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusHours(2);
        LocalDateTime end = now.plusHours(1);

        when(timeProvider.now()).thenReturn(now);

        assertThatThrownBy(() ->
                bookingSystem.bookRoom("A101", start, end, "user@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("end time must be after start time");
    }

    @Test
    void shouldReturnFalseIfRoomNotAvailable() throws NotificationException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusHours(1);
        LocalDateTime end = now.plusHours(2);

        // Mock rätt metod i timeProvider
        when(timeProvider.getCurrentTime()).thenReturn(now);

        // Mock ett rum som returnerar false för isAvailable
        Room mockRoom = mock(Room.class);
        when(roomRepository.findById("A101")).thenReturn(Optional.of(mockRoom));
        when(mockRoom.isAvailable(start, end)).thenReturn(false);

        boolean result = bookingSystem.bookRoom("A101", start, end, "user@example.com");

        assertThat(result).isFalse();
        verify(notificationService, never()).sendBookingConfirmation(any());
    }



    @Test
    void shouldReturnFalseIfRoomIdDoesNotExist() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusHours(1);
        LocalDateTime end = now.plusHours(2);

        when(timeProvider.getCurrentTime()).thenReturn(now);
        when(roomRepository.getRoomById("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingSystem.bookRoom("INVALID", start, end, "user@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rummet existerar inte");
    }

    @Test
    void shouldStillSucceedIfNotificationFails() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusHours(1);
        LocalDateTime end = now.plusHours(2);

        when(timeProvider.now()).thenReturn(now);
        when(roomRepository.isRoomAvailable(testRoom, start, end)).thenReturn(true);
        doThrow(new RuntimeException("Notification error")).when(notificationService).notifyUser("user@example.com");

        boolean result = bookingSystem.bookRoom("A101", start, end, "user@example.com");

        assertThat(result).isTrue();  // Booking should still succeed
    }


}