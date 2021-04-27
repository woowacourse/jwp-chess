package chess.dao.jdbctemplate;

import chess.dao.GameDao;
import chess.dao.dto.game.GameDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class GameDaoTemplate implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<GameDto> actorRowMapper = (resultSet, rowNum) ->
            new GameDto(
                    resultSet.getLong("id"),
                    resultSet.getString("room_name"),
                    resultSet.getString("white_username"),
                    resultSet.getString("white_password"),
                    resultSet.getString("black_username"),
                    resultSet.getString("black_password")
            );

    @Override
    public Long save(final GameDto gameDto) {
        String sql = "INSERT INTO game(room_name, white_username, white_password, black_username, black_password) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, gameDto.getRoomName());
            ps.setString(2, gameDto.getWhiteUsername());
            ps.setString(3, gameDto.getWhitePassword());
            ps.setString(4, gameDto.getBlackUsername());
            ps.setString(5, gameDto.getBlackPassword());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long update(GameDto gameDto) {
        String sql = "UPDATE game SET room_name = ?, white_username = ?, white_password = ?, " +
                "black_username = ?, black_password = ? WHERE id = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, gameDto.getRoomName());
            ps.setString(2, gameDto.getWhiteUsername());
            ps.setString(3, gameDto.getWhitePassword());
            ps.setString(4, gameDto.getBlackUsername());
            ps.setString(5, gameDto.getBlackPassword());
            ps.setLong(6, gameDto.getId());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<GameDto> findByPlayingIsTrue() {
        String sql = "SELECT * from game AS g JOIN state AS s ON g.id = s.game_id WHERE s.playing = true ORDER BY g.id ASC";
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    @Override
    public GameDto findById(final Long gameId) {
        String sql = "SELECT * from game where id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }
}
