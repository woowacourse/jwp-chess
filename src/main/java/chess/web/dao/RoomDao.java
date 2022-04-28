package chess.web.dao;

import chess.domain.entity.Room;
import chess.domain.board.Board;
import chess.domain.board.Turn;

import java.util.List;
import java.util.Optional;

public interface RoomDao {

    Optional<Turn> findTurnById(Long id);

    void updateTurnById(Long id, String newTurn);

    Long save(String title, String password);

    Optional<Board> findById(Long id);

    void deleteById(Long id);

    List<Room> findAll();
}
