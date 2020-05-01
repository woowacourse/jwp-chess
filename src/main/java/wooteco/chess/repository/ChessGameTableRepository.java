package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChessGameTableRepository extends CrudRepository<ChessGameTable, Long> {
    @Query("SELECT id FROM chess_game_table")
    List<Long> findRoomIds();
}
