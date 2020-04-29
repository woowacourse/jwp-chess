package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.ChessGameEntity;

public interface ChessGameRepository extends CrudRepository<ChessGameEntity, Integer> {

    @Query("SELECT id FROM chess_game")
    List<Integer> findAllGameId();
}
