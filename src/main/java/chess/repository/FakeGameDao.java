package chess.repository;

import chess.domain.state.State;
import chess.dto.GameDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameDao {

    private final Map<Integer, GameDto> game;
    private int id = 1;

    public FakeGameDao() {
        game = new HashMap<>();
    }

    @Override
    public int save(String title, String password, String state) {
        GameDto gameDto = new GameDto(id, title, state, password);
        game.put(id, gameDto);
        return id++;
    }

    @Override
    public List<GameDto> findAll() {
        return new ArrayList<>(game.values());
    }

    @Override
    public State findState(int id) {
        GameDto gameDto = game.get(id);
        String state = gameDto.getState();
        return State.of(state);
    }

    @Override
    public String findPassword(int id) {
        GameDto gameDto = game.get(id);
        return gameDto.getPassword();
    }

    @Override
    public Long findGameCount() {
        return (long) game.size();
    }

    @Override
    public int update(String state, int id) {
        GameDto value = game.get(id);
        GameDto gameDto = new GameDto(value.getId(), value.getTitle(), state, value.getPassword());
        game.put(id, gameDto);
        return id;
    }

    @Override
    public int delete(int id) {
        game.remove(id);
        return id;
    }
}
