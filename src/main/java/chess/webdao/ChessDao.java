package chess.webdao;

import chess.webdto.MoveRequestDto;

import java.util.List;

public interface ChessDao {
    int insertMove(String start, String destination);

    List<MoveRequestDto> selectAllMovesByRoomId(int roomId);

    void deleteMovesByRoomId(int roomId);

}