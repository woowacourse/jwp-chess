package chess.dao;

import chess.dto.GameDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FakeGameDao implements GameDao {

    private Map<Integer, GameDto> map = new HashMap<>();
    private int id = 1;

    @Override
    public void save(GameDto gameDto) {
        map.put(id++, gameDto);
    }

    @Override
    public int findGameIdByUserName(String whiteUserName, String blackUserName) {
        return map.entrySet().stream()
                .filter(entry -> isMatchingWithUserName(whiteUserName, blackUserName, entry))
                .findFirst()
                .map(entry -> entry.getKey())
                .orElse(0);
    }

    private boolean isMatchingWithUserName(String whiteUserName, String blackUserName, Entry<Integer, GameDto> entry) {
        return entry.getValue().getWhiteUserName() == whiteUserName
                && entry.getValue().getBlackUserName() == blackUserName;
    }

    @Override
    public GameDto findById(int gameId) {
        return map.get(gameId);
    }

    @Override
    public void update(String state, int gameId) {
        GameDto gameDto = map.get(gameId);
        GameDto newGameDto = new GameDto(gameDto.getWhiteUserName(), gameDto.getBlackUserName(), state);
        map.put(gameId, newGameDto);
    }

    @Override
    public void deleteById(int gameId) {
        map.remove(gameId);
    }
}
