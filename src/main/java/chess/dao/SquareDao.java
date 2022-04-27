package chess.dao;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;

public interface SquareDao {

    void insert(Long id, Position position, Piece piece);

    Board createBoard(Long id);

    int delete(Long id);

    int update(Long id, Position position, Piece piece);
}
