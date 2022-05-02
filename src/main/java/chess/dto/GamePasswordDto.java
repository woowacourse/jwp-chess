package chess.dto;

public class GamePasswordDto {

    private String password;

    private GamePasswordDto() {
    }

    public GamePasswordDto(final String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
