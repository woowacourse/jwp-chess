package chess.model.repository;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ChessGameRepository extends CrudRepository<ChessGameEntity, Integer> {

    @Query("SELECT * FROM CHESS_GAME_TB WHERE ROOM_ENTITY = :ROOM_ENTITY")
    Iterable<ChessGameEntity> findAllByRoomId(@Param("ROOM_ENTITY") Integer roomId);


    @Query("SELECT * FROM CHESS_GAME_TB WHERE ID = :ID AND PROCEEDING_YN = 'Y'")
    Optional<ChessGameEntity> findProceedingById(@Param("ID") Integer gameId);
}
