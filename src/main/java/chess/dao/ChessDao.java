package chess.dao;

import chess.dto.GameRoomDto;
import chess.dto.MoveRequestDto;
import chess.dto.NewGameRequest;
import java.util.List;
import java.util.Map;

public interface ChessDao {
    Map<String, String> getBoardByGameId(String gameId);

    void move(MoveRequestDto moveRequestDto);

    String getTurnByGameId(String gameId);

    int createNewGame(NewGameRequest newGameRequest);

    List<GameRoomDto> findGamesOnPlay();

    GameRoomDto findGameById(int id);

    int deleteGameById(int id);

    void setFinishedById(String gameId);

    List<GameRoomDto> findGames();
}
