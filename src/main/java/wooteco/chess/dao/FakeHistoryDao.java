package wooteco.chess.dao;

import wooteco.chess.domain.position.MovingPosition;

import java.util.*;

public class FakeHistoryDao implements HistoryDao {
    private final Map<Integer, MovingPosition> fakeHistoryDao;

    public FakeHistoryDao() {
        fakeHistoryDao = new LinkedHashMap<>();

        fakeHistoryDao.put(1, new MovingPosition("a2", "a4"));
        fakeHistoryDao.put(2, new MovingPosition("a7", "a6"));
        fakeHistoryDao.put(3, new MovingPosition("a4", "a5"));
        fakeHistoryDao.put(4, new MovingPosition("b7", "b5"));
    }

    @Override
    public List<MovingPosition> selectAll() {
        return Collections.unmodifiableList(
                new ArrayList<>(fakeHistoryDao.values())
        );
    }

    @Override
    public void clear() {
        fakeHistoryDao.clear();
    }

    @Override
    public void insert(MovingPosition movingPosition) {
        fakeHistoryDao.put(fakeHistoryDao.size() + 1, movingPosition);
    }
}
