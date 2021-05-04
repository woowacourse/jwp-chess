package chess.dto.request;

import javax.validation.constraints.Size;

public class UserCreateRequest {
    @Size(min = 2, max = 4, message = "유저 이름은 2글자 이상 4글자 이하 입니다.")
    private final String userName;
    @Size(min = 2, max = 8, message = "유저 비밀번호는 2글자 이상 4글자 이하 입니다.")
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
