package chess.dao;

import java.util.List;
import java.util.Optional;

import chess.entity.Square;

public interface SquareDao {
    void saveAll(List<Square> squares, long roomId);

    List<Square> findByRoomId(long roomId);

    Optional<Square> findByRoomIdAndPosition(long roomId, String position);

    void update(long roomId, String position, String piece);

    void removeAll(long roomId);
}
