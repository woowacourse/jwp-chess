package chess.dao.fake;

import chess.dao.TurnDao;
import chess.domain.piece.Team;

public class FakeTurnDao implements TurnDao {

    private String turn = "white";

    @Override
    public void init(String data) {
        turn = data;
    }

    @Override
    public void update(String nowTurn, String nextTurn) {
        turn = nextTurn;
    }

    @Override
    public String getTurn() {
        return turn;
    }

    @Override
    public void reset(Team data) {
        turn = data.toString();
    }
}
