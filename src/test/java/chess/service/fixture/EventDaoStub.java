package chess.service.fixture;

import chess.dao.EventDao;
import chess.entity.EventEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EventDaoStub extends EventDao {

    private final Map<Integer, List<EventEntity>> repository = new HashMap<>() {{
        put(1, new ArrayList<>(List.of(new EventEntity("INIT", "")
                , new EventEntity("MOVE", "e2 e4")
                , new EventEntity("MOVE", "d7 d5")
                , new EventEntity("MOVE", "f1 b5")
        )));
        put(2, new ArrayList<>(List.of(new EventEntity("INIT", "")
                , new EventEntity("MOVE", "e2 e4")
                , new EventEntity("MOVE", "d7 d5")
                , new EventEntity("MOVE", "f1 b5")
                , new EventEntity("MOVE", "a7 a5")
        )));
        put(3, new ArrayList<>(List.of(new EventEntity("INIT", "")
                , new EventEntity("MOVE", "e2 e4")
                , new EventEntity("MOVE", "d7 d5")
                , new EventEntity("MOVE", "f1 b5")
                , new EventEntity("MOVE", "a7 a5")
                , new EventEntity("MOVE", "b5 e8")
        )));
    }};

    public EventDaoStub(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public List<EventEntity> findAllByGameId(int gameId) {
        if (repository.containsKey(gameId)) {
            return repository.get(gameId);
        }
        return List.of();
    }

    @Override
    public void save(int gameId, EventEntity event) {
        if (repository.containsKey(gameId)) {
            repository.get(gameId).add(event);
            return;
        }
        repository.put(gameId, new ArrayList<>(List.of(event)));
    }
}
