package chess.dao;

import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.dto.GameDto;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
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

    public int save(ChessGame chessGame) {
        final String sql = "insert into game (title, password, finished, white_turn) values (?, ?, false, true)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"no"});
            ps.setString(1, chessGame.getTitle());
            ps.setString(2, chessGame.getPassword());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<GameDto> findAll() {
        final String sql = "select no, title from game";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new GameDto(
                resultSet.getInt("no"),
                resultSet.getString("title")
        ));
    }

    public ChessGame findById(int id) {
        final String sql = "select title, password, finished from game where no = ?";

        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> new ChessGame(
                resultSet.getString("title"),
                resultSet.getString("password")
        ), id);
    }

    public void updateTurnById(int id) {
        final String sql = "update game set white_turn = ? where no = ?";

        jdbcTemplate.update(sql, Camp.BLACK.isNotTurn(), id);
    }

    public void updateStateById(int id) {
        final String sql = "update game set finished = true where no = ?";

        jdbcTemplate.update(sql, id);
    }

    public void deleteById(int id) {
        final String sql = "delete from game where no = ?";

        jdbcTemplate.update(sql, id);
    }

    public boolean isWhiteTurn(int id) {
        final String sql = "select white_turn from game where no = ?";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    public boolean findRunningById(int id) {
        final String sql = "select finished from game where no = ?";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
