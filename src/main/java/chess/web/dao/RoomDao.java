package chess.web.dao;

import chess.domain.board.Board;
import chess.domain.board.Turn;
import chess.domain.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomDao {

    Optional<Turn> findTurnById(Long id);

    void updateTurnById(Long id, String newTurn);

    Long save(String title, String password);

    Optional<Room> findById(Long id);

    void deleteById(Long id);

    List<Room> findAll();
}
