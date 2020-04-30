package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChessRepository extends CrudRepository<ChessEntity, Long> {
    @Query("SELECT room_id FROM chess")
    List<Long> findIds();

    @Override
    Optional<ChessEntity> findById(Long roomId);

    @Query("SELECT * FROM chess where room_id = :roomId")
    ChessEntity findByRoomId(@Param("roomId") Long roomId);

    @Override
    ChessEntity save(ChessEntity entity);
}
