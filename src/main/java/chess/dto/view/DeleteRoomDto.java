package chess.dto.view;

public class DeleteRoomDto {

    private Integer roomId;

    protected DeleteRoomDto() {
    }

    public Integer getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "DeleteRoomDto{" +
            "roomId=" + roomId +
            '}';
    }
}
