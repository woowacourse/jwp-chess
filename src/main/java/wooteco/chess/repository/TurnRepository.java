package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wooteco.chess.entity.TurnEntity;

@Repository
public interface TurnRepository extends CrudRepository<TurnEntity, Long> {
    @Query("SELECT teamName FROM turn LIMIT 1")
    String findFirst();

    @Modifying
    @Query("UPDATE turn SET teamName = :turn")
    void update(@Param("turn") String turn);

    @Modifying
    @Query("INSERT INTO turn(teamName) VALUES (:teamName)")
    void insert(@Param("teamName") String turn);
}
