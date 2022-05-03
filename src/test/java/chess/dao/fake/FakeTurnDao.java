package chess.dao.fake;

import chess.dao.TurnDao;
import chess.domain.piece.Team;
import java.util.HashMap;
import java.util.Map;

public class FakeTurnDao implements TurnDao {

    private Map<Integer, String> values = new HashMap<>();


    @Override
    public void create(Team data, int roomId) {
        values.put(roomId, data.toString());
    }

    @Override
    public void update(String nowTurn, String nextTurn, int roomId) {
        values.replace(roomId, nowTurn, nextTurn);
    }

    @Override
    public String getTurn(int roomId) {
        return values.get(roomId);
    }

    @Override
    public void reset(Team data, int roomId) {
        values.put(roomId, data.toString());
    }
}
