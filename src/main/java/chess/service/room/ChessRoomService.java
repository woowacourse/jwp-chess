package chess.service.room;

import chess.dto.*;

import java.util.List;

public interface ChessRoomService {
    RoomCreateResponse create(RoomCreateRequest roomCreateRequest);
    RoomDto enter(RoomRequestDto roomRequestDto);
    RoomLoadResponse load(Long roomId);
    List<RoomDto> rooms();
    void exit(Long roomId, String userName);
    ChessGameDto exitReturnEndChessGame(RoomRequestDto roomRequestDto, String userName);
}
