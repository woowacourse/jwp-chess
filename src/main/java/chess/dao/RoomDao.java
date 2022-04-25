package chess.dao;

import chess.entity.RoomEntity;
import java.util.List;

public interface RoomDao {

    int insert(String name, String password);

    List<RoomEntity> findAll();
}
