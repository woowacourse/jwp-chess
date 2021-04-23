package chess.dao;

import chess.dto.web.RoomDto;
import java.util.List;

public interface RoomDao {

    String insert(RoomDto roomDto);

    List<RoomDto> openedRooms();

    void close(String roomId);
}
