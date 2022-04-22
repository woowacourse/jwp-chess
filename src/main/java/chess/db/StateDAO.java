package chess.db;

import chess.domain.piece.Color;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StateDAO {

    private static final String FIND_COLOR_SQL = "select color from state where roomID = ?";
    private static final String CONVERT_COLOR_SQL = "update state set color = ? where roomID = ?";
    private static final String CHECK_SAVE_SQL = "select count(*) from room where id = ?";
    private static final String FIND_ALL_USERS_SQL = "select id from room";
    private static final String TERMINATE_GAME_SQL = "delete from room where id = ?";
    private static final String INITIALIZE_ID_SQL = "insert into room values (?)";
    private static final String INITIALIZE_COLOR_SQL = "insert into state values (?, ?)";
    private static final String DELIMITER = ", ";

    private JdbcTemplate jdbcTemplate;

    public StateDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<String> stringRowMapper = (resultSet, rowNum) ->
            resultSet.getString("id");

    private final RowMapper<String> colorRowMapper = (resultSet, rowNum) ->
            resultSet.getString("color");

    public Color findColor(String roomId) {
        String color = jdbcTemplate.queryForObject(FIND_COLOR_SQL, colorRowMapper, roomId);
        return Color.valueOf(color);
    }

    public void convertColor(Color color, String roomId) {
        jdbcTemplate.update(CONVERT_COLOR_SQL, color.name(), roomId);
    }

    public boolean isSaved(String roomId) {
        int count = jdbcTemplate.queryForObject(CHECK_SAVE_SQL, Integer.class, roomId);
        return count > 0;
    }

    public void terminateDB(String roomId) {
        jdbcTemplate.update(TERMINATE_GAME_SQL, roomId);
    }

    public void initializeID(String roomId) {
        jdbcTemplate.update(INITIALIZE_ID_SQL, roomId);
    }

    public void initializeColor(String roomId) {
        jdbcTemplate.update(INITIALIZE_COLOR_SQL, roomId, Color.WHITE.name());
    }

    public String findAllUsers() {
        List<String> ids = jdbcTemplate.query(FIND_ALL_USERS_SQL, stringRowMapper);
        return String.join(DELIMITER, ids);
    }
}
