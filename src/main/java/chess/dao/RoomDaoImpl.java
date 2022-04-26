package chess.dao;

import chess.entity.Room;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(Room room) {
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
    public Optional<Room> findByName(String name) {
        String sql = "select * from room where name = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) ->
                            new Room(
                                    rs.getLong("id"),
                                    rs.getString("turn"),
                                    rs.getString("name"),
                                    rs.getString("password")),
                    name);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Room> findById(long id) {
        String sql = "select * from room where id = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) ->
                            new Room(
                                    rs.getLong("id"),
                                    rs.getString("turn"),
                                    rs.getString("name"),
                                    rs.getString("password")),
                    id);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateTurn(long id, String turn) {
        String sql = "update room set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn, id);
    }
}
