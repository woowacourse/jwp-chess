package chess.dao;

import chess.dao.dto.game.GameDto;
import chess.domain.game.Game;

public interface GameDao {

    Long saveGame(final Game game);

    GameDto findGameById(final Long gameId);
}
