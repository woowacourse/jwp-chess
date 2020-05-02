package wooteco.chess.dto;

public class GameRequestDto {

    private final Long roomId;

    public GameRequestDto(final Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}
