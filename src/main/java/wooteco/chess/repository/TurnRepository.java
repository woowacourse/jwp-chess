package wooteco.chess.repository;

import chess.dto.TurnDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wooteco.chess.entity.Turn;

@Repository
public interface TurnRepository extends CrudRepository<Turn, Long> {
    @Query("SELECT teamName FROM turn LIMIT 1")
    TurnDto findFirst();

    @Query("UPDATE turn SET teamName = :turn")
    void update(@Param("turn") String turn);

//    @Query("DELETE FROM turn")
//    void deleteTurn();
}
