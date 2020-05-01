package wooteco.chess.controller.dto;

public class ChessRoomResponseDto {
    private Long roomId;

    public ChessRoomResponseDto(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}
