package chess.dao;

import chess.entity.Movement;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementDao {

    void save(final Movement movement);

    List<Movement> findByChessName(final String name);
}
