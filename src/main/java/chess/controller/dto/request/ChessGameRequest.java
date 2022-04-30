package chess.controller.dto.request;

import javax.validation.constraints.NotEmpty;

public class ChessGameRequest {

    @NotEmpty(message = "체스 게임 제목을 입력해 주세요.")
    private String title;

    @NotEmpty(message = "체스 게임 비밀번호를 입력해 주세요.")
    private String password;

    private ChessGameRequest() {
    }

    public ChessGameRequest(String title, String password) {
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
