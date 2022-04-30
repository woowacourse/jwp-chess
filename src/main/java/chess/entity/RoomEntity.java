package chess.entity;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.domain.room.Password;
import chess.domain.room.RoomName;

public class RoomEntity {

    private final int roomId;
    private final String name;
    private final String gameStatus;
    private final String currentTurn;
    private final String password;
    private final boolean isDelete;

    public RoomEntity(final int roomId, final String name, final String gameStatus, final String currentTurn,
                      final String password, final boolean isDelete) {
        this.roomId = roomId;
        this.name = name;
        this.gameStatus = gameStatus;
        this.currentTurn = currentTurn;
        this.password = password;
        this.isDelete = isDelete;
    }

    public RoomName toRoomName() {
        return new RoomName(name);
    }

    public GameStatus toGameStatus() {
        return GameStatus.from(gameStatus);
    }

    public Color toCurrentTurn() {
        return Color.from(currentTurn);
    }

    public Password toPassword() {
        return Password.fromHash(password);
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public String getPassword() {
        return password;
    }

    public boolean isDelete() {
        return isDelete;
    }
}
