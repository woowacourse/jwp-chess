package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.MoveEntity;

public interface MoveRepository extends CrudRepository<MoveEntity, Integer> {
    @Modifying
    @Query("DELETE FROM move WHERE game = :gameId")
    void deleteAllByGameId(@Param("gameId") String gameId);

    @Query("SELECT * FROM move WHERE game = :gameId")
    List<MoveEntity> findAllByGameId(@Param("gameId") String gameId);
}
