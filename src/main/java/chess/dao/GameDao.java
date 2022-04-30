package chess.dao;

import chess.domain.piece.Color;
import chess.domain.room.Room;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createByTitleAndPassword(String title, String password) {
        final String sql = "insert into game (title, password) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, title);
            ps.setString(2, password);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Room findRoomById(long id) {
        final String sql = "select id, end_flag, turn, title, password from game where id = ?";

        return jdbcTemplate.queryForObject(sql, actorRowMapper, id);
    }

    public List<Room> findAllRoom() {
        final String sql = "select id, end_flag, turn, title, password from game";

        return jdbcTemplate.query(sql, actorRowMapper);
    }

    public boolean exists(String id) {
        final String sql = "select count(*) from game where id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    public void updateTurnById(Color nextTurn, long id) {
        final String sql = "update game set turn = ? where id = ?";

        jdbcTemplate.update(sql, nextTurn.getName(), id);
    }

    public void updateEndFlagById(boolean endFlag, long id) {
        final String sql = "update game set end_flag = ? where id = ?";

        jdbcTemplate.update(sql, endFlag, id);
    }

    public void deleteById(long id) {
        final String sql = "delete from game where id = ?";

        jdbcTemplate.update(sql, id);
    }

    private final RowMapper<Room> actorRowMapper = (resultSet, rowNum) -> new Room(
        resultSet.getLong("id"),
        resultSet.getBoolean("end_flag"),
        Color.of(resultSet.getString("turn")),
        resultSet.getString("title"),
        resultSet.getString("password")
    );

}
