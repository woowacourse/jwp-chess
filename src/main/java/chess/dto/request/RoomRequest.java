package chess.dto.request;

public class RoomRequest {

    private final String name;
    private final String password;

    public RoomRequest(final String name, final String password) {
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
