package chess.service.dto;

public class PasswordRequestDto {

    private final int id;
    private final String password;

    public PasswordRequestDto(final int id, final String password) {
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
