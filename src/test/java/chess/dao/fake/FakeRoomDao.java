package chess.dao.fake;

import chess.dao.RoomDao;
import chess.entity.RoomEntity;
import java.util.List;

public class FakeRoomDao implements RoomDao {

    @Override
    public int insert(String name, String password) {
        return 0;
    }

    @Override
    public List<RoomEntity> findAll() {
        return null;
    }
}
