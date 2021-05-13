package chess.domain.repository.user;

import chess.domain.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (resultSet, rowNum) ->
            new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("password")
            );

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final User user) {
        final String sql = "INSERT INTO user(name, password) VALUES(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<User> findById(final Long id) {
        final String sql = "SELECT * FROM user WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, id));
    }

    @Override
    public Optional<User> findByName(final String name) {
        final String sql = "SELECT * FROM user WHERE name = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, name));
    }

    @Override
    public Optional<User> findByRoomIdAndName(Long roomId, String userName) {
        final String sql = "SELECT * FROM user AS u " +
                "JOIN room AS r " +
                "ON u.id = r.white_user_id " +
                "OR u.id = r.black_user_id " +
                "WHERE r.id = ? AND u.name = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, roomId, userName));
    }
}
