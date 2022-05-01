package chess.web.service.fakedao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dao.RoomDao;
import java.util.HashMap;
import java.util.Map;

public class FakeRoomDao implements RoomDao {

    Map<Integer, RoomName> map = new HashMap<>();

    @Override
    public int save(RoomName name, RoomPassword password) {
        map.put(1, name);
        return 1;
    }
}
