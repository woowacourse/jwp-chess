package chess.web.dto;

public class CreateRoomResultDto {
    private String roomTitle;

    public CreateRoomResultDto() {
    }

    public CreateRoomResultDto(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }
}
