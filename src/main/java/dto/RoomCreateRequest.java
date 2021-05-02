package dto;

public class RoomCreateRequest {
    private String name;
    private String pw;
    private String user;

    public RoomCreateRequest(final String name, final String pw, final String user) {
        this.name = name;
        this.pw = pw;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }

    public String getUser() {
        return user;
    }
}
