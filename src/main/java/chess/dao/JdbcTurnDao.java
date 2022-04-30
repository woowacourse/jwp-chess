package chess.dao;

import chess.domain.player.Team;
import chess.dto.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTurnDao implements TurnDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TurnDto> turnRowMapper = (resultSet, rowNum) -> new TurnDto(
            resultSet.getString("team")
    );

    public JdbcTurnDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertTurn(int roomId) {
        final String sql = "insert into turn(roomId, team) values(?, 'WHITE')";
        jdbcTemplate.update(sql, roomId);
    }

    @Override
    public TurnDto findTurn(int roomId) {
        final String sql = "select * from turn where roomId = ?";
        return jdbcTemplate.queryForObject(sql, turnRowMapper, roomId);
    }

    @Override
    public void updateTurn(int roomId, final String turn) {
        final String sql = "update turn set team = ? where roomId = ? and team = ?";
        if (turn.equals(Team.WHITE.getName())) {
            jdbcTemplate.update(sql, Team.BLACK.getName(), roomId, Team.WHITE.getName());
            return;
        }
        jdbcTemplate.update(sql, Team.WHITE.getName(), roomId, Team.BLACK.getName());
    }

    @Override
    public void initializeTurn(int roomId) {
        final String sql = "update turn set team = 'WHITE' where roomId = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
