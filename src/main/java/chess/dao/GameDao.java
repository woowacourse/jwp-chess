package chess.dao;

import chess.domain.Camp;
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

    public void save() {
        final String sql = chooseSaveSql();
        jdbcTemplate.update(sql, true, Camp.BLACK.isNotTurn(), "방이름1", "1234", 1);
    }

    private String chooseSaveSql() {
        String sql = "insert into game (running, white_turn, title, password, no) values (?,?,?,?,?)";
        if (isGameExistIn()) {
            sql = "update game set running = ?, white_turn = ?, title = ?, password = ? where no = ?";
        }
        return sql;
    }

    private boolean isGameExistIn() {
        final String sql = "select no from game where no = 1";

        return jdbcTemplate.query(sql, ResultSet::next);
    }

    public boolean isWhiteTurn() {
        final String sql = "select white_turn from game where no = 1";

        return jdbcTemplate.queryForObject(sql, Boolean.class);
    }

    public List<GameDto> selectGames() {
        final String sql = "select no, title from game";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new GameDto(
                resultSet.getInt("no"),
                resultSet.getString("title"))
        );
    }
}
