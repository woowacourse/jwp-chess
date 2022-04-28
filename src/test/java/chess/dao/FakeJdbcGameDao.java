package chess.dao;

import chess.dao.dto.GameDto;
import chess.dto.GameStatusDto;

import java.util.ArrayList;
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
        games.remove(gameDto.getId());
    }

    @Override
    public GameDto findById(Long id) {
        return games.get(id);
    }

    @Override
    public List<GameDto> findAll() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameDto game) {
        games.replace(game.getId(), game);
    }

    @Override
    public void updateStatus(Long gameId, GameStatusDto status) {
        GameDto game = games.get(gameId);
        GameDto updatedGame = new GameDto(game.getId(), game.getTitle(), game.getPassword(), game.getTurn(), status.getName());
        games.replace(gameId, updatedGame);
    }
}
