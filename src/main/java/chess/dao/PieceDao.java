package chess.dao;

import java.util.List;
import java.util.Optional;

public interface PieceDao<T> {

    T save(T piece);

    Optional<T> findByPositionId(int positionId);

    int deleteByPositionId(int positionId);

    List<T> getAllByBoardId(int boardId);

    void saveAll(List<T> pieces);

    int updatePiece(T source, T piece);
}
