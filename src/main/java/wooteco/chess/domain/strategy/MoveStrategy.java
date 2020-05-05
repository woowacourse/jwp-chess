package wooteco.chess.domain.strategy;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

import java.util.List;

public interface MoveStrategy {
    List<Position> possiblePositions(final Board board, final Piece piece, final Position position) throws IllegalArgumentException;
}
