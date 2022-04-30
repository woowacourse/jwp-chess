package chess.dto;

public class DeleteRoomDto {

    private Long roomId;
    private String password;

    public DeleteRoomDto() {
    }

    public DeleteRoomDto(Long roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}
