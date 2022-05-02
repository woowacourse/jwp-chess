package chess.dao;

import chess.dto.GameDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeGameDao implements GameDao {

    private Map<Integer, GameDto> map = new HashMap<>();
    private int id = 1;

    @Override
    public int save(GameDto gameDto) {
        map.put(id, gameDto);
        return id++;
    }

    @Override
    public String findStateById(int gameId) {
        return map.get(gameId).getState();
    }

    @Override
    public void update(String state, int gameId) {
        GameDto gameDto = map.get(gameId);
        GameDto newGameDto = new GameDto(gameDto.getRoomName(), gameDto.getPassword(), state);
        map.put(gameId, newGameDto);
    }

    @Override
    public void deleteById(int gameId) {
        map.remove(gameId);
    }

    @Override
    public String findPasswordById(int gameId) {
        return map.get(gameId).getPassword();
    }

    @Override
    public List<GameDto> findGames() {
        return map.values().stream()
                .collect(Collectors.toList());
    }
}
