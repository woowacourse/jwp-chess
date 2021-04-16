package chess.dao.jdbctemplate;

import chess.controller.web.dto.game.GameResponseDto;
import chess.dao.GameDao;
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

    private RowMapper<GameResponseDto> actorRowMapper = (resultSet, rowNum) ->
            new GameResponseDto(
                    resultSet.getLong("id"),
                    resultSet.getString("white_username"),
                    resultSet.getString("black_username"),
                    resultSet.getString("room_name")
            );

    @Override
    public Long saveGame(final Game game) {
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
    public GameResponseDto findGameById(final Long gameId) {
        String sql = "SELECT * from game where id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }
}
