package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChessRepository extends CrudRepository<ChessEntity, Long> {
    @Modifying
    @Query("ALTER TABLE chess AUTO_INCREMENT = 1")
    void initTable();

    @Query("SELECT room_id FROM chess")
    List<Long> findAllIds();

    @Override
    Optional<ChessEntity> findById(Long roomId);

    @Modifying
    @Query("UPDATE chess SET board = :board, is_white = :isWhite WHERE room_id = :roomId")
    void update(@Param("roomId") Long roomId, @Param("board") String board, @Param("isWhite") boolean isWhite);

    @Query("SELECT title FROM chess WHERE room_id = :roomId")
    String findTitleById(@Param("roomId") Long roomId);
}
