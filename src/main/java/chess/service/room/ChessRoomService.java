package chess.service.room;

import dto.ChessGameDto;
import dto.RoomDto;
import dto.RoomRequestDto;

import java.util.List;

public interface ChessRoomService {
    Long create(RoomRequestDto room);
    boolean enterable(RoomRequestDto room);
    List<RoomDto> rooms();
    ChessGameDto enter(RoomRequestDto roomRequestDto);
}
