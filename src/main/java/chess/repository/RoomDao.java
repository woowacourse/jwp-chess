package chess.repository;

import chess.entity.RoomEntity;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
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
        return new RoomEntity(room.getId(), room.getName(), room.getPassword());
    }
}
