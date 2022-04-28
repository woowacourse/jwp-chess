package chess.dao;

import chess.domain.ChessGame2;
import chess.domain.Color;

public interface BoardDao {

    Color findTurn(Long boardId);

    void deleteBoard();

    boolean existsBoardByName(String title);

    Long save(ChessGame2 chessGame2);

    void updateTurn(Long boardId, Color turn);
}
