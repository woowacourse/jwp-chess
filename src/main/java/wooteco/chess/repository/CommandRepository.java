package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.chess.dto.Commands;

import java.util.List;

public interface CommandRepository extends CrudRepository<Commands, Long> {
    @Override
    <S extends Commands> S save(S entity);

    @Override
    void deleteAll();

    @Override
    List<Commands> findAll();

    @Query("SELECT * FROM commands WHERE room_number = :roomNumber")
    List<Commands> findAllByRoomNumber(@Param("roomNumber") Long roomNumber);

}
