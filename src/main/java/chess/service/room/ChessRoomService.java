package chess.service.room;

import chess.dto.RoomDto;
import chess.dto.request.RoomCreateRequest;
import chess.dto.request.RoomEnterRequest;
import chess.dto.request.RoomExitRequest;
import chess.dto.response.ChessRoomStatusResponse;
import chess.dto.response.RoomEnterResponse;
import chess.dto.response.RoomListResponse;

import java.util.List;

public interface ChessRoomService {
    RoomEnterResponse create(RoomCreateRequest request);
    RoomDto enter(RoomEnterRequest request);
    ChessRoomStatusResponse load(Long roomId);
    List<RoomListResponse> rooms();
    void exit(Long roomId, String userName);
    ChessRoomStatusResponse exitReturnEndChessGame(RoomExitRequest request);
}
