package chess.controller.dto.response;

import java.util.Map;

public class PlayersResponse {

    private final Map<ColorResponse, PlayerResponse> playerResponses;

    public PlayersResponse(final Map<ColorResponse, PlayerResponse> playersResponse) {
        this.playerResponses = playersResponse;
    }

    public Map<ColorResponse, PlayerResponse> getPlayerResponses() {
        return playerResponses;
    }
}
