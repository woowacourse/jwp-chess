package chess.dao;

import chess.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Long create(final String name, final String pw) {
        String sql = "insert into user (user_name, user_pw) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"user_id"});
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pw);
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public User findByName(final String name) {
        String sql = "select * from user where user_name = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            String userName = resultSet.getString("user_name");
            String userPw = resultSet.getString("user_pw");
            Long roomId = resultSet.getLong("room_id");
            return new User(userName, userPw, roomId);
        }, name);
    }

    public void setRoomId(Long roomId, String name) {
        String sql = "update user set room_id = ? where user_name = ?";
        jdbcTemplate.update(sql, roomId, name);
    }
}
