package chess.web.dto;

public class RoomDto {
    private int roomId;
    private String roomTitle;

    public RoomDto() {
    }

    public RoomDto(int roomId, String roomTitle) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }
}
