package chess.dao;

import chess.dto.RoomDto;
import java.util.List;

public interface RoomDao {

    void createRoom(RoomDto room);

    int getRecentCreatedRoomId();

    boolean matchPassword(int roomId, String password);

    List<RoomDto> getRooms();

    void deleteRoom(RoomDto roomDto);
}
