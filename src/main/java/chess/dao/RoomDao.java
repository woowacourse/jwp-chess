package chess.dao;

import chess.dto.RoomResponseDto;
import java.util.List;


public interface RoomDao {

    int create(String name, String pw);

    RoomResponseDto getRoom(int id);

    List<RoomResponseDto> getRooms();

    int deleteRoom(String pw, int id);
}
