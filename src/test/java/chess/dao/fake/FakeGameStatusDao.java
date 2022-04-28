package chess.dao.fake;

import chess.dao.GameStatusDao;
import chess.domain.GameStatus;

public class FakeGameStatusDao implements GameStatusDao {

    private GameStatus status = GameStatus.READY;

    @Override
    public void init(int gameId) {
        status = GameStatus.READY;
    }

    @Override
    public void update(String nowStatus, String nextStatus, int gameId) {
        status = GameStatus.valueOf(nextStatus);
    }

    @Override
    public String getStatus(int id) {
        return status.name();
    }

    @Override
    public void reset() {
        status = GameStatus.READY;
    }
}
