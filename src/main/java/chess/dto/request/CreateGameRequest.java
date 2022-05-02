package chess.dto.request;

import javax.validation.constraints.NotBlank;

public class CreateGameRequest {

    @NotBlank(message = "게임 이름은 빈 값일 수 없습니다.")
    private String gameName;
    @NotBlank(message = "비밀번호는 빈 값일 수 없습니다.")
    private String password;

    public CreateGameRequest() {
    }

    public CreateGameRequest(String gameName, String password) {
        this.gameName = gameName;
        this.password = password;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPassword() {
        return password;
    }
}
