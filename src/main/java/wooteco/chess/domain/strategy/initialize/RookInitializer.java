package wooteco.chess.domain.strategy.initialize;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class RookInitializer implements InitializeStrategy {
    @Override
    public Map<Position, Piece> initialize() {
        Map<Position, Piece> pieces = new HashMap<>();
        pieces.put(Positions.of("a8"), new Piece(PieceType.ROOK, Team.BLACK));
        pieces.put(Positions.of("a1"), new Piece(PieceType.ROOK, Team.WHITE));
        pieces.put(Positions.of("h8"), new Piece(PieceType.ROOK, Team.BLACK));
        pieces.put(Positions.of("h1"), new Piece(PieceType.ROOK, Team.WHITE));

        return Collections.unmodifiableMap(pieces);
    }
}