package chess.repository;

import chess.web.dao.PlayerRepository;
import chess.web.dto.PlayerDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakePlayerRepository implements PlayerRepository {

    private final Map<Integer, String> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public void save(String name) {
        autoIncrementId++;
        database.put(autoIncrementId, name);
    }

    @Override
    public Optional<PlayerDto> find(String name) {
        return database.keySet().stream()
                .filter(key -> database.get(key).equals(name))
                .map(key -> new PlayerDto(key, database.get(key)))
                .findAny();
    }
}
