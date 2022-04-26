package chess.dao;

import chess.domain.piece.Color;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createById(String id) {
        final String sql = "insert into game (id, turn) values (?, ?)";

        jdbcTemplate.update(sql, id, Color.BLACK.getName());
    }

    public boolean exists(String id) {
        final String sql = "select count(*) from game where id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    public boolean findForceEndFlagById(String id) {
        final String sql = "select force_end_flag from game where id = ?";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public Color findTurnById(String id) {
        final String sql = "select turn from game where id = ?";

        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                Color.of(resultSet.getString("turn")), id);
    }

    public void updateTurnById(Color nextTurn, String id) {
        final String sql = "update game set turn = ? where id = ?";

        jdbcTemplate.update(sql, nextTurn.getName(), id);
    }

    public void updateForceEndFlagById(boolean forceEndFlag, String id) {
        final String sql = "update game set force_end_flag = ? where id = ?";

        jdbcTemplate.update(sql, forceEndFlag, id);
    }

    public void deleteById(String id) {
        final String sql = "delete from game where id = ?";

        jdbcTemplate.update(sql, id);
    }

}
