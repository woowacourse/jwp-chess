package wooteco.chess.domain.piece.implementation.piece;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.implementation.Strategy.KnightStrategy;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class Knight extends Piece {

    private Knight(Position position, Team team) {
        super(new KnightStrategy(team), PieceType.KNIGHT, position, team);
    }

    public static Knight of(Position position, Team team) {
        return new Knight(position, team);
    }

    @Override
    protected PieceState movedPieceState(Position target) {
        return new Knight(target, team);
    }
}
