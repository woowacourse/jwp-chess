package chess.dto.request;

import javax.validation.constraints.Size;

public class UserRequest {
    @Size(min = 2, max = 4)
    private final String name;
    @Size(min = 2, max = 8)
    private final String pw;


    public UserRequest(final String name, final String pw) {
        this.name = name;
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }
}
