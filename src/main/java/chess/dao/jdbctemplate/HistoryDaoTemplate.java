package chess.dao.jdbctemplate;

import chess.controller.web.dto.history.HistoryResponseDto;
import chess.dao.HistoryDao;
import chess.domain.history.History;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class HistoryDaoTemplate implements HistoryDao {

    private final JdbcTemplate jdbcTemplate;

    public HistoryDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<HistoryResponseDto> actorRowMapper = (resultSet, rowNum) ->
            new HistoryResponseDto(
                    resultSet.getString("move_command"),
                    resultSet.getString("turn_owner"),
                    resultSet.getInt("turn_number"),
                    resultSet.getBoolean("playing")
            );

    @Override
    public Long saveHistory(final History history, final Long gameId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO history(game_id, move_command, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, gameId);
            ps.setString(2, history.moveCommand());
            ps.setString(3, history.turnOwner());
            ps.setInt(4, history.turnNumber());
            ps.setBoolean(5, history.isPlaying());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<HistoryResponseDto> findHistoryByGameId(final Long gameId) {
        String sql = "SELECT * from history where game_id = ? ORDER BY id ASC";
        return jdbcTemplate.query(sql, actorRowMapper, gameId);
    }
}
