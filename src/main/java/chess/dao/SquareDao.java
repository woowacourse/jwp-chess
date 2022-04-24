package chess.dao;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;

public interface SquareDao {

    void insert(Position position, Piece piece);

    Board createBoard();

    int delete();

    int update(Position position, Piece piece);
}
