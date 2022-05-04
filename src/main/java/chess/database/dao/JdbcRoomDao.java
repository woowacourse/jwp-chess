package chess.database.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import chess.database.entity.RoomEntity;

@Repository
public class JdbcRoomDao implements RoomDao {

    private static final RowMapper<RoomEntity> ROOM_ENTITY_ROW_MAPPER = (resultSet, rowNum) -> new RoomEntity(
        resultSet.getLong("id"),
        resultSet.getString("room_name"),
        resultSet.getString("password"));

    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public JdbcRoomDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("room")
            .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveRoom(RoomEntity entity) {
        return jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).longValue();
    }

    @Override
    public Optional<RoomEntity> findRoomById(Long id) {
        String sql = "SELECT * FROM room WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROOM_ENTITY_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<RoomEntity> findAll() {
        String sql = "SELECT * FROM room";
        return jdbcTemplate.query(sql, ROOM_ENTITY_ROW_MAPPER);
    }

    @Override
    public void deleteRoom(Long id) {
        String sql = "DELETE FROM room where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
