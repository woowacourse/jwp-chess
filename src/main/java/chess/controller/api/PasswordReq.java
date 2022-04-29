package chess.controller.api;

public class PasswordReq {

    private String password;

    private PasswordReq() {
    }

    public PasswordReq(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
