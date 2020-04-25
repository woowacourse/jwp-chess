package wooteco.chess.domain.piece.implementation.piece;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.implementation.Strategy.RookStrategy;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class Rook extends Piece {

    private Rook(Position position, Team team) {
        super(new RookStrategy(team), PieceType.ROOK, position, team);
    }

    public static Rook of(Position position, Team team) {
        return new Rook(position, team);
    }

    @Override
    protected PieceState movedPieceState(Position target) {
        return new Rook(target, team);
    }
}
