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
                    resultSet.getString("turn_owner"),
                    resultSet.getInt("turn_number"),
                    resultSet.getBoolean("playing")
            );

    @Override
    public Long save(final ChessManager chessManager, final Long gameId) {
        String sql = "INSERT INTO state(game_id, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?)";
        return (long) jdbcTemplate.update(sql, gameId, chessManager.turnOwner().name(), chessManager.turnNumber(), chessManager.isPlaying());
    }

    @Override
    public Long update(final ChessManager chessManager, final Long gameId) {
        String sql = "UPDATE state SET turn_owner=?, turn_number=?, playing=? WHERE game_id=?";
        return (long) jdbcTemplate.update(sql, chessManager.turnOwner().name(), chessManager.turnNumber(), chessManager.isPlaying(), gameId);
    }

    @Override
    public StateDto findByGameId(final Long gameId) {
        String sql = "SELECT * from state where game_id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }
}
