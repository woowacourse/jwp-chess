package chess.repository;

import chess.model.piece.Piece;
import chess.model.square.Square;
import java.util.Map;
import java.util.Set;

public interface SquareRepository<T> {

    Square save(Square square);

    T getBySquareAndBoardId(T square, int boardId);

    int saveAllSquares(int boardId, Set<Square> squares);

    Map<T, Piece> findAllSquaresAndPieces(int boardId);
}
