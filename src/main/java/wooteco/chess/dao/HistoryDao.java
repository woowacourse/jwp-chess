package wooteco.chess.dao;

import wooteco.chess.domain.position.MovingPosition;

import java.sql.SQLException;
import java.util.List;

public interface HistoryDao {
    void insert(MovingPosition movingPosition) throws SQLException;

    List<MovingPosition> selectAll() throws SQLException;

    void clear() throws SQLException;
}
