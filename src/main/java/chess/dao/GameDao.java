package chess.dao;

import chess.domain.ChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class GameDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long create(ChessGame chessGame) {
        String sql = "insert into game (is_end) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"game_id"});
            preparedStatement.setBoolean(1, chessGame.isEnd());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean isEnd(Long gameId) {
        String sql = "select is_end from game where game_id = ?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, gameId);
    }

    public void update(final Long gameId, final boolean isEnd) {
        String sql = "update game set is_end = ? where game_id = ?";
        jdbcTemplate.update(sql, isEnd, gameId);
    }
}
