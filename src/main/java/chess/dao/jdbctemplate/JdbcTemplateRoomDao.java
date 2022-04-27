package chess.dao.jdbctemplate;

import chess.dao.RoomDao;
import chess.dto.RoomResponseDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateRoomDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(String name, String pw) {
        final String sql = "insert into room (name, pw) values (?,?)";
        jdbcTemplate.update(sql,name,pw);
    }

    @Override
    public List<RoomResponseDto> getRooms() {
        final String sql = "select * from room";
        return jdbcTemplate.query(sql, new RoomMapper());
    }

    @Override
    public List<RoomResponseDto> deleteRoom() {
        return null;
    }
}
