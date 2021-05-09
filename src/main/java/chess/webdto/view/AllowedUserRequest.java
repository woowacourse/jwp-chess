package chess.webdto.view;

public class AllowedUserRequest {
    private String password;

    public AllowedUserRequest() {
    }

    public AllowedUserRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
