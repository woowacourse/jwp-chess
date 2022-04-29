package chess.dao.jdbctemplate;

import chess.dao.RoomDao;
import chess.dto.RoomResponseDto;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

@Service
public class JdbcTemplateRoomDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcTemplateRoomDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("room")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public int create(String name, String pw) {
        SqlParameterSource parameters = new MapSqlParameterSource("name", name)
                .addValue("pw", pw);
        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public List<RoomResponseDto> getRooms() {
        final String sql = "select * from room ";
        return jdbcTemplate.query(sql, new RoomMapper());
    }

    @Override
    public RoomResponseDto getRoom(int id) {
        final String sql = "select * from room where id = ?";
        return jdbcTemplate.queryForObject(sql, new RoomMapper(), id);
    }

    @Override
    public int deleteRoom(String pw, int id) {
        String sql = "delete from room where id = ? and pw = ?";
        return jdbcTemplate.update(sql, id, pw);
    }
}
