package chess.web.domain.board;

import chess.web.domain.piece.Piece;
import chess.web.domain.piece.type.PieceType;
import chess.web.domain.player.type.TeamColor;

public class Cell {
    private static final String EMPTY_STATUS = ".";

    private final Piece piece;

    public Cell(Piece piece) {
        this.piece = piece;
    }

    public TeamColor getTeamColor() {
        return piece.getTeamColor();
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public PieceType getPieceType() {
        return piece.getPieceType();
    }

    public Piece getPieceEntity() {
        return piece;
    }

    public String getStatus() {
        if (piece != null) {
            return piece.getName();
        }
        return EMPTY_STATUS;
    }
}
