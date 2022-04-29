package chess.dao.fake;

import chess.dao.GameStatusDao;
import chess.domain.GameStatus;
import java.util.HashMap;
import java.util.Map;

public class FakeGameStatusDao implements GameStatusDao {

    private Map<Integer, GameStatus> values = new HashMap<>();

    private GameStatus status = GameStatus.READY;

    @Override
    public void create(GameStatus data, int roomId) {
        values.put(roomId, data);
    }

    @Override
    public void update(String nowStatus, String nextStatus, int roomId) {
        values.replace(roomId, GameStatus.of(nextStatus));
    }

    @Override
    public String getStatus(int roomId) {
        return values.get(roomId).toString();
    }

    @Override
    public void reset(GameStatus data, int roomId) {
        values.remove(roomId);
        values.put(roomId, data);
    }
}
