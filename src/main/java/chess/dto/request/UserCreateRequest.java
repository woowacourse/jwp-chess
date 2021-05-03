package chess.dto.request;

import javax.validation.constraints.Size;

public class UserCreateRequest {
    @Size(min = 2, max = 4)
    private final String userName;
    @Size(min = 2, max = 8)
    private final String userPw;


    public UserCreateRequest(final String userName, final String userPw) {
        this.userName = userName;
        this.userPw = userPw;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPw() {
        return userPw;
    }
}
