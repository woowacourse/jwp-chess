package chess.dao;

import chess.entity.RoomEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

public interface RoomDao {

    void insert(RoomEntity roomEntity);
    List<RoomEntity> findAll();
    void delete(RoomEntity roomEntity);
    RoomEntity findById(RoomEntity roomEntity);
    void updateById(RoomEntity roomEntity);

}
