package chess.dto;

import javax.validation.constraints.Size;

public class RoomCreateRequest {
    @Size(min = 2, max = 8)
    private String name;
    @Size(min = 4, max = 8)
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
