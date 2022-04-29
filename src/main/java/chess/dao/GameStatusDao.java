package chess.dao;

public interface GameStatusDao {

    void init(int gameId);

    void update(String nowStatus, String nextStatus, int gameId);

    String getStatus(int gameId);

    void reset(int gameId);
}
