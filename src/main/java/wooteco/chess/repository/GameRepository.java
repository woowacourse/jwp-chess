package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.GameEntity;

public interface GameRepository extends CrudRepository<GameEntity, String> {
    @Modifying
    @Query("insert into game (id, white, black) VALUES (:id, :white, :black)")
    int addGame(@Param("id") String id, @Param("white") int white, @Param("black") int black);
}
