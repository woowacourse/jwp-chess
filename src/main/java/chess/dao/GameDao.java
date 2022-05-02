package chess.dao;

import chess.domain.entity.Game;
import java.time.LocalDateTime;
import java.util.List;

public interface GameDao {
    boolean isExistGame();

    void save(String gameId, String lastTeamName, LocalDateTime createdAt);

    void createGame(String game_id,
              String roomName,
              String roomEncryptedPassword,
              String teamName,
              LocalDateTime createdAt);

    List<Game> findAll();

    Game findLastGame();

    void deleteAll();
}
