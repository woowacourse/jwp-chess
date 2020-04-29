package wooteco.chess.domain.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wooteco.chess.domain.entity.ChessGameEntity;

import java.util.List;

@Repository
public interface ChessRepository extends CrudRepository<ChessGameEntity, Integer> {
    @Query("SELECT id FROM chess_recode")
    List<Integer> selectAll();
}
