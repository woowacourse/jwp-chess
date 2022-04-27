package chess.dao;

import chess.controller.dto.response.GameIdentifiers;
import chess.domain.GameState;
import java.util.List;
import java.util.Optional;

public interface GameDao {

    void save(Long id, String name, String password, String salt);

    List<GameIdentifiers> findAllGames();

    Optional<String> findName(Long id);

    Optional<String> findPassword(Long id);

    Optional<String> findSalt(Long id);

    Optional<GameState> findState(Long id);

    void updateState(Long id, GameState gameState);

    void delete(Long id);
}
