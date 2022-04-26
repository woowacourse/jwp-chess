package chess.dao;

import chess.domain.GameState;
import java.util.List;
import java.util.Optional;

public interface GameDao {

    void save(long id);

    List<Long> findAllGameId();

    Optional<GameState> load(long id);

    void updateState(long id, GameState gameState);

    void delete(long id);
}
