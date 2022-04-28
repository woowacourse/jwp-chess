package chess.dao;

import chess.dao.dto.GameDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeJdbcGameDao implements SpringGameDao {

    Long id = 0L;
    private Map<Long, GameDto> games = new HashMap<>();

    @Override
    public long save(GameDto gameDto) {
        games.put(id, gameDto);
        return id++;
    }

    @Override
    public void remove(GameDto gameDto) {

    }

    @Override
    public GameDto find(Long id, String password) {
        return games.get(id);
    }

    @Override
    public List<GameDto> findAll() {
        return null;
    }
}
