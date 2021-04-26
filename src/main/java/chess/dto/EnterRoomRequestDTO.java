package chess.dto;

public class EnterRoomRequestDTO {
    private final String id;
    private final String nickname;
    private final String password;

    public EnterRoomRequestDTO(final String id, final String nickname, final String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
