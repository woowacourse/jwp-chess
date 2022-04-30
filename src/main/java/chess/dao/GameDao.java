package chess.dao;

public interface GameDao {

    void initializeState(int roomId);

    void updateStateEnd(int roomId);

    String getState(int roomId);

    void insertState(int roomId);
}
