package chess.dao;

public interface GameDao {

    void updateState(int roomId, String state);

    String getState(int roomId);

    void insertState(int roomId, String state);
}
