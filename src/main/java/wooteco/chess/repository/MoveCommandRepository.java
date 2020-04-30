package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoveCommandRepository extends CrudRepository<MoveCommand, Long> {
    @Override
    List<MoveCommand> findAll();

    @Query("SELECT * FROM move_command WHERE chess_room = :room_id")
    List<MoveCommand> findAllByRoomId(@Param("room_id") Long roomId);
}
