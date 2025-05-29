package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Optional<Room> findById(String id);
    List<Room> findAll();
    void save(Room room);

    Object isRoomAvailable(Room testRoom, LocalDateTime start, LocalDateTime end);

    Object getRoomById(String invalid);
    void delete(String id);

}
