package chess.domain.repository.game;

import chess.domain.manager.Game;

public interface GameRepository {
    Long save(Game game);

    Game findById(Long id);

    void update(Game game);
}
