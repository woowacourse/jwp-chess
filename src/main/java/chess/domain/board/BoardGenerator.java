package chess.domain.board;

import chess.domain.piece.Piece;
import chess.domain.position.Square;

import java.util.Map;

@FunctionalInterface
public interface BoardGenerator {

    Map<Square, Piece> generate();
}
