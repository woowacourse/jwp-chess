package chess.database.dao;

import java.util.Map;
import java.util.Optional;

import chess.database.dto.GameStateDto;

public interface GameDao {
    Optional<GameStateDto> findGameById(Long id);

    Long saveGame(GameStateDto gameStateDto, String roomName, String password);

    void updateState(GameStateDto gameStateDto, Long id);

    void removeGame(Long id);

    Map<Long, String> readGameRoomIdAndNames();

    Optional<GameStateDto> findGameByRoomName(String roomName);

    Optional<String> findPasswordById(Long roomId);
}
