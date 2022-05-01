package chess.dto;

public class PasswordDto {

    private String password;

    private PasswordDto() {
    }

    public PasswordDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
