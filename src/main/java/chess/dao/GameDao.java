package chess.dao;

import chess.domain.db.Game;

public interface GameDao {
    boolean isExistGame();

    void save(String gameId, String lastTeamName);

    Game findLastGame();

    void deleteAll();
}
