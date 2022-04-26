package chess.dao;

import chess.controller.dto.GameDto;
import chess.domain.GameState;
import java.util.Optional;

public interface GameDao {

    void save(String name, String password);

    Optional<GameDto> load(String name, String password);

    void updateState(long id, GameState gameState);

    void delete(long id);
}
