package chess.model.service.fake;

import chess.model.piece.Piece;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import chess.repository.SquareRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FakeSquareRepository implements SquareRepository<Square> {

    @Override
    public Square save(Square square) {
        return null;
    }

    @Override
    public Square getBySquareAndBoardId(Square square, int boardId) {
        return new Square(1, File.A, Rank.FOUR, 1);
    }

    @Override
    public int saveAllSquares(int boardId, Set<Square> squares) {
        return 0;
    }

    @Override
    public Map<Square, Piece> findAllSquaresAndPieces(int boardId) {
        return null;
    }

    @Override
    public List<Square> getPaths(List<Square> squares, int roomId) {
        return null;
    }
}
