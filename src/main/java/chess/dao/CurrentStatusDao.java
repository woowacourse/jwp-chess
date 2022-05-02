package chess.dao;

import chess.domain.CurrentStatus;
import chess.dto.CurrentStatusDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CurrentStatusDao {
    private final JdbcTemplate jdbcTemplate;

    public CurrentStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CurrentStatus> currentStatusMapper = (resultSet, rowNum) ->
            new CurrentStatus(
                    resultSet.getString("state"),
                    resultSet.getString("turn")
            );

    public CurrentStatus findByGameId(int gameId) {
        String sql = "SELECT state,turn FROM current_status WHERE game_id = ?";
        return jdbcTemplate.queryForObject(sql, currentStatusMapper, gameId);
    }

    public void save(int gameId, CurrentStatusDto currentStatus) {
        String sql = "INSERT INTO current_status(game_id,state,turn) VALUES (?,?,?)";
        jdbcTemplate.update(sql, gameId, currentStatus.getState(), currentStatus.getTurn());
    }

    public void update(int gameId, CurrentStatusDto currentStatus) {
        String sql = "UPDATE current_status SET state=? , turn=? WHERE game_id=?";
        jdbcTemplate.update(sql, currentStatus.getState(), currentStatus.getTurn(), gameId);
    }

    public void saveState(int gameId, String state) {
        String sql = "UPDATE current_status SET state=? WHERE game_id=?";
        jdbcTemplate.update(sql, state, gameId);
    }
}
