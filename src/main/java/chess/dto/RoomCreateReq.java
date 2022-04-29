package chess.dto;

public class RoomCreateReq {

    private String title;
    private String password;

    public RoomCreateReq() {
    }

    public RoomCreateReq(String title, String password) {
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
