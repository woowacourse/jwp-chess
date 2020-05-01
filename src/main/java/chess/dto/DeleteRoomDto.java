package chess.dto;

public class DeleteRoomDto {

    private Integer roomId;

    public DeleteRoomDto() {
    }

    public DeleteRoomDto(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }
}
