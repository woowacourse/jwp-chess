package chess.dao;

import chess.dao.dto.game.GameDto;
import chess.domain.game.Game;

public interface GameDao {

    Long save(final Game game);

    GameDto findById(final Long gameId);
}
