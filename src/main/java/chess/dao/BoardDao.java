package chess.dao;

import chess.domain.ChessGame2;
import chess.domain.Color;
import chess.dto.RoomDto;
import java.util.List;

public interface BoardDao {

    Color findTurn(Long boardId);

    void deleteBoard(Long boardId);

    boolean existsBoardByName(String title);

    Long save(ChessGame2 chessGame2);

    void updateTurn(Long boardId, Color turn);

    List<RoomDto> findAllRooms();
}
