package chess.dao;

import chess.domain.team.Team;
import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TeamDao {
    private final JdbcTemplate jdbcTemplate;

    public TeamDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Team team, long teamId) {
        String sql = "insert into team (name, is_turn, game_id) values (?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"team_id"});
            preparedStatement.setString(1, team.getName());
            preparedStatement.setBoolean(2, team.isCurrentTurn());
            preparedStatement.setLong(3, teamId);
            return preparedStatement;
        });
    }

    public Team load(long gameId, String color) {
        String sql = "select is_turn from team where game_id = ? and name = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            boolean is_turn = resultSet.getBoolean("is_turn");
            return Team.of(color, is_turn);
        }, gameId, color);
    }

    public void update(final long gameId, final Team team) {
        String sql = "update team set is_turn = ? where game_id = ? and name = ?";
        jdbcTemplate.update(sql, team.isCurrentTurn(), gameId, team.getName());
    }
}
