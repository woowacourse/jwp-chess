package chess.dao;

import chess.dao.entity.GameEntity;
import chess.domain.GameState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

public class FakeGameDao extends GameDao {

    private final Map<Long, GameEntity> games;
    private Long autoIncrementId = 1L;

    public FakeGameDao() {
        super(Mockito.mock(DataSource.class));
        this.games = new HashMap<>();
    }

    @Override
    public Long save(GameEntity gameEntity) {
        checkDataIntegrityViolation(gameEntity);
        checkDuplicatedName(gameEntity.getName());
        gameEntity = GameEntity.toFind(autoIncrementId, gameEntity.getName(), gameEntity.getPassword(),
                gameEntity.getSalt(), gameEntity.getState());
        games.put(autoIncrementId, gameEntity);
        return autoIncrementId++;
    }

    private void checkDataIntegrityViolation(GameEntity gameEntity) {
        if (gameEntity.getName() == null || gameEntity.getPassword() == null || gameEntity.getSalt() == null
                || gameEntity.getState() == null) {
            throw new DataIntegrityViolationException("올바르지 않은 요청값입니다.");
        }
    }

    private void checkDuplicatedName(String name) {
        games.values()
                .forEach(gameEntity -> {
                    if (gameEntity.getName().equals(name)) {
                        throw new DuplicateKeyException("이미 존재하는 게임입니다.");
                    }
                });
    }

    @Override
    public List<GameEntity> findAll() {
        return new ArrayList<>(games.values());
    }

    @Override
    public GameEntity findById(Long id) {
        if (!games.containsKey(id)) {
            throw new EmptyResultDataAccessException("존재하지 않는 게임입니다.", 1);
        }
        return games.get(id);
    }

    @Override
    public void updateState(Long id, GameState state) {
        if (!games.containsKey(id)) {
            throw new EmptyResultDataAccessException("존재하지 않는 게임입니다.", 1);
        }
        GameEntity savedEntity = games.get(id);
        GameEntity updatedEntity = GameEntity.toFind(savedEntity.getId(), savedEntity.getName(),
                savedEntity.getPassword(),
                savedEntity.getSalt(), state);
        games.put(id, updatedEntity);
    }

    @Override
    public void delete(Long id) {
        games.remove(id);
    }
}
