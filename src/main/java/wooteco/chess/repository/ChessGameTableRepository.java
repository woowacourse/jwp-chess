package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChessGameTableRepository extends CrudRepository<ChessGameTable, Long> {
    @Query("SELECT id FROM chess_game_table")
    List<Long> findRoomIds();
}
