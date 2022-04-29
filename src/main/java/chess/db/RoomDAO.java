package chess.db;

import chess.domain.state.State;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDAO {

    private static final String EXIST_NAME_SQL = "select count(*) from room where name = ?";
    private static final String FIND_ID_BY_NAME_SQL = "select id from room where name = ?";
    private static final String FIND_ALL_NAME_SQL = "select name from room join state where room.id = state.roomID and state.now = ?";

    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isNameDuplicated(String roomName) {
        return jdbcTemplate.queryForObject(EXIST_NAME_SQL, Integer.class, roomName) > 0;
    }

    public String findIdByName(String roomName) {
        return jdbcTemplate.queryForObject(FIND_ID_BY_NAME_SQL, String.class, roomName);
    }

    public List<String> findAllSavedName() {
        return jdbcTemplate.query(FIND_ALL_NAME_SQL, nameRowMapper, State.ON.name());
    }

    public List<String> findAllEndedName() {
        return jdbcTemplate.query(FIND_ALL_NAME_SQL, nameRowMapper, State.OFF.name());
    }

    private final RowMapper<String> nameRowMapper = (resultSet, rowNum) ->
            resultSet.getString("name");
}
