package chess.repository.entity;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.Objects;

public class BoardEntity {

    private long gameId;
    private Position position;
    private Piece piece;

    public BoardEntity(long gameId, Position position, Piece piece) {
        this.gameId = gameId;
        this.position = position;
        this.piece = piece;
    }

    public BoardEntity(long gameId) {
        this.gameId = gameId;
    }

    public long getGameId() {
        return gameId;
    }

    public Position getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardEntity that = (BoardEntity) o;
        return gameId == that.gameId && Objects.equals(position, that.position) && Objects.equals(piece,
                that.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, position, piece);
    }
}
