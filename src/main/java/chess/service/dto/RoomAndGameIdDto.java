package chess.service.dto;

public class RoomAndGameIdDto {

    private Long roomId;
    private Long gameId;

    public RoomAndGameIdDto(final Long roomId, final Long gameId) {
        this.roomId = roomId;
        this.gameId = gameId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getGameId() {
        return gameId;
    }
}
