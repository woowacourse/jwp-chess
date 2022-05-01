package chess.dto.request;

public class UserPasswordRequest {

    private final String password;

    public UserPasswordRequest(final String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
