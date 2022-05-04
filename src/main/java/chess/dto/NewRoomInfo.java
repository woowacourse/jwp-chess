package chess.dto;

public class NewRoomInfo {

    private final String name;
    private final String password;
    private final String confirmPassword;

    public NewRoomInfo(String name, String password, String confirmPassword) {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
