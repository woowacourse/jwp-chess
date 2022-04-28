package chess.dao;

import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.dto.GameDto;
import java.sql.ResultSet;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(ChessGame chessGame) {
        final String sql = "insert into game (title, password, white_turn) values (?, ?, true)";
        jdbcTemplate.update(sql, chessGame.getTitle(), chessGame.getPassword());
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
}
