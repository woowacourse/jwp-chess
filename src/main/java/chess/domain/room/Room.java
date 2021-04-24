package chess.domain.room;

import chess.domain.game.ChessGame;

public class Room {
    private final Long roomId;
    private final String roomName;
    private final ChessGame chessGame;

    public Room(Long roomId, String roomName, ChessGame chessGame) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.chessGame = chessGame;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

}
