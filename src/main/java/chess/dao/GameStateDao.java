package chess.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface GameStateDao {

    boolean hasPlayingGame();

    void saveState(final String state);

    void saveTurn(final String turn);

    String getGameState();

    String getTurn();

    void removeGameState();
}
