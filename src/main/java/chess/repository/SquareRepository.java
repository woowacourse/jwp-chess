package chess.repository;

import chess.model.piece.Piece;
import chess.model.square.Square;

import java.util.List;
import java.util.Map;

public interface SquareRepository<T> {

    Square save(Square square);

    T getBySquareAndBoardId(T square, int boardId);

    int saveAllSquare(int boardId);

    Map<T, Piece> findAllSquaresAndPieces(int boardId);

    List<Square> getPaths(List<T> squares, int roomId);
}
