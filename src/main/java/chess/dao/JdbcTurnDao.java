package chess.dao;

import chess.domain.player.Team;
import chess.dto.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTurnDao implements TurnDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TurnDto> turnRowMapper = (resultSet, rowNum) -> {
        return new TurnDto(
                resultSet.getString("team")
        );
    };

    public JdbcTurnDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TurnDto findTurn(final long roomId) {
        final String sql = "select * from turn where roomId = ?";
        return jdbcTemplate.queryForObject(sql, turnRowMapper, roomId);
    }

    @Override
    public void updateTurn(final long roomId, final String turn) {
        final String sql = "update turn set team = ? where roomId = ? and team = ?";
        jdbcTemplate.update(sql, Team.getOpponentTeamName(turn), roomId, turn);
    }

    @Override
    public void resetTurn(final long roomId) {
        final String deleteSql = "delete from turn where roomId = ?";
        jdbcTemplate.update(deleteSql, roomId);
        final String sql = "insert into turn (roomId, team) values (?, ?)";
        jdbcTemplate.update(sql, roomId, "WHITE");
    }
}
