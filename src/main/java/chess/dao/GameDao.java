package chess.dao;

import java.util.List;

import chess.dto.response.ChessGameDto;
import chess.entity.Room;

public interface GameDao {
    ChessGameDto getGame(int gameId);

    int createGameAndGetId(String gameName, String gamePassword);

    void deleteGame(int gameId);

    void updateTurnToWhite(int gameId);

    void updateTurnToBlack(int gameId);

    List<Room> inquireAllRooms();

    String getPasswordById(int gameId);
}
