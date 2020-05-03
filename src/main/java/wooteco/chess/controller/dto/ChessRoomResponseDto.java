package wooteco.chess.controller.dto;

public class ChessRoomResponseDto {
    private Long roomId;
    private String title;

    public ChessRoomResponseDto(Long roomId, String title) {
        this.roomId = roomId;
        this.title = title;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }
}
