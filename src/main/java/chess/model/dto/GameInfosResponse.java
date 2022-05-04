package chess.model.dto;

import java.util.List;

public class GameInfosResponse {
    private List<GameInfoResponse> gameInfos;

    public GameInfosResponse(List<GameInfoResponse> gameInfos) {
        this.gameInfos = gameInfos;
    }

    public List<GameInfoResponse> getGameInfos() {
        return gameInfos;
    }
}
