package chess.repository;

import chess.dao.MovementDao;
import chess.entity.Movement;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JDBCMovementDao implements MovementDao {
    @Override
    public void save(Movement movement) {

    }

    @Override
    public List<Movement> findByChessName(String name) {
        return null;
    }
}
