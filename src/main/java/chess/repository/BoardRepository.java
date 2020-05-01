package chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends CrudRepository<BoardEntity, Integer> {

    @Query("SELECT * FROM CHESS_BOARD_TB WHERE CHESS_GAME_ENTITY = :CHESS_GAME_ENTITY")
    Iterable<BoardEntity> findAllByGameId(@Param("CHESS_GAME_ENTITY") Integer gameId);
}
