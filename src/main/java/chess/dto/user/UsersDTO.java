package chess.dto.user;

public final class UsersDTO {
    private String blackUser;
    private String whiteUser;

    public UsersDTO() {
    }

    public UsersDTO(final String blackUser, final String whiteUser) {
        this.blackUser = blackUser;
        this.whiteUser = whiteUser;
    }

    public String getBlackUser() {
        return blackUser;
    }

    public String getWhiteUser() {
        return whiteUser;
    }

    public void setBlackUser(String blackUser) {
        this.blackUser = blackUser;
    }

    public void setWhiteUser(String whiteUser) {
        this.whiteUser = whiteUser;
    }
}
