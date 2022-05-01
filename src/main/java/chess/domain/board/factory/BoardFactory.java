package chess.domain.board.factory;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import java.util.Map;

public interface BoardFactory {
    Map<Position, Piece> create();
}
