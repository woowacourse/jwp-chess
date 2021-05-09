package chess.webdto.view;

public class AllowedUserResponse {
    private boolean allowed;

    public AllowedUserResponse() {
    }

    public AllowedUserResponse(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
