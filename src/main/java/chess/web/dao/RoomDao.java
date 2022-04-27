package chess.web.dao;

import chess.board.Room;
import java.util.List;
import java.util.Optional;

public interface RoomDao {

    Long save(Long boardId, String title, String password);

    Optional<Room> findByBoardId(Long boardId);

    List<Room> findAll();

    void delete(Long boardId, String password);
}
