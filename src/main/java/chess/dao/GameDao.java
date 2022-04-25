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

    public void createById(String gameId) {
        final String sql = "insert into game (id, turn) values (?, ?)";

        jdbcTemplate.update(sql, gameId, Color.BLACK.getName());
    }

    public boolean isInId(String gameId) {
        final String sql = "select count(*) from game where id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, gameId) > 0;
    }

    public boolean findForceEndFlagById(String gameId) {
        final String sql = "select force_end_flag from game where id = ?";

        return jdbcTemplate.queryForObject(sql, Boolean.class, gameId);
    }

    public Color findTurnById(String gameId) {
        final String sql = "select turn from game where id = ?";

        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                Color.of(resultSet.getString("turn")), gameId);
    }

    public void updateTurnById(Color nextTurn, String gameId) {
        final String sql = "update game set turn = ? where id = ?";

        jdbcTemplate.update(sql, nextTurn.getName(), gameId);
    }

    public void updateForceEndFlagById(boolean forceEndFlag, String gameId) {
        final String sql = "update game set force_end_flag = ? where id = ?";

        jdbcTemplate.update(sql, forceEndFlag, gameId);
    }

    public void deleteById(String gameId) {
        final String sql = "delete from game where id = ?";

        jdbcTemplate.update(sql, gameId);
    }

}
