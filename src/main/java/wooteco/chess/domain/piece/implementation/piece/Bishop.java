package wooteco.chess.domain.piece.implementation.piece;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.implementation.Strategy.BishopStrategy;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class Bishop extends Piece {

    private Bishop(Position position, Team team) {
        super(new BishopStrategy(team), PieceType.BISHOP, position, team);
    }

    public static Bishop of(Position position, Team team) {
        return new Bishop(position, team);
    }

    @Override
    protected PieceState movedPieceState(Position target) {
        return new Bishop(target, team);
    }
}
