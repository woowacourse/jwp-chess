package chess.service.dto.game;

public class RoomJoinDto {

    private String username;
    private String password;

    public RoomJoinDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
