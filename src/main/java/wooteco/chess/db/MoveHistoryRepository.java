package wooteco.chess.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoveHistoryRepository extends CrudRepository<MoveHistory, Long> {

    @Modifying
    @Query("INSERT INTO move_history (room, moves, team, source_position, target_position) " +
            "VALUES (:roomId, (SELECT IFNULL(MAX(moves) + 1, 1) FROM move_history AS INNERTABLE WHERE room = :roomId), :team, :source, :target)")
    void addMoveHistory(Long roomId, String team, String source, String target);


    @Query("SELECT team FROM move_history WHERE room = :roomId ORDER BY moves DESC LIMIT 1")
    Optional<String> findLastTurn(Long roomId);
}
