package chess.domain.game;

import chess.domain.pieces.Piece;
import chess.domain.position.Position;

import java.util.Map;

public interface BoardInitializer {

    Map<Position, Piece> initialize();
}
