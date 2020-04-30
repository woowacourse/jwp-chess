package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
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

    @Override
    ChessEntity save(ChessEntity entity);

    @Modifying
    @Query("UPDATE chess SET board = :board, is_white = :isWhite WHERE room_id = :roomId")
    void update(@Param("roomId") Long roomId, @Param("board") String board, @Param("isWhite") boolean isWhite);
}
