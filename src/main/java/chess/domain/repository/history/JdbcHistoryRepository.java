package chess.domain.repository.history;

import chess.domain.board.piece.Owner;
import chess.domain.board.position.Position;
import chess.domain.history.History;
import chess.domain.manager.State;
import chess.domain.manager.TurnNumber;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcHistoryRepository implements HistoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<History> actorRowMapper = (resultSet, rowNum) ->
            new History(
                    resultSet.getLong("id"),
                    resultSet.getLong("game_id"),
                    Position.of(resultSet.getString("source")),
                    Position.of(resultSet.getString("target")),
                    State.of(Owner.valueOf(resultSet.getString("turn_owner")),
                            TurnNumber.of(resultSet.getInt("turn_number")),
                            resultSet.getBoolean("playing"))
            );

    public JdbcHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final History history) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO history(game_id, source, target, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, history.gameId());
            ps.setString(2, history.sourceToString());
            ps.setString(3, history.targetToString());
            ps.setString(4, history.turnOwnerName());
            ps.setInt(5, history.turnNumber());
            ps.setBoolean(6, history.isPlaying());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<History> findByGameId(final Long gameId) {
        String sql = "SELECT * from history where game_id = ? ORDER BY id ASC";
        return jdbcTemplate.query(sql, actorRowMapper, gameId);
    }
}
