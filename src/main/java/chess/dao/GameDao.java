package chess.dao;

import chess.controller.web.dto.game.GameResponseDto;
import chess.domain.game.Game;

public interface GameDao {

    Long saveGame(final Game game);

    GameResponseDto findGameById(final Long gameId);
}
