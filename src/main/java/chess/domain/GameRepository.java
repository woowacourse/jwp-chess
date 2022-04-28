package chess.domain;

import java.util.Map;

import chess.domain.game.Game;

public interface GameRepository {

    Game save(final Game game);

    Game findById(final Long id);

    Map<Long, Boolean> findIdAndFinished();

    Game update(final Game game);

    void remove(final Long id);
}
