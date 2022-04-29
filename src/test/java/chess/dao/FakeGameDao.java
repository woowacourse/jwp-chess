package chess.dao;

import chess.controller.dto.response.GameIdentifiers;
import chess.domain.GameState;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;

public class FakeGameDao extends GameDao {

    private final Map<Long, String> names;
    private final Map<Long, String> passwords;
    private final Map<Long, String> salts;
    private final Map<Long, GameState> states;
    private Long autoIncrementId = 1L;

    public FakeGameDao() {
        super(Mockito.mock(DataSource.class));
        this.names = new HashMap<>();
        this.passwords = new HashMap<>();
        this.salts = new HashMap<>();
        this.states = new HashMap<>();
    }

    @Override
    public Long save(String name, String password, String salt, GameState state) {
        if (names.containsValue(name)) {
            throw new DuplicateKeyException("");
        }
        names.put(autoIncrementId, name);
        passwords.put(autoIncrementId, password);
        salts.put(autoIncrementId, salt);
        states.put(autoIncrementId, GameState.READY);
        return autoIncrementId++;
    }

    @Override
    public List<GameIdentifiers> findAllGames() {
        return names.keySet().stream()
                .map(id -> new GameIdentifiers(id, names.get(id)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> findName(Long id) {
        if (names.containsKey(id)) {
            return Optional.of(names.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> findPassword(Long id) {
        if (passwords.containsKey(id)) {
            return Optional.of(passwords.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> findSalt(Long id) {
        if (salts.containsKey(id)) {
            return Optional.of(salts.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<GameState> findState(Long id) {
        if (states.containsKey(id)) {
            return Optional.of(states.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void updateState(Long id, GameState gameState) {
        states.put(id, gameState);
    }

    @Override
    public void delete(Long id) {
        names.remove(id);
        passwords.remove(id);
        salts.remove(id);
        states.remove(id);
    }


}
