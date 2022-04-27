package chess.dao;

public interface GameDao {

    void startGame(int roomId);

    void endGame(int roomId);

    String getState(int roomId);
}
