package chess.controller.dto.request;

import javax.validation.constraints.NotEmpty;

public class ChessGamePasswordRequest {

    @NotEmpty(message = "비밀번호를 입력해 주세요.")
    private String password;

    private ChessGamePasswordRequest() {
    }

    public ChessGamePasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
