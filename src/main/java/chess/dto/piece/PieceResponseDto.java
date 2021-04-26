package chess.dto.piece;

import chess.dao.dto.PieceDto;
import chess.domain.piece.PieceType;
import chess.domain.team.Team;

public class PieceResponseDto {

    private final int x;
    private final int y;
    private final Team color;
    private final PieceType shape;

    private PieceResponseDto(final int x, final int y, final Team color, final PieceType shape) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.shape = shape;
    }

    public static PieceResponseDto from(final PieceDto pieceDto) {
        return new PieceResponseDto(
            pieceDto.getX(), pieceDto.getY(), Team.from(pieceDto.getColor()),
            PieceType.from(pieceDto.getShape())
        );
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
