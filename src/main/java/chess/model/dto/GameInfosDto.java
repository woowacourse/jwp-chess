package chess.model.dto;

import java.util.List;

public class GameInfosDto {
    private List<GameInfoDto> gameInfos;

    public GameInfosDto(List<GameInfoDto> gameInfos) {
        this.gameInfos = gameInfos;
    }

    public List<GameInfoDto> getGameInfos() {
        return gameInfos;
    }
}
