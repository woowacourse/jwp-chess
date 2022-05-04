package chess.repository;

import chess.domain.Room;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private static final String TABLE_NAME = "room";
    private static final String KEY_NAME = "id";
    private static final int KEY_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int PASSWORD_INDEX = 3;

    private final SimpleJdbcInsert insertActor;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoomDao(DataSource dataSource,
                   NamedParameterJdbcTemplate jdbcTemplate) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Room room) {
        return insertActor.executeAndReturnKey(
                Map.of("name", room.getName(), "password", room.getPassword())).intValue();
    }

    public List<Room> findAll() {
        String sql = "select * from room";
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) ->
                        new Room(resultSet.getInt(KEY_INDEX), resultSet.getString(NAME_INDEX),
                                resultSet.getString(PASSWORD_INDEX)));
    }

    public Room findById(int roomId) {
        String sql = "select * from room where id = :roomId";
        return jdbcTemplate.queryForObject(sql, Map.of("roomId", roomId),
                (resultSet, rowNum) ->
                        new Room(resultSet.getInt(KEY_INDEX), resultSet.getString(NAME_INDEX),
                                resultSet.getString(PASSWORD_INDEX)));
    }

    public void delete(int roomId) {
        String sql = "delete from room where id = :roomId";
        jdbcTemplate.update(sql, Map.of("roomId", roomId));
    }
}
