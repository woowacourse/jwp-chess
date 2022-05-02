package chess.dao;

public interface TurnDao {

    void init(int gameId);

    void update(String nowTurn, String nextTurn);

    String getTurn(int gameId);

    void reset(int gameId);
}
