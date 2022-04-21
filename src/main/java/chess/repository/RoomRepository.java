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
public class RoomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public RoomRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
            .withTableName("room")
            .usingGeneratedKeyColumns("id");
    }


    public List<RoomEntity> findRooms() {
        final String sql = "select * from room where game_over = false;";
        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<RoomEntity> rowMapper() {
        return (rs, rowNum) -> {
            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final String team = rs.getString("team");
            final boolean gameOver = rs.getBoolean("game_over");
            return new RoomEntity(id, name, team, gameOver);
        };
    }

    public RoomEntity insert(final RoomEntity room) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(room);
        final Long id = insertActor.executeAndReturnKey(parameters).longValue();
        return new RoomEntity(id, room.getName(), room.getTeam(), room.isGameOver());
    }

    public void updateTeam(final Long id, final String team) {
        String sql = "update room set team = ? where id = ?";
        jdbcTemplate.update(sql, team, id);
    }

    public RoomEntity findById(final Long id) {
        String sql = "select * from room where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }
}
