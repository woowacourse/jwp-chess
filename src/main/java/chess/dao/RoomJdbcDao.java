package chess.dao;

import chess.entity.RoomEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomJdbcDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(RoomEntity room) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into room (turn, password, name) values (?, ?, ?)";
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, room.getTurn());
                ps.setString(2, room.getPassword());
                ps.setString(3, room.getName());
                return ps;
            }, keyHolder);
        } catch (DataAccessException exception) {
            System.out.println(exception.getMessage());
            throw new DataIntegrityViolationException("방 이름은 중복될 수 없습니다.");
        }

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<RoomEntity> findById(long id) {
        String sql = "select * from room where id = ?";
        try {
            RoomEntity room = jdbcTemplate.queryForObject(sql, rowMapper(), id);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<RoomEntity> findAll() {
        String sql = "select * from room";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public void updateTurn(long id, String turn) {
        String sql = "update room set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn, id);
    }

    @Override
    public void deleteRoom(long id) {
        String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<RoomEntity> rowMapper() {
        return (rs, rowNum) ->
                new RoomEntity(
                        rs.getLong("id"),
                        rs.getString("turn"),
                        rs.getString("name"),
                        rs.getString("password"));
    }
}
