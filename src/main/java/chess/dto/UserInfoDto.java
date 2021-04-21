package chess.dto;

public class UserInfoDto {
    private String id;
    private String password;

    public UserInfoDto(String id, Object password) {
        this.id = id;
        this.password = password.toString();
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
