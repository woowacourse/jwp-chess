package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChessRoomRepository extends CrudRepository<ChessRoom, Long> {
    @Override
    <S extends ChessRoom> S save(S entity);

    @Override
    List<ChessRoom> findAll();

//    @Query("SELECT move_command.command FROM chess_room JOIN move_command ON move_command.chess_room = chess_room.room_id WHERE chess_room.room_id = :room_id")
//    List<MoveCommand> findByRoomId(@Param("room_id") Long roomId);
}
