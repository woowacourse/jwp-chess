package chess.dao.spring;

import chess.domain.Camp;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

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
}
