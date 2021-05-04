package chess.service.room;

import chess.dto.request.RoomCreateRequest;
import chess.dto.request.RoomEnterRequest;
import chess.dto.request.RoomExitRequest;
import chess.dto.response.ChessRoomStatusResponse;
import chess.dto.response.RoomCreateResponse;
import chess.dto.response.RoomEnterResponse;
import chess.dto.response.RoomListResponse;

import java.util.List;

public interface ChessRoomService {
    RoomCreateResponse create(RoomCreateRequest request);
    RoomEnterResponse enter(RoomEnterRequest request);
    ChessRoomStatusResponse load(Long roomId);
    List<RoomListResponse> rooms();
    void exit(Long roomId, String userName);
    ChessRoomStatusResponse exitReturnEndChessGame(RoomExitRequest request);
}
