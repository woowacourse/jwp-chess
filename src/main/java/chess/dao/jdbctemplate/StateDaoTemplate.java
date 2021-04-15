package chess.dao.jdbctemplate;

import chess.controller.web.dto.state.StateResponseDto;
import chess.dao.StateDao;
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

    private final RowMapper<StateResponseDto> actorRowMapper = (resultSet, rowNum) ->
            new StateResponseDto(
                    resultSet.getString("turn_owner"),
                    resultSet.getInt("turn_number"),
                    resultSet.getBoolean("playing")
            );

    @Override
    public Long saveState(ChessManager chessManager, Long gameId) {
        String sql = "INSERT INTO state(game_id, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?)";
        return (long) jdbcTemplate.update(sql, gameId, chessManager.turnOwner().name(), chessManager.turnNumber(), chessManager.isPlaying());
    }

    @Override
    public Long updateState(ChessManager chessManager, Long gameId) {
        String sql = "UPDATE state SET turn_owner=?, turn_number=?, playing=? WHERE game_id=?";
        return (long) jdbcTemplate.update(sql, chessManager.turnOwner().name(), chessManager.turnNumber(), chessManager.isPlaying(), gameId);
    }

    @Override
    public StateResponseDto findStateByGameId(Long gameId) {
        String sql = "SELECT * from state where game_id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }
}
