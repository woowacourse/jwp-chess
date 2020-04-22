package wooteco.chess.domain.strategy;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.exception.*;

import java.util.List;

public class BlankStrategy implements MoveStrategy {
    private static final String BLANK_MOVE_UNSUPPORTED_EXCEPTION_MESSAGE = "빈 칸은 움직일 수 없습니다.";

    @Override
    public List<Position> possiblePositions(Board board, Piece piece, Position position) {
        throw new PieceImpossibleMoveException(BLANK_MOVE_UNSUPPORTED_EXCEPTION_MESSAGE);
    }
}
