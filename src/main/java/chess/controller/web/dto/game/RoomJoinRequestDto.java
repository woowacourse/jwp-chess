package chess.controller.web.dto.game;

public class RoomJoinRequestDto {

    private String username;
    private String password;

    public RoomJoinRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
