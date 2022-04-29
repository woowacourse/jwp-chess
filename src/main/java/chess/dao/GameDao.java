package chess.dao;

import chess.domain.Camp;
import chess.dto.GameDto;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GameDto> selectGames() {
        final String sql = "select no, title from game";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new GameDto(
                resultSet.getInt("no"),
                resultSet.getString("title"))
        );
    }

    public long insert(GameDto game) {
        final String sql = "insert into game (running, white_turn, title, password) values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"no"});
            ps.setBoolean(1, game.isRunning());
            ps.setBoolean(2, game.isWhiteTurn());
            ps.setString(3, game.getTitle());
            ps.setString(4, game.getPassword());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public String loadPassword(long gameNo) {
        final String sql = "select password from game where no = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameNo);
    }

    public String loadTitle(long gameNo) {
        final String sql = "select title from game where no = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameNo);
    }

    public boolean isRunning(long gameNo) {
        final String sql = "select running from game where no = ?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, gameNo);
    }

    public void update(long gameNo, boolean whiteTurn) {
        final String sql = "update game set white_turn = ? where no = ?";
        jdbcTemplate.update(sql, whiteTurn, gameNo);
    }

    public boolean isWhiteTurn(long gameNo) {
        final String sql = "select white_turn from game where no = ?";

        return jdbcTemplate.queryForObject(sql, Boolean.class, gameNo);
    }

    public void end(long gameNo) {
        final String sql = "update game set running = ? where no = ?";
        jdbcTemplate.update(sql, false, gameNo);
    }
}
