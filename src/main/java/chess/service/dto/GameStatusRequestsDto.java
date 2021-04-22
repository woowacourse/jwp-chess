package chess.service.dto;

import java.util.List;

public class GameStatusRequestsDto {

    private List<GameStatusRequestDto> gameStatusRequests;

    public GameStatusRequestsDto() {}

    public GameStatusRequestsDto(final List<GameStatusRequestDto> gameStatusRequests) {
        this.gameStatusRequests = gameStatusRequests;
    }

    public List<GameStatusRequestDto> getGameStatusRequests() {
        return gameStatusRequests;
    }

    public void setGameStatusRequests(final List<GameStatusRequestDto> gameStatusRequests) {
        this.gameStatusRequests = gameStatusRequests;
    }
}
