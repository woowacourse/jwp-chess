package chess.dao.dto;

import chess.domain.game.board.piece.Piece;

public class PieceDto {

    private final long id;
    private final long gameId;
    private final int x;
    private final int y;
    private final String color;
    private final char shape;

    private PieceDto(final long id, final long gameId, final int x, final int y, final String color,
        final char shape) {
        this.id = id;
        this.gameId = gameId;
        this.x = x;
        this.y = y;
        this.color = color;
        this.shape = shape;
    }

    public static PieceDto of(final long id, final long gameId, final int x, final int y,
        final String color, final char shape) {

        return new PieceDto(id, gameId, x, y, color, shape);
    }

    public static PieceDto of(final long gameId, final Piece piece) {
        return new PieceDto(
            piece.getId(),
            gameId,
            piece.getX(),
            piece.getY(),
            piece.getTeamValue(),
            piece.getPieceTypeValue()
        );
    }

    public long getId() {
        return id;
    }

    public long getGameId() {
        return gameId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public char getShape() {
        return shape;
    }

}
