package chess.webdto;

public class UserInfoDto {
    private final String id;
    private final String password;

    public UserInfoDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
