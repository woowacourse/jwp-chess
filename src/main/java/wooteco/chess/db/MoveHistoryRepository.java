package wooteco.chess.db;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveHistoryRepository extends CrudRepository<MoveHistory, String> {
    @Modifying
    @Query("INSERT INTO move_history (game_id, moves, team, source_position, target_position) " +
        "VALUES (:gameId, (SELECT IFNULL(MAX(moves) + 1, 1) FROM move_history AS INNERTABLE WHERE game_id = :gameId), :team, :source, :target)")
    void addMoveHistory(@Param("gameId") String gameId, @Param("team") String team, @Param("source") String source,
        @Param("target") String target);

    @Query("SELECT team FROM move_history WHERE game_id = :gameId ORDER BY moves DESC LIMIT 1")
    Optional<String> findLastTurn(@Param("gameId") String gameId);

    @Override
    void deleteById(String gameId);
}
