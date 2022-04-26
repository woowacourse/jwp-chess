package chess.web.dto;

public class CreateRoomRequestDto {
    private String roomTitle;
    private String roomPassword;

    public CreateRoomRequestDto() {
    }

    public CreateRoomRequestDto(String roomTitle, String roomPassword) {
        this.roomTitle = roomTitle;
        this.roomPassword = roomPassword;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getRoomPassword() {
        return roomPassword;
    }
}
