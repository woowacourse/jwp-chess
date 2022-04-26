package chess.database.dao;

import java.util.List;
import java.util.Optional;

import chess.database.dto.GameStateDto;

public interface GameDao {
    Optional<GameStateDto> findGameById(Long id);

    Long saveGame(GameStateDto gameStateDto, String roomName, String password);

    void updateState(GameStateDto gameStateDto, Long id);

    void removeGame(Long id);

    List<String> readGames();

    Optional<GameStateDto> findGameByRoomName(String roomName);
}
