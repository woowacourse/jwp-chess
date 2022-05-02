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
public class RoomRepositoryImpl implements RoomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public RoomRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
            .withTableName("room")
            .usingGeneratedKeyColumns("id");
    }


    @Override
    public List<RoomEntity> findRooms() {
        final String sql = "SELECT * from room WHERE game_over = false;";
        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<RoomEntity> rowMapper() {
        return (rs, rowNum) -> {
            final Long id = rs.getLong("id");
            final String password = rs.getString("password");
            final String name = rs.getString("name");
            final String team = rs.getString("team");
            final boolean gameOver = rs.getBoolean("game_over");
            return new RoomEntity(id, password, name, team, gameOver);
        };
    }

    @Override
    public RoomEntity insert(final RoomEntity room) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(room);
        final Long id = insertActor.executeAndReturnKey(parameters).longValue();
        return new RoomEntity(id, room.getPassword(), room.getName(), room.getTeam(), room.isGameOver());
    }

    @Override
    public RoomEntity findById(final Long id) {
        String sql = "SELECT * FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    @Override
    public void updateTeam(final Long id, final String team) {
        String sql = "UPDATE room SET team = ? WHERE id = ?";
        jdbcTemplate.update(sql, team, id);
    }

    @Override
    public void updateGameOver(final Long id) {
        String sql = "UPDATE room SET game_over = true WHERE  id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public RoomEntity update(final RoomEntity room) {
        final String sql = "UPDATE room SET  name = ?,  team = ?,  game_over = ? WHERE  id = ?";
        jdbcTemplate.update(sql, room.getName(), room.getTeam(), room.isGameOver(), room.getId());
        return room;
    }
}
