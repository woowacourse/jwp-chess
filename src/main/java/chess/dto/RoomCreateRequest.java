package chess.dto;

public class RoomCreateRequest {

    private String title;
    private String password;

    public RoomCreateRequest() {
    }

    public RoomCreateRequest(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
