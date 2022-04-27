package chess.dao;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;

public interface SquareDao {

    int insert(String id, Position position, Piece piece);

    Board createBoardFrom(String id);

    int deleteFrom(String id);

    int update(String id, Position position, Piece piece);
}
