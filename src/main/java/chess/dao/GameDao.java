package chess.dao;

import chess.domain.ChessGame;
import java.sql.PreparedStatement;
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

    public long insert(ChessGame game, long roomNo) {
        final String sql = "insert into game (room_no, running, white_turn) values (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"no"});
            ps.setLong(1, roomNo);
            ps.setBoolean(2, game.isRunning());
            ps.setBoolean(3, game.isWhiteTurn());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public long findNoByRoom(long roomNo) {
        final String sql = "select no from game where room_no = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, roomNo);
    }

    public boolean isRunning(long roomNo) {
        final String sql = "select running from game where room_no = ?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, roomNo);
    }

    public void update(long roomNo, boolean whiteTurn) {
        final String sql = "update game set white_turn = ? where room_no = ?";
        jdbcTemplate.update(sql, whiteTurn, roomNo);
    }

    public boolean isWhiteTurn(long roomNo) {
        final String sql = "select white_turn from game where room_no = ?";

        return jdbcTemplate.queryForObject(sql, Boolean.class, roomNo);
    }

    public void end(long roomNo) {
        final String sql = "update game set running = ? where room_no = ?";
        jdbcTemplate.update(sql, false, roomNo);
    }

    public void delete(long roomNo) {
        final String sql = "delete from game where room_no = ?";
        jdbcTemplate.update(sql, roomNo);
    }
}
