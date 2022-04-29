package chess.dao.fake;

import chess.dao.TurnDao;

public class FakeTurnDao implements TurnDao {

    private String turn = "white";

    @Override
    public void init(int gameId) {
        turn = "white";
    }

    @Override
    public void update(String nowTurn, String nextTurn) {
        turn = nextTurn;
    }

    @Override
    public String getTurn(int gameId) {
        return turn;
    }

    @Override
    public void reset(int gameId) {
        turn = "white";
    }
}
