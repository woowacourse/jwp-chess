package chess.dto;

public class DeleteResponseDto {
    private final String roomName;
    private final Boolean success;

    public DeleteResponseDto(String roomName, boolean success) {
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
