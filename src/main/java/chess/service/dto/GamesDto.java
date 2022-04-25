package chess.service.dto;

import chess.dao.GameEntity;
import java.util.ArrayList;
import java.util.List;

public class GamesDto {
    private final List<GameEntity> games;

    public GamesDto(List<GameEntity> games) {
        this.games = new ArrayList<>(games);
    }

    public List<GameEntity> getGames() {
        return games;
    }
}
