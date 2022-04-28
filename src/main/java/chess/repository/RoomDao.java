package chess.repository;

import chess.entity.RoomEntity;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public RoomDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("rooms")
                .usingGeneratedKeyColumns("room_id");
    }

    public RoomEntity insert(RoomEntity room) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(room);
        final Long id = insertActor.executeAndReturnKey(parameters).longValue();
        return new RoomEntity(id, room.getName(), room.getPassword());
    }

    public RoomEntity findById(long id) {
        final String sql = "select * from rooms where room_id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    public List<RoomEntity> findAll() {
        final String sql = "select * from rooms;";
        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<RoomEntity> rowMapper() {
        return (rs, rowNum) -> {
            final Long id = rs.getLong("room_id");
            final String name = rs.getString("name");
            final String password = rs.getString("password");
            return new RoomEntity(id, name, password);
        };
    }

    public void deleteById(Long id) {
        final String sql = "delete from rooms where room_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
