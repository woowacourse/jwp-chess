package chess.dto;

public class LoginRequestDTO {

    private final int roomId;
    private final String password;

    public LoginRequestDTO(int roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}
