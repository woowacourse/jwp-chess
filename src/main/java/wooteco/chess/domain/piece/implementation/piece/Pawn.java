package wooteco.chess.domain.piece.implementation.piece;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.implementation.Strategy.PawnStrategy;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class Pawn extends Piece {

    private Pawn(Position position, Team team) {
        super(new PawnStrategy(team), PieceType.PAWN, position, team);
    }

    public static Pawn of(Position position, Team team) {
        return new Pawn(position, team);
    }

    @Override
    public double getPoint(BoardSituation boardSituation) {
        if (boardSituation.existSamePieceInSameFile(position, team)) {
            return pieceType.getPoint() / 2;
        }
        return pieceType.getPoint();
    }

    @Override
    protected PieceState movedPieceState(Position target) {
        return new Pawn(target, team);
    }
}
