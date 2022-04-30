package chess.fixture;

import static chess.util.HashUtils.hash;

import chess.dao.GameDao;
import chess.domain.auth.EncryptedAuthCredentials;
import chess.entity.FullGameEntity;
import chess.entity.GameEntity;
import chess.exception.InvalidAccessException;
import chess.exception.InvalidStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameDaoStub extends GameDao {

    public static final int TOTAL_GAME_COUNT = 5;
    public static final int RUNNING_GAME_COUNT = 4;

    private final Map<Integer, GameFullEntity> repository = new HashMap<>() {{
        put(1, new GameFullEntity(1, "진행중인_게임", hash("encrypted1"), hash("enemy1"), true));
        put(2, new GameFullEntity(2, "이미_존재하는_게임명", hash("encrypted2"), hash("enemy2"), true));
        put(3, new GameFullEntity(3, "종료된_게임", hash("encrypted3"), false));
        put(4, new GameFullEntity(4, "참여자가_있는_게임", hash("encrypted4"), hash("enemy4"), true));
        put(5, new GameFullEntity(5, "참여자가_없는_게임", hash("encrypted5"), true));
    }};
    private int autoIncrementId = repository.size();

    public GameDaoStub() {
        super(null);
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
            throw new InvalidAccessException(InvalidStatus.GAME_NOT_FOUND);
        }
        return new GameEntity(game.id, game.name, game.running);
    }

    @Override
    public FullGameEntity findFullDataById(int gameId) {
        GameFullEntity game = repository.get(gameId);
        if (game == null) {
            throw new InvalidAccessException(InvalidStatus.GAME_NOT_FOUND);
        }
        return new FullGameEntity(game.id, game.name, game.password, game.opponent_password, game.running);
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

    public boolean checkRunning(int gameId) {
        GameFullEntity game = repository.get(gameId);
        return game.running;
    }

    @Override
    public int saveAndGetGeneratedId(EncryptedAuthCredentials authCredentials) {
        validateNoDuplicate(authCredentials);
        autoIncrementId++;
        repository.put(autoIncrementId, new GameFullEntity(autoIncrementId,
                authCredentials.getName(), authCredentials.getPassword(), true));
        return autoIncrementId;
    }

    private void validateNoDuplicate(EncryptedAuthCredentials authCredentials) {
        boolean duplicateGameExists = repository.values().stream()
                .anyMatch(game -> game.name.equals(authCredentials.getName()));
        if (duplicateGameExists) {
            throw new IllegalArgumentException("게임을 생성하는데 실패하였습니다.");
        }
    }

    @Override
    public void saveOpponent(EncryptedAuthCredentials authCredentials) {
        GameFullEntity foundGame = repository.values().stream()
                .filter(game -> game.name.equals(authCredentials.getName()))
                .filter(game -> !game.password.equals(authCredentials.getPassword()))
                .filter(game -> game.opponent_password == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상대방 플레이어 저장에 실패하였습니다."));

        foundGame.opponent_password = authCredentials.getPassword();
    }

    @Override
    public void finishGame(int gameId) {
        if (!repository.containsKey(gameId)) {
            throw new InvalidAccessException(InvalidStatus.GAME_NOT_FOUND);
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
        return repository.values().stream()
                .filter(game -> game.name.equals(authCredentials.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));
    }

    private static class GameFullEntity {

        final int id;
        final String name;
        final String password;
        String opponent_password;
        boolean running;

        GameFullEntity(int id,
                       String name,
                       String password,
                       String opponent_password,
                       boolean running) {
            this.id = id;
            this.name = name;
            this.password = password;
            this.opponent_password = opponent_password;
            this.running = running;
        }

        GameFullEntity(int id,
                       String name,
                       String password,
                       boolean running) {
            this(id, name, password, null, running);
        }
    }
}
