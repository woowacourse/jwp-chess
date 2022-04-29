package chess.dao;

import java.util.List;

import chess.dto.response.ChessGameDto;
import chess.dto.response.RoomDto;

public interface GameDao {
    ChessGameDto getGame(int gameId);

    int createGame(String gameName, String gamePassword);

    void deleteGame(int gameId);

    void updateTurnToWhite(int gameId);

    void updateTurnToBlack(int gameId);

    List<RoomDto> inquireAllRooms();

    void checkCanDelete(int gameId, String inputPassword);
}
