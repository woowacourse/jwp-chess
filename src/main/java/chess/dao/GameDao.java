package chess.dao;

import java.util.List;

import chess.domain.piece.PieceColor;
import chess.entity.Room;

public interface GameDao {
    PieceColor getGameTurn(int gameId);

    int createGameAndGetId(String gameName, String gamePassword);

    void deleteGame(int gameId);

    void updateTurnToWhite(int gameId);

    void updateTurnToBlack(int gameId);

    List<Room> inquireAllRooms();

    String getPasswordById(int gameId);
}
