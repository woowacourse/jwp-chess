package chess.dto;

public class CreateRoomRequestDTO {
    private final String title;
    private final String nickname;
    private final String password;

    public CreateRoomRequestDTO(final String title, final String nickname, final String password) {
        this.title = title;
        this.nickname = nickname;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
