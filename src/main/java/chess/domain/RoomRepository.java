package chess.domain;

import chess.domain.room.Room;

import java.util.List;

public interface RoomRepository {

    Long save(Room room);

    void update(Room room);

    Room findByRoomId(Long roomId);

    List<Room> allRooms();

}
