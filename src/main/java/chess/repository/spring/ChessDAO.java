package chess.repository.spring;

import chess.domain.history.History;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessDAO {
    private static final RowMapper<History> ROW_MAPPER = (resultSet, rowNumber) -> {
        String source = resultSet.getString("source");
        String destination = resultSet.getString("destination");
        String teamType = resultSet.getString("team_type");
        return new History(source, destination, teamType);
    };

    private final JdbcTemplate jdbcTemplate;

    public ChessDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<History> findAllHistoriesByRoomId(int roomId) {
        String query = "SELECT * FROM HISTORY WHERE ROOM_ID = ?";
        return jdbcTemplate.query(query, ROW_MAPPER, roomId);
    }

    public void insertHistoryByRoomId(History history, int roomId) {
        String query = "INSERT INTO HISTORY (SOURCE, DESTINATION, TEAM_TYPE, ROOM_ID) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, history.getSource(), history.getDestination(), history.getTeamType(), roomId);
    }

    public void deleteAllHistoriesByRoomId(int roomId) {
        String query = "DELETE FROM HISTORY WHERE ROOM_ID = ?";
        jdbcTemplate.update(query, roomId);
    }
}
