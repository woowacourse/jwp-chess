package chess.dto.user;

public final class UserDTO {
    private final int id;
    private final String nickname;

    public UserDTO(final int id, final String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }
}
