package chess.repository.dao;

import chess.repository.entity.RoomEntity;
import java.util.List;

public interface RoomDao {

    long save(RoomEntity roomEntity);

    RoomEntity findById(long id);

    List<RoomEntity> findAll();

    long deleteById(long id);

    long deleteAll();

    void changeTurnById(long id);

    void finishById(long id);
}
