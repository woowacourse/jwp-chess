package chess.dao;

import chess.controller.dto.response.GameIdentifiers;
import chess.domain.GameState;
import java.util.List;
import java.util.Optional;

public interface GameDao {

    void save(long id, String name, String password, String salt);

    List<GameIdentifiers> findAllGames();

    Optional<String> findName(long id);

    Optional<String> findPassword(long id);

    Optional<String> findSalt(long id);

    Optional<GameState> findState(long id);

    void updateState(long id, GameState gameState);

    void delete(long id);
}
