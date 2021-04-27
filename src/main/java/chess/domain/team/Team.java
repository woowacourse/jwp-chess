package chess.domain.team;

import chess.domain.Position;
import chess.domain.piece.Piece;

import java.util.Map;

public final class Team {
    private final PiecePositions piecePositions;
    private final Score score;

    public Team(final PiecePositions piecePositions) {
        this.piecePositions = piecePositions;
        this.score = new Score();
    }

    public static Team blackTeam() {
        return new Team(PiecePositions.initBlackPosition());
    }

    public static Team whiteTeam() {
        return new Team(PiecePositions.initWhitePosition());
    }

    public Piece choosePiece(final Position position) {
        return piecePositions.choosePiece(position);
    }

    public void movePiece(final Position current, final Position destination) {
        piecePositions.movePiece(current, destination);
    }

    public void moveCastlingRook(final Position destination) {
        piecePositions.moveCastlingRook(destination);
    }

    public boolean havePiece(final Position position) {
        return piecePositions.havePiece(position);
    }

    public Piece deletePiece(final Position destination) {
        return piecePositions.deletePiece(destination);
    }

    public void promotePiece(final Position position) {
        piecePositions.promote(position);
    }

    public double calculateScore() {
        return score.calculateScoreOnLine(currentPiecePosition());
    }

    public Map<Position, Piece> currentPiecePosition() {
        return piecePositions.getPiecePosition();
    }
}
