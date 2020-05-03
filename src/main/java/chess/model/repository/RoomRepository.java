package chess.model.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends CrudRepository<RoomEntity, Integer> {

    @Query("SELECT * FROM ROOM_TB WHERE USED_YN = :USED_YN")
    Iterable<RoomEntity> findAllByUsedYNEquals(@Param("USED_YN") String usedYN);
}
