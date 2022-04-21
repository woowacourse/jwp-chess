package chess.repository;

import chess.entity.RoomEntity;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
}
