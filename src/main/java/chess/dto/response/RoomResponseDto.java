package chess.dto.response;

import chess.domain.GameStatus;

public class RoomResponseDto {

    private final int roomId;
    private final String roomName;
    private final GameStatus gameStatus;

    private RoomResponseDto(final int roomId, final String roomName, final GameStatus gameStatus) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.gameStatus = gameStatus;
    }

    public static RoomResponseDto of(final int roomId, final String roomName, final String gameStatus) {
        return new RoomResponseDto(
                roomId,
                roomName,
                GameStatus.from(gameStatus)
        );
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
