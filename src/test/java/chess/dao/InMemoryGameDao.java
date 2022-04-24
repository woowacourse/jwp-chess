package chess.dao;

import chess.service.dto.GameEntity;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryGameDao implements GameDao {

    private static final String DEFAULT_STATUS = "READY";
    private static final String DEFAULT_TURN = "WHITE";
    private final Map<Integer, GameEntity> gameTable = new HashMap<>();
    private int id = 1;

    @Override
    public int createGame(String name) {
        gameTable.put(id++, new GameEntity(1, name, DEFAULT_STATUS, DEFAULT_TURN));
        return id;
    }

    @Override
    public void update(GameEntity dto) {
        gameTable.put(dto.getId(), dto);
    }

    @Override
    public GameEntity findById(int id) {
        return gameTable.get(id);
    }

    @Override
    public GamesDto findAll() {
        return new GamesDto(new ArrayList<>(gameTable.values()));
    }

    public Map<Integer, GameEntity> getGameTable() {
        return gameTable;
    }
}
