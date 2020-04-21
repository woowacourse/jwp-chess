package wooteco.chess.strategy;

import wooteco.chess.piece.Piece;
import wooteco.chess.position.Position;

import java.util.Map;

public interface PiecesInitStrategy {
    Map<Position, Piece> init();
}
