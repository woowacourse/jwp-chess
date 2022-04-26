package chess.controller.dto.response;

import java.util.List;

public class GameIdsResponse {

    private final List<Long> gameIds;

    public GameIdsResponse(List<Long> gameIds) {
        this.gameIds = gameIds;
    }

    public List<Long> getGameIds() {
        return gameIds;
    }
}
