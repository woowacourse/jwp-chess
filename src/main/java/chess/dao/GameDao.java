package chess.dao;

import chess.domain.game.GameId;
import chess.domain.piece.PieceColor;

public interface GameDao {

    void createGame(GameId gameId);

    void deleteGame(GameId gameId);

    void updateTurnToWhite(GameId gameId);

    void updateTurnToBlack(GameId gameId);

    PieceColor getCurrentTurn(GameId gameId);
}
