package chess.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDAO {

    private static final String EXIST_NAME_SQL = "select count(*) from room where name = ?";
    private static final String FIND_ID_BY_NAME_SQL = "select id from room where name = ?";


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
}
