package chess.domain;

import chess.domain.piece.Color;
import chess.domain.vo.Room;
import chess.dto.GameStatus;

public class ChessGame {

    private final Room room;
    private final GameStatus status;
    private final Color currentColor;
    private final GameScore gameScore;

    public ChessGame(Room room, GameStatus status, Color currentColor, GameScore gameScore) {
        this.room = room;
        this.status = status;
        this.currentColor = currentColor;
        this.gameScore = gameScore;
    }

    public static ChessGame start(Room room) {
        return new ChessGame(room, GameStatus.READY, Color.WHITE, new GameScore(new Score(), new Score()));
    }

    public Room getRoom() {
        return room;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Score getBlackScore() {
        return gameScore.getBlackScore();
    }

    public Score getWhiteScore() {
        return gameScore.getWhiteScore();
    }

    public Color getCurrentColor() {
        return currentColor;
    }
}
