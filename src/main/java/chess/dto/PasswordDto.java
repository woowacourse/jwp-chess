package chess.dto;

public class PasswordDto {

    private final String password;

    public PasswordDto() {
        this.password = "";
    }

    public PasswordDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
