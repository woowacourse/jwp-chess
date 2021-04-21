package chess.dao.jdbctemplate;

import chess.dao.GameDao;
import chess.dao.dto.game.GameDto;
import chess.domain.game.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class GameDaoTemplate implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<GameDto> actorRowMapper = (resultSet, rowNum) ->
            new GameDto(
                    resultSet.getLong("id"),
                    resultSet.getString("white_username"),
                    resultSet.getString("black_username"),
                    resultSet.getString("room_name")
            );

    @Override
    public Long save(final Game game) {
        String sql = "INSERT INTO game(room_name, white_username, black_username) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, game.roomName());
            ps.setString(2, game.whiteUsername());
            ps.setString(3, game.blackUsername());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public GameDto findById(final Long gameId) {
        String sql = "SELECT * from game where id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }
}
