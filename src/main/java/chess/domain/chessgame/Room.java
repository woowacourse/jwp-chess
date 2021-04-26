package chess.domain.chessgame;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Map;

public class Room {

    private final int id;
    private final ChessGame chessGame;

    public Room(int id, ChessGame chessGame) {
        this.id = id;
        this.chessGame = chessGame;
    }

    public Room(int roomId) {
        this.id = roomId;
        this.chessGame = new ChessGame();
    }

    public int getId() {
        return id;
    }

    public Map<Position, Piece> pieces() {
        return chessGame.pieces();
    }

    public ChessGame chessgame() {
        return chessGame;
    }

    public boolean isBlackTurn() {
        return chessGame.isBlackTurn();
    }

    public boolean isPlaying() {
        return chessGame.isPlaying();
    }

    public Color winnerColor() {
        return chessGame.winnerColor();
    }

}
