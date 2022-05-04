package chess.domain;

import chess.domain.board.BoardGenerator;
import chess.domain.piece.unit.Piece;
import chess.domain.position.Position;
import java.util.Collections;
import java.util.Map;

public class EmptyBoardGenerator implements BoardGenerator {
    @Override
    public Map<Position, Piece> generate() {
        return Collections.emptyMap();
    }
}
