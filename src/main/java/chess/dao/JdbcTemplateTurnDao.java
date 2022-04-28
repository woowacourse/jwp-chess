package chess.dao;

import chess.domain.piece.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateTurnDao implements TurnDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateTurnDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void init(int gameId) {
        jdbcTemplate.update("INSERT INTO turn(team, game_id) VALUES (?, ?)", "white", gameId);
    }

    @Override
    public void update(String nowTurn, String nextTurn) {
        String sql = "update turn set team = ? where team = ?";
        jdbcTemplate.update(sql, nextTurn, nowTurn);
    }

    @Override
    public String getTurn(int gameId) {
        String sql = "select team from turn where game_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    @Override
    public void reset() {
        removeAll();
        String sql = "insert into turn (team) values (?)";
        jdbcTemplate.update(sql, Team.WHITE.toString());
    }

    private void removeAll() {
        String sql = "truncate table turn";
        this.jdbcTemplate.execute(sql);
    }
}
