package chess.repository;

import chess.entity.BoardEntity;
import java.util.List;

public interface BoardRepository {

    List<BoardEntity> findBoardByRoomId(final Long roomId);

    BoardEntity updatePosition(final BoardEntity board);

    void batchUpdatePositions(final List<BoardEntity> board);

    BoardEntity insert(final BoardEntity board);

    void batchInsert(final List<BoardEntity> boards);

    BoardEntity findBoardByRoomIdAndPosition(final Long roomId, final String position);

    void delete(Long roomId);
}
