package chess.dao;

import chess.entity.SquareEntity;
import java.util.List;
import java.util.Optional;

public interface SquareDao {
    void saveAll(List<SquareEntity> squares, long roomId);

    List<SquareEntity> findByRoomId(long roomId);

    Optional<SquareEntity> findByRoomIdAndPosition(long roomId, String position);

    void update(long roomId, String position, String piece);

    void removeAll(long roomId);
}
