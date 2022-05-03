package chess.model.service.fake;

import chess.model.piece.Piece;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import chess.repository.SquareRepository;

import java.util.Map;
import java.util.Set;

public class FakeSquareRepository implements SquareRepository<Square> {

    @Override
    public Square save(Square square) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public Square getBySquareAndBoardId(Square square, int boardId) {
        return new Square(1, File.A, Rank.FOUR, 1);
    }

    @Override
    public int saveAllSquares(int boardId, Set<Square> squares) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public Map<Square, Piece> findAllSquaresAndPieces(int boardId) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

}
