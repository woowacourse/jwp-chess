package chess.dao;

import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.dto.GameDto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public int create(ChessGame chessGame) {
        final String sql = "insert into game (title, password, white_turn) values (?, ?, true)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"no"});
            ps.setString(1, chessGame.getTitle());
            ps.setString(2, chessGame.getPassword());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void save() {
        final String sql = chooseSaveSql();
        jdbcTemplate.update(sql, Camp.BLACK.isNotTurn());
    }

    private String chooseSaveSql() {
        String sql = "insert into game (no, white_turn) values (1,?)";
        if (isGameExistIn()) {
            sql = "update game set white_turn = ?";
        }
        return sql;
    }

    private boolean isGameExistIn() {
        final String sql = "select no from game";

        return jdbcTemplate.query(sql, ResultSet::next);
    }

    public boolean isWhiteTurn() {
        final String sql = "select white_turn from game";

        return jdbcTemplate.queryForObject(sql, Boolean.class);
    }

    public List<GameDto> findAll() {
        final String sql = "select no, title from game";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new GameDto(
                resultSet.getInt("no"),
                resultSet.getString("title")
        ));
    }

    public void deleteById(int id) {
        final String sql = "delete from game where no = ?";
        jdbcTemplate.update(sql, id);
    }

    public ChessGame findById(int id) {
        final String sql = "select title, password from game where no = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> new ChessGame(
                resultSet.getString("title"),
                resultSet.getString("password")
        ), id);
    }
}
