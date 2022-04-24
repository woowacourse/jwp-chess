package chess.dao;

public interface TurnDao {

    void init();

    void update(String nowTurn, String nextTurn);

    String getTurn();

    void reset();
}
