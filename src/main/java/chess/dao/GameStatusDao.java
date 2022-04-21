package chess.dao;

public interface GameStatusDao {

    void init();

    void update(String nowStatus, String nextStatus);

    String getStatus();

    void reset();
}
