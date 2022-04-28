package chess.dao;

import chess.dto.response.ChessGameDto;

public interface GameDao {
    ChessGameDto getGame(int gameId);

    int createGame(String gameName, String gamePassword);

    void deleteGame(int gameId);

    void updateTurnToWhite(int gameId);

    void updateTurnToBlack(int gameId);
}
