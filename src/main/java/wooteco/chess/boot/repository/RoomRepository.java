package wooteco.chess.boot.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import wooteco.chess.boot.entity.RoomEntity;

import java.util.List;

public interface RoomRepository extends CrudRepository<RoomEntity, Long> {

    @Override
    List<RoomEntity> findAll();

    @Query("SELECT id from room where room_number = :roomNumber limit 1")
    Long findIdByNumber(Long roomNumber);

    @Modifying
    @Query("DELETE from room where room_number = :roomNumber")
    void deleteByRoomNumber(Long roomNumber);
}
