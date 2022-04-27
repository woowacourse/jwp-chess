package chess.service.fixture;

import chess.dao.GameDao;
import chess.dto.CreateGameRequest;
import chess.dto.GameInfoDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class GameDaoStub extends GameDao {

    private int autoIncrementId = 3;
    private final Map<Integer, Boolean> repository = new HashMap<>() {{
        put(1, true);
        put(2, true);
        put(3, false);
    }};

    public GameDaoStub(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public List<GameInfoDto> selectAll() {
        return repository.entrySet()
                .stream()
                .map(entry -> new GameInfoDto(entry.getKey(), "title", "password", entry.getValue()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public GameInfoDto findById(int id) {
        return new GameInfoDto(id, "title", "password", repository.get(id));
    }

    @Override
    public int delete(int id) {
        if (repository.remove(id) != null) {
            return 0;
        }
        return 1;
    }

    @Override
    public int saveAndGetGeneratedId(CreateGameRequest createGameRequest) {
        autoIncrementId++;
        repository.put(autoIncrementId, true);
        return autoIncrementId;
    }

    @Override
    public void finishGame(int gameId) {
        repository.put(gameId, false);
    }

    @Override
    public boolean checkById(int gameId) {
        return repository.containsKey(gameId);
    }

    @Override
    public int countAll() {
        return repository.values().size();
    }

    @Override
    public int countRunningGames() {
        return (int) repository.values()
                .stream()
                .filter(value -> value)
                .count();
    }
}
