package chess.service.room;

import dto.ChessGameDto;
import dto.RoomDto;
import dto.RoomRequestDto;

import java.util.List;

public interface ChessRoomService {
    RoomDto create(RoomRequestDto room);
    ChessGameDto enter(RoomRequestDto room);
    List<RoomDto> rooms();
    void exit(Long roomId, String userName);
}
