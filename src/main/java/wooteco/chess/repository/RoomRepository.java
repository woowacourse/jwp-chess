package wooteco.chess.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.chess.entity.Room;

public interface RoomRepository extends CrudRepository<Room, Long> {

    @Override
    List<Room> findAll();

    @Query("SELECT * FROM room JOIN "
        + "(SELECT game.room AS game_room, game.turn AS game_turn FROM game) AS tmp "
        + "ON game_room = room.id "
        + "WHERE room.name = :name")
    Optional<Room> findByName(@Param("name") String name);
}
