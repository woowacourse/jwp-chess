package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.MoveEntity;

public interface MoveRepository extends CrudRepository<MoveEntity, Integer> {
    @Modifying
    @Query("delete from move where game = :gameId")
    void deleteAllByGameId(@Param("gameId") int gameId);

    @Query("select * from move where game = :gameId")
    List<MoveEntity> findAllByGameId(@Param("gameId") int gameId);
}
