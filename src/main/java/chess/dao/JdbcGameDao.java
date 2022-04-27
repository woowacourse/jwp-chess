package chess.dao;

import chess.entity.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class JdbcGameDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(Game game) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final String sql = "insert into game (title, password, turn, status) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, game.getTitle());
            preparedStatement.setString(2, game.getPassword());
            preparedStatement.setString(3, game.getTurn());
            preparedStatement.setString(4, game.getStatus());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}