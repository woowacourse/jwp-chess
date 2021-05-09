package chess.controller.web.dto.game;

public class RoomIdAndGameIdResponse {
    private Long roomId;
    private Long gameId;

    public RoomIdAndGameIdResponse() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
