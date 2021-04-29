package chess.dao;

import chess.domain.entity.History;
import chess.exception.NoHistoryException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistoryDAO {
    private final JdbcTemplate jdbcTemplate;

    public HistoryDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createHistory(final String roomId, final String startPoint, final String endPoint) {
        String query = "INSERT INTO history (room_id, start_position, end_position) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, roomId, startPoint, endPoint);
    }

    public List<History> allHistoryByRoomId(final String roomId) {
        String query = "SELECT start_position, end_position FROM history WHERE room_id = ? ORDER BY register_date";
        List<History> histories = jdbcTemplate.query(query, mapper(), roomId);
        if (histories.isEmpty()) {
            throw new NoHistoryException(roomId);
        }
        return histories;
    }

    private RowMapper<History> mapper() {
        return (resultSet, rowNum) -> new History(
                resultSet.getString("start_position"),
                resultSet.getString("end_position")
        );
    }
}
