package chess.dto;

public class DeleteDto {
    private final String roomName;
    private final Boolean success;

    public DeleteDto(String roomName, boolean success) {
        this.roomName = roomName;
        this.success = success;
    }

    public String getRoomName() {
        return roomName;
    }

    public Boolean getSuccess() {
        return success;
    }
}
