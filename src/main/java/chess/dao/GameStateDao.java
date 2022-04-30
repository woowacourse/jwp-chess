package chess.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface GameStateDao {

    boolean hasPlayingGame(final int roomNumber);

    void saveState(final int roomNumber, final String state);

    void saveTurn(final int roomNumber, final String turn);

    String getGameState(final int roomNumber);

    String getTurn(final int roomNumber);

    void removeGameState(final int roomNumber);
}
