package chess.dao;

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
    public void insertTurn(int roomId, String team) {
        final String sql = "insert into turn(roomId, team) values(?, ?)";
        jdbcTemplate.update(sql, roomId, team);
    }

    @Override
    public TurnDto findTurn(int roomId) {
        final String sql = "select * from turn where roomId = ?";
        return jdbcTemplate.queryForObject(sql, turnRowMapper, roomId);
    }

    @Override
    public void updateTurn(int roomId, final String nextTeam, final String currentTeam) {
        final String sql = "update turn set team = ? where roomId = ? and team = ?";
        jdbcTemplate.update(sql, nextTeam, roomId, currentTeam);
    }

    @Override
    public void initializeTurn(final int roomId, final String startTeam) {
        final String sql = "update turn set team = ? where roomId = ?";
        jdbcTemplate.update(sql, startTeam, roomId);
    }
}
