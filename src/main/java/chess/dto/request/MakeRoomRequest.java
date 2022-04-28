package chess.dto.request;

public class MakeRoomRequest {

    private final String name;
    private final String password;

    public MakeRoomRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
