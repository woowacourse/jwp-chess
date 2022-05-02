package chess.domain;

import java.util.List;

import chess.domain.game.Game;
import chess.repository.dto.game.GameStatus;

public interface ChessRepository {

    Game save(final Game game);

    Game findById(final Long gameId);

    List<GameStatus> findStatuses();

    Game update(final Game game);

    void remove(final Long gameId);
}
