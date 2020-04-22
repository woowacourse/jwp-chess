package wooteco.chess.domain.strategy.initialize;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class QueenInitializer implements InitializeStrategy {
    @Override
    public Map<Position, Piece> initialize() {
        Map<Position, Piece> pieces = new HashMap<>();
        pieces.put(Positions.of("d8"), new Piece(PieceType.QUEEN, Team.BLACK));
        pieces.put(Positions.of("d1"), new Piece(PieceType.QUEEN, Team.WHITE));

        return Collections.unmodifiableMap(pieces);
    }
}