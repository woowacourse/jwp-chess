package chess.domain.board.piece;

import chess.domain.Entity;
import chess.domain.board.position.Position;

public class Square extends Entity<Long> {

    private final Long gameId;
    private final Position position;
    private final Piece piece;

    public Square(Long gameId, Position position, Piece piece) {
        this(null, gameId, position, piece);
    }

    public Square(Long id, Long gameId, Position position, Piece piece) {
        super(id);
        this.gameId = gameId;
        this.position = position;
        this.piece = piece;
    }

    public Long getGameId() {
        return gameId;
    }

    public Position getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }
}
