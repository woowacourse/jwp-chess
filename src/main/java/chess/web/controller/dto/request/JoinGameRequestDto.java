package chess.web.controller.dto.request;

public class JoinGameRequestDto {

    private Long gameId;
    private String rawBlackPlayerPassword;

    public Long getGameId() {
        return gameId;
    }

    public String getRawBlackPlayerPassword() {
        return rawBlackPlayerPassword;
    }
}
