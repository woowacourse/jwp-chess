package chess.dto.request;

public class PasswordRequest {

    private final String password;

    public PasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
