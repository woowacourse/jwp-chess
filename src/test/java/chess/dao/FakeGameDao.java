package chess.dao;

import chess.dao.entity.GameEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameDao {

    private final Map<Long, GameEntity> games = new HashMap<>();
    private Long id = 0L;

    @Override
    public Long save(GameEntity game) {
        games.put(id, game);
        return id++;
    }

    @Override
    public void removeById(Long id) {
        games.remove(id);
    }

    @Override
    public GameEntity findGameById(Long id) {
        return games.get(id);
    }

    @Override
    public String findPasswordById(Long id) {
        return games.get(id).getPassword();
    }

    @Override
    public String findStatusById(Long id) {
        return games.get(id).getStatus();
    }

    @Override
    public List<GameEntity> findAll() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(Long id, String turn, String status) {
        GameEntity gameDto = games.get(id);
        games.replace(id, new GameEntity(id, gameDto.getTitle(), gameDto.getPassword(), turn, status));
    }

    @Override
    public void updateStatus(Long id, String status) {
        GameEntity game = games.get(id);
        GameEntity updatedGame = new GameEntity(game.getId(), game.getTitle(), game.getPassword(), game.getTurn(), status);
        games.replace(id, updatedGame);
    }
}
