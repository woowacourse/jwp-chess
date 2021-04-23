package chess.dao;

import chess.domain.Movement;
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

    public void deleteHistoryByRoomId(final String roomId) {
        String query = "DELETE FROM history WHERE room_id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public List<Movement> allHistoryByRoomId(final String roomId) {
        String query = "SELECT start_position, end_position FROM history WHERE room_id = ? ORDER BY register_date";
        List<Movement> histories = jdbcTemplate.query(query, mapper(), roomId);
        if (histories.isEmpty()) {
            throw new NoHistoryException(roomId);
        }
        return histories;
    }

    private RowMapper<Movement> mapper() {
        return (resultSet, rowNum) -> new Movement(
                resultSet.getString("start_position"),
                resultSet.getString("end_position")
        );
    }
}
