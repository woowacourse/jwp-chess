package chess.web.dto;

public class RoomDto {
    private String roomTitle;
    private String status;

    public RoomDto() {
    }

    public RoomDto(String roomTitle, String status) {
        this.roomTitle = roomTitle;
        this.status = status;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
