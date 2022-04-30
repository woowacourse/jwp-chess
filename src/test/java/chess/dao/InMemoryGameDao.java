package chess.dao;

import chess.entity.GameEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryGameDao implements GameDao {

    private static final String DEFAULT_STATUS = "READY";
    private static final String DEFAULT_TURN = "WHITE";
    private final Map<Integer, GameEntity> gameTable = new HashMap<>();
    private int id = 1;

    @Override
    public int createGame(String name, String password) {
        gameTable.put(id++, new GameEntity(1, name, DEFAULT_STATUS, DEFAULT_TURN, password));
        return id;
    }

    @Override
    public int deleteGame(Integer id) {
        gameTable.remove(id);
        return 1;
    }

    @Override
    public String findPasswordById(Integer gameId) {
        return gameTable.get(gameId).getPassword();
    }

    @Override
    public int update(GameEntity dto) {
        gameTable.put(dto.getId(), dto);
        return 1;
    }

    @Override
    public GameEntity findById(Integer id) {
        return gameTable.get(id);
    }

    @Override
    public List<GameEntity> findAll() {
        return new ArrayList<>(gameTable.values());
    }

    public Map<Integer, GameEntity> getGameTable() {
        return gameTable;
    }
}
