package springchess.dao;

import springchess.model.piece.Piece;
import springchess.model.square.Square;

import java.util.List;
import java.util.Map;

public interface SquareDao<T> {

    Square save(Square square);

    T getBySquareAndBoardId(T square, int boardId);

    int saveAllSquare(int boardId);

    int getSquareIdBySquare(T square, int boardId);

    Map<T, Piece> findAllSquaresAndPieces(int boardId);

    List<Square> getPaths(List<T> squares, int roomId);
}
