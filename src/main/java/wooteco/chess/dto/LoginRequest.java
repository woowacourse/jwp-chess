package wooteco.chess.dto;

public class LoginRequest {

    private final Long id;
    private final Boolean loginSuccess;

    public LoginRequest(final Long id, final Boolean loginSuccess) {
        this.id = id;
        this.loginSuccess = loginSuccess;
    }

    public Long getId() {
        return id;
    }

    public Boolean getLoginSuccess() {
        return loginSuccess;
    }
}
