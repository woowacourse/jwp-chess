package chess.service.fixture;

import chess.dao.GameDao;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class GameDaoStub extends GameDao {

    private static final String DATABASE_ACCESS_ATTEMPT_EXCEPTION_MESSAGE = "테스트 더블에서 game 테이블에 접근시도하였습니다.";

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
    public int saveAndGetGeneratedId() {
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

    @Override
    protected String addTable(String sql) {
        throw new UnsupportedOperationException(DATABASE_ACCESS_ATTEMPT_EXCEPTION_MESSAGE);
    }
}
