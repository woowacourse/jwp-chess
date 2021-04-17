package chess.domain.board;

import chess.domain.color.type.TeamColor;
import chess.domain.piece.Piece;
import chess.domain.piece.type.PieceType;

public class Cell {

    private static final String EMPTY_STATUS = ".";

    private final Piece piece;

    public Cell(String cellStatus) {
        if (EMPTY_STATUS.equals(cellStatus)) {
            piece = null;
            return;
        }
        piece = Piece.of(cellStatus);
    }

    public Cell() {
        this(EMPTY_STATUS);
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

    public Piece getPiece() {
        return piece;
    }

    public String getStatus() {
        if (piece != null) {
            return piece.getName();
        }
        return EMPTY_STATUS;
    }

    public boolean containsPieceColorOf(TeamColor teamColor) {
        if (isEmpty()) {
            return false;
        }
        return getTeamColor() == teamColor;
    }
}
