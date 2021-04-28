package chess.mysql;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = (rs, rownum) -> {
        long userId = rs.getLong("user_id");
        String password = rs.getString("password");
        String color = rs.getString("color");
        long roomId = rs.getLong("room_id");
        return new User(userId, Color.of(color), password, roomId);
    };

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateRoomId(long userId, long roomId) {
        String query =
                "UPDATE chess.user SET room_id = ? WHERE user_id = ?";

        jdbcTemplate.update(query, roomId, userId);
    }

    public User insertUser(User entity) {
        String query =
                "INSERT INTO chess.user (color, password) VALUES " +
                        "(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->
                {
                    PreparedStatement ps = connection.prepareStatement(query, new String[]{"user_id"});
                    ps.setString(1, entity.getColor().name());
                    ps.setString(2, entity.getPassword());
                    return ps;
                }
                , keyHolder
        );

        return new User(keyHolder.getKey().longValue(), entity.getColor(), entity.getPassword(), entity.getRoomId());
    }

    public User findByUserId(long userId) {
        String query =
                "SELECT * " +
                        "FROM chess.user " +
                        "WHERE user_id = ?";

        return jdbcTemplate.query(query, rowMapper, userId).stream().findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저가 없습니다."));
    }

    public List<User> findByRoomId(long roomId) {
        String query =
                "SELECT * " +
                        "FROM chess.user u " +
                        "INNER JOIN chess.room r " +
                        "ON u.user_id = r.user1 OR u.user_id = r.user2 " +
                        "WHERE room_id = ?";

        return jdbcTemplate.query(query, rowMapper, roomId);
    }

    public Optional<User> findByRoomIdAndColor(long roomId, Color color) {
        String query =
                "SELECT * " +
                        "FROM chess.user " +
                        "WHERE room_id = ? AND color = ?";

        return jdbcTemplate.query(query, rowMapper, roomId, color.name()).stream().findAny();
    }
}
