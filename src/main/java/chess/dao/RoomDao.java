package chess.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public RoomDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int selectLastRoomId() {
        String query = "SELECT room_id from room ORDER BY room_id desc limit 1";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    public int insertRoom(int newRoomId) {
        String query = "INSERT INTO room(room_id, current_turn) values(?, ?)";
        jdbcTemplate.update(query, newRoomId, "white");
        return newRoomId;
    }

    public String selectTurnByRoomId(int roomId) {
        String query = "SELECT current_turn from room where room_id = :room_id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("room_id", roomId);
        return namedParameterJdbcTemplate.queryForObject(query, namedParameters, String.class);
    }

    public void changeTurn(String nextTurn, String currentTurn, int roomId) {
        String query = "UPDATE room SET current_turn=? WHERE current_turn= ? AND room_id = ?";
        jdbcTemplate.update(query, nextTurn, currentTurn, roomId);
    }
}
