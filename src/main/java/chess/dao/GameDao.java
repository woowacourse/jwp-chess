package chess.dao;

import chess.domain.game.GameId;
import chess.dto.response.ChessGameDto;

public interface GameDao {
    ChessGameDto getGame(GameId gameId);

    void createGame(GameId gameId);

    void deleteGame(GameId gameId);

    void updateTurnToWhite(GameId gameId);

    void updateTurnToBlack(GameId gameId);
}
