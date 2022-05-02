package chess.dao;

import chess.domain.state.State;
import chess.entity.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameDao {

    private final Map<Integer, Game> game;
    private int id = 1;

    public FakeGameDao() {
        game = new HashMap<>();
    }

    @Override
    public int save(Game game) {
        this.game.put(id, game);
        return id++;
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(game.values());
    }

    @Override
    public Game findById(int id) {
        return game.get(id);
    }

    @Override
    public State findState(int id) {
        Game game = this.game.get(id);
        String state = game.getState();
        return State.of(state);
    }

    @Override
    public int update(String state, int id) {
        Game game = this.game.get(id);
        this.game.put(id, new Game(game.getId(), game.getTitle(), game.getPassword(), state));
        return id;
    }

    @Override
    public int delete(int id) {
        game.remove(id);
        return id;
    }
}
