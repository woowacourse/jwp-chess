package chess.service.fixture;

import chess.dao.GameDao;
import chess.domain.auth.EncryptedAuthCredentials;
import chess.entity.GameEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class GameDaoStub extends GameDao {

    private int autoIncrementId = 3;
    private final Map<Integer, GameFullEntity> repository = new HashMap<>() {{
        put(1, new GameFullEntity(1, "진행중인_게임", "encrypted1", true));
        put(2, new GameFullEntity(2, "이미_존재하는_게임명", "encrypted2", true));
        put(3, new GameFullEntity(3, "종료된_게임", "encrypted3", false));
    }};

    public GameDaoStub(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<GameEntity> findAll() {
        return repository.values().stream()
                .map(game -> new GameEntity(game.id, game.name, game.running))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public GameEntity findById(int gameId) {
        GameFullEntity game = repository.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("존재하지 않는 게임입니다.");
        }
        return new GameEntity(game.id, game.name, game.running);
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
                .filter(value -> value.running)
                .count();
    }

    @Override
    public int saveAndGetGeneratedId(EncryptedAuthCredentials authCredentials) {
        autoIncrementId++;
        repository.put(autoIncrementId, new GameFullEntity(autoIncrementId,
                authCredentials.getName(), authCredentials.getPassword(), true));
        return autoIncrementId;
    }

    @Override
    public void finishGame(int gameId) {
        if (!repository.containsKey(gameId)) {
            throw new IllegalArgumentException("존재하지 않는 게임입니다.");
        }
        repository.get(gameId).running = false;
    }

    @Override
    public void deleteGame(EncryptedAuthCredentials authCredentials) {
        GameFullEntity game = findGameByName(authCredentials);
        if (game.running) {
            throw new IllegalArgumentException("아직 진행 중인 게임입니다.");
        }
        repository.remove(game.id);
    }

    private GameFullEntity findGameByName(EncryptedAuthCredentials authCredentials) {
        for (GameFullEntity game : repository.values()) {
            if (game.name.equals(authCredentials.getName())) {
                return game;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 게임입니다.");
    }

    private static class GameFullEntity {

        int id;
        String name;
        String password;
        boolean running;

        GameFullEntity(int id, String name, String password, boolean running) {
            this.id = id;
            this.name = name;
            this.password = password;
            this.running = running;
        }
    }
}
