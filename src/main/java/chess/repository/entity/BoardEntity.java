package chess.repository.entity;

import chess.domain.board.Position;
import chess.domain.piece.Piece;

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
}
