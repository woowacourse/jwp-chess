package chess.dao;

import chess.entity.BoardEntity;
import java.util.List;

public interface BoardDao {

    List<BoardEntity> getBoard();

    void updatePosition(final BoardEntity board);

    void updateBatchPositions(final List<BoardEntity> board);

    BoardEntity insert(final BoardEntity board);

    void batchInsert(final List<BoardEntity> boards);
}
