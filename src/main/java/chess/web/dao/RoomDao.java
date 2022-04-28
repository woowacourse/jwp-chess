package chess.web.dao;

import chess.board.Room;
import java.util.List;
import java.util.Optional;

public interface RoomDao {

    Long save(String turn, String title, String password);

    Long updateTurnById(Long id, String turn);

    Optional<Room> findById(Long id);

    List<Room> findAll();

    void delete(Long id, String password);
}
