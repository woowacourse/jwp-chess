package chess.model.dto;

public class DeleteRoomDto {

    private final Integer roomId;

    public DeleteRoomDto(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }
}
