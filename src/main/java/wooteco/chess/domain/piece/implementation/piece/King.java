package wooteco.chess.domain.piece.implementation.piece;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.implementation.Strategy.KingStrategy;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class King extends Piece {

    private King(Position position, Team team) {
        super(new KingStrategy(team), PieceType.KING, position, team);
    }

    public static King of(Position position, Team team) {
        return new King(position, team);
    }

    @Override
    protected PieceState movedPieceState(Position target) {
        return new King(target, team);
    }
}
