package chess.dao.fake;

import chess.dao.RoomDao;
import chess.entity.RoomEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeRoomDao implements RoomDao {

    private final List<RoomEntity> rooms = new ArrayList<>();
    private int id = 0;

    @Override
    public int insert(final String name, final String password) {
        final RoomEntity roomEntity = new RoomEntity(id, name, password);
        rooms.add(roomEntity);
        id += 1;
        return 1;
    }

    @Override
    public List<RoomEntity> findAll() {
        return Collections.unmodifiableList(rooms);
    }
}
