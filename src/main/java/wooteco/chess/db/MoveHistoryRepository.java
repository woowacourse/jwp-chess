package wooteco.chess.db;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wooteco.chess.domains.piece.PieceColor;
import wooteco.chess.domains.position.Position;

import java.util.Optional;

@Repository
public interface MoveHistoryRepository extends CrudRepository<MoveHistory, String> {
    @Query("INSERT INTO move_history (game_id, moves, team, source_position, target_position) " +
            "VALUES (:gameId, (SELECT IFNULL(MAX(moves) + 1, 1) FROM move_history AS INNERTABLE WHERE game_id = :gameId), :team, :source, :target)")
    void addMoveHistory(String gameId, PieceColor team, Position source, Position target);

    @Override
    Optional<MoveHistory> findById(String gameId);

    @Override
    void deleteById(String gameId);
}
