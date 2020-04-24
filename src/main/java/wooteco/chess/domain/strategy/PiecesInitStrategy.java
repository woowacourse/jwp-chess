package wooteco.chess.domain.strategy;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;

import java.util.Map;

public interface PiecesInitStrategy {
    Map<Position, Piece> init();
}
