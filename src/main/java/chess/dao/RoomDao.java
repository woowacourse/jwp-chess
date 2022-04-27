package chess.dao;

import chess.dto.RoomDto;
import java.util.List;

public interface RoomDao {
    void createRoom(RoomDto room);

    int getRecentRoomId();

    boolean matchPassword(int id, String password);

    List<RoomDto> getRooms();

    void deleteRoom(RoomDto roomDto);
}
