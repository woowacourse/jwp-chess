package chess.dao;

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

    private static final int IS_EXIST = 1;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public RoomDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("rooms")
                .usingGeneratedKeyColumns("id");
    }

    public RoomEntity save(final RoomEntity roomEntity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(roomEntity);
        final Long id = insertActor.executeAndReturnKey(parameters).longValue();
        return new RoomEntity(id, roomEntity.getName(), roomEntity.getPassword(), roomEntity.getStatus(),
                roomEntity.getTurn());
    }

    public List<RoomEntity> findAll() {
        final String sql = "SELECT * FROM rooms";
        return jdbcTemplate.query(sql, rowMapper());
    }

    public RoomEntity findById(final Long id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    public boolean isExist(final Long id) {
        final String sql = "SELECT EXISTS (SELECT id FROM rooms WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Long.class, id) == IS_EXIST;
    }

    public void updateStatusById(final Long id, final String status) {
        final String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }

    public void updateTurnById(final Long id, final String turn) {
        final String sql = "UPDATE rooms SET turn = ? WHERE id = ?";
        jdbcTemplate.update(sql, turn, id);
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM rooms WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<RoomEntity> rowMapper() {
        return (resultSet, rowNum) -> new RoomEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("password"),
                resultSet.getString("status"),
                resultSet.getString("turn")
        );
    }
}
