package chess.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface GameStateDao {

    boolean hasPlayingGame(int roomNumber);

    void saveState(int roomNumber, final String state);

    void saveTurn(int roomNumber, final String turn);

    String getGameState(int roomNumber);

    String getTurn(int roomNumber);

    void removeGameState(int roomNumber);
}
