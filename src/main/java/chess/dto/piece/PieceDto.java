package chess.dto.piece;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.team.Team;

public class PieceDto {

    private final int x;
    private final int y;
    private final Team color;
    private final PieceType shape;

    public PieceDto(final int x, final int y, final Team color, final PieceType shape) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.shape = shape;
    }

    public static PieceDto from(final Piece piece) {
        return new PieceDto(piece.getX(), piece.getY(), piece.getTeam(), piece.getPieceType());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Team getColor() {
        return color;
    }

    public PieceType getShape() {
        return shape;
    }

}
