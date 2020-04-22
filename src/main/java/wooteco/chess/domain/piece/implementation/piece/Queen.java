package wooteco.chess.domain.piece.implementation.piece;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.implementation.Strategy.QueenStrategy;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class Queen extends Piece {

    private Queen(Position position, Team team) {
        super(new QueenStrategy(team), PieceType.QUEEN, position, team);
    }

    public static Queen of(Position position, Team team) {
        return new Queen(position, team);
    }

    @Override
    protected PieceState movedPieceState(Position target) {
        return new Queen(target, team);
    }
}
