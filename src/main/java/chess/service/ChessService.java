package chess.service;

import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import chess.dto.web.PointDto;
import chess.dto.web.RoomDto;
import chess.dto.web.UsersInRoomDto;
import java.util.List;

public interface ChessService {

    List<RoomDto> openedRooms();

    BoardDto latestBoard(String id);

    UsersInRoomDto usersInRoom(String id);

    RoomDto create(RoomDto roomDto);

    GameStatusDto gameStatus(String id);

    BoardDto start(String id);

    BoardDto exit(String id);

    void close(String id);

    List<PointDto> movablePoints(String id, String point);

    BoardDto move(String id, String source, String destination);
}
