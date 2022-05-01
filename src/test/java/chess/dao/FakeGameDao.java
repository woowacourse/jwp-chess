package chess.dao;

import chess.dao.entity.Game;
import chess.domain.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameDao {

    Long id = 0L;
    private Map<Long, Game> games = new HashMap<>();

    @Override
    public Long save(Game game) {
        games.put(id, game);
        return id++;
    }

    @Override
    public void removeById(Long id) {
        games.remove(id);
    }

    @Override
    public Game findGameById(Long id) {
        return games.get(id);
    }

    @Override
    public String findPasswordById(Long id) {
        return games.get(id).getPassword();
    }

    @Override
    public GameStatus findStatusById(Long id) {
        return GameStatus.find(games.get(id).getStatus());
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(Long id, String turn, String status) {
        Game gameDto = games.get(id);
        games.replace(id, new Game(id, gameDto.getTitle(), gameDto.getPassword(), turn, status));
    }

    @Override
    public void updateStatus(Long id, GameStatus status) {
        Game game = games.get(id);
        Game updatedGame = new Game(game.getId(), game.getTitle(), game.getPassword(), game.getTurn(), status.getName());
        games.replace(id, updatedGame);
    }
}
