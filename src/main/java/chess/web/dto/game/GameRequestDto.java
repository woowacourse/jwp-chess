package chess.web.dto.game;

public class GameRequestDto {

    private final int id;
    private final String password;

    public GameRequestDto(int id, String password) {
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
