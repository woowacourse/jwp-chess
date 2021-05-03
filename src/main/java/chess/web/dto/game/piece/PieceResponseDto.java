package chess.web.dto.game.piece;

import chess.dao.dto.PieceDto;
import chess.domain.game.board.piece.Piece;
import chess.domain.game.board.piece.PieceType;
import chess.domain.game.team.Team;

public class PieceResponseDto {

    private int x;
    private int y;
    private Team color;
    private PieceType shape;

    public PieceResponseDto() {
    }

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

    public static PieceResponseDto from(final Piece piece) {
        return new PieceResponseDto(
            piece.getX(), piece.getY(), piece.getTeam(), piece.getPieceType()
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
