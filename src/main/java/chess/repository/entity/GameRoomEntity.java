package chess.repository.entity;

import chess.domain.game.GameRoom;

public class GameRoomEntity {

    private final String gameRoomId;
    private final String name;
    private final String password;

    public GameRoomEntity(String gameRoomId, String name, String password) {
        this.gameRoomId = gameRoomId;
        this.name = name;
        this.password = password;
    }

    public GameRoomEntity(GameRoom gameRoom) {
        this.gameRoomId = gameRoom.getGameRoomId();
        this.name = gameRoom.getName();
        this.password = gameRoom.getPassword();
    }

    public String getGameRoomId() {
        return gameRoomId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
