package chess.web.dto.game;

public class PasswordDto {

    private final int id;
    private final String password;

    public PasswordDto(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
