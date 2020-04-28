package chess.model.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends CrudRepository<Room, Integer> {

    @Query("SELECT * FROM ROOM_TB WHERE USED_YN = :USED_YN")
    Iterable<Room> findAllByUsedYNEquals(@Param("USED_YN") String usedYN);
}
