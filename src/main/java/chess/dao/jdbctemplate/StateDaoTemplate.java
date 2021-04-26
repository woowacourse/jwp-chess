package chess.dao.jdbctemplate;

import chess.dao.StateDao;
import chess.dao.dto.state.StateDto;
import chess.domain.manager.ChessManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StateDaoTemplate implements StateDao {

    private final JdbcTemplate jdbcTemplate;

    public StateDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<StateDto> actorRowMapper = (resultSet, rowNum) ->
            new StateDto(
                    resultSet.getLong("id"),
                    resultSet.getLong("game_id"),
                    resultSet.getString("turn_owner"),
                    resultSet.getInt("turn_number"),
                    resultSet.getBoolean("playing")
            );

    @Override
    public Long save(StateDto stateDto) {
        String sql = "INSERT INTO state(game_id, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?)";
        return (long) jdbcTemplate.update(sql,
                stateDto.getGameId(),
                stateDto.getTurnOwner(),
                stateDto.getTurnNumber(),
                stateDto.isPlaying());
    }

    @Override
    public Long update(final StateDto stateDto) {
        String sql = "UPDATE state SET turn_owner=?, turn_number=?, playing=? WHERE game_id=?";
        return (long) jdbcTemplate.update(sql,
                stateDto.getTurnOwner(),
                stateDto.getTurnNumber(),
                stateDto.isPlaying(),
                stateDto.getGameId());
    }

    @Override
    public StateDto findByGameId(final Long gameId) {
        String sql = "SELECT * from state WHERE game_id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }

    @Override
    public StateDto findById(Long id) {
        String sql = "SELECT * from state WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, id);
    }
}
