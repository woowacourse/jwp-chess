package chess.dao;

import chess.dto.RoomResponseDto;
import java.util.List;


public interface RoomDao {

    void create(String name, String pw);

    List<RoomResponseDto> getRooms();

    List<RoomResponseDto> deleteRoom();
}
