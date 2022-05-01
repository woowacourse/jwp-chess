package chess.dao;

import chess.domain.entity.Game;
import java.time.LocalDateTime;

public interface GameDao {
    boolean isExistGame();

    void save(String gameId, String lastTeamName, LocalDateTime createdAt);

    Game findLastGame();

    void deleteAll();
}
