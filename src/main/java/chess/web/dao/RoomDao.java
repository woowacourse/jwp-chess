package chess.web.dao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;

public interface RoomDao {

    int save(RoomName name, RoomPassword password);
}
