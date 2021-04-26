package chess.dao;

import chess.dao.dto.game.GameDto;
import chess.domain.entity.Game;

public interface GameDao {

    Long save(final GameDto game);

    GameDto findById(final Long gameId);

    Long update(GameDto gameDto);
}
