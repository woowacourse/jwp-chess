package chess.web.dao;

import chess.board.Room;
import java.util.Optional;

public interface RoomDao {

    Long save(Long boardId, String title, String password);

    Optional<Room> findByBoardId(Long boardId);

    void delete(Long boardId, String password);
}
