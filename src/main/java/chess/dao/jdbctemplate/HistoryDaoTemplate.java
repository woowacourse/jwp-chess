package chess.dao.jdbctemplate;

import chess.dao.HistoryDao;
import chess.dao.dto.history.HistoryDto;
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

    private final RowMapper<HistoryDto> actorRowMapper = (resultSet, rowNum) ->
            new HistoryDto(
                    resultSet.getLong("id"),
                    resultSet.getLong("game_id"),
                    resultSet.getString("move_command"),
                    resultSet.getString("turn_owner"),
                    resultSet.getInt("turn_number"),
                    resultSet.getBoolean("playing")
            );

    @Override
    public Long save(final HistoryDto historyDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO history(game_id, move_command, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, historyDto.getGameId());
            ps.setString(2, historyDto.getMoveCommand());
            ps.setString(3, historyDto.getTurnOwner());
            ps.setInt(4, historyDto.getTurnNumber());
            ps.setBoolean(5, historyDto.isPlaying());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<HistoryDto> findByGameId(final Long gameId) {
        String sql = "SELECT * from history where game_id = ? ORDER BY id ASC";
        return jdbcTemplate.query(sql, actorRowMapper, gameId);
    }
}
