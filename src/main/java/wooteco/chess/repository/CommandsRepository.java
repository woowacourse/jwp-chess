package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandsRepository extends CrudRepository<Commands, Long> {
    @Override
    <S extends Commands> S save(S entity);

    @Override
    void deleteAll();

    @Override
    List<Commands> findAll();

    @Query("SELECT * FROM Commands WHERE RoomID = :RoomID")
    List<Commands> findByRoomId(@Param("RoomID") Long roomId);
}
