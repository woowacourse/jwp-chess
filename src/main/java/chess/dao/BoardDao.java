package chess.dao;

import chess.domain.ChessGame;
import chess.domain.Color;
import chess.dto.RoomDto;
import java.util.List;

public interface BoardDao {

    Color findTurn(Long boardId);

    void deleteBoard(Long boardId);

    boolean existsBoardByName(String title);

    Long save(ChessGame chessGame);

    void updateTurn(Long boardId, Color turn);

    List<RoomDto> findAllRooms();

    String findPasswordById(Long boardId);
}
