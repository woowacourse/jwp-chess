package chess.repository.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import chess.repository.dao.dto.game.GameDto;
import chess.repository.dao.dto.game.GameStatusDto;
import chess.repository.dao.dto.game.GameUpdateDto;

@Component
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(final GameDto gameDto) {
        final String query = "INSERT INTO Game (title, password, player_id1, player_id2, finished, turn_color) VALUES (?,?,?,?,?,?)";
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, gameDto.getTitle());
            preparedStatement.setString(2, gameDto.getPassword());
            preparedStatement.setLong(3, gameDto.getPlayer_id1());
            preparedStatement.setLong(4, gameDto.getPlayer_id2());
            preparedStatement.setBoolean(5, gameDto.getFinished());
            preparedStatement.setString(6, gameDto.getCurrentTurnColor());
            return preparedStatement;
        }, generatedKeyHolder);
        return Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
    }

    public GameDto findById(final Long id) {
        final String query = "SELECT id, title, password, player_id1, player_id2, finished, turn_color FROM Game WHERE id=?";
        return jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> new GameDto(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("password"),
                        resultSet.getLong("player_id1"),
                        resultSet.getLong("player_id2"),
                        resultSet.getBoolean("finished"),
                        resultSet.getString("turn_color")
                ), id);
    }

    public List<GameStatusDto> findStatuses() {
        final String query = "SELECT id, title, finished FROM Game ORDER BY id DESC";
        return jdbcTemplate.query(query,
                (resultSet, rowNum) -> new GameStatusDto(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getBoolean(3)
                ));
    }

    public void update(final GameUpdateDto gameUpdateDto) {
        final String query = "UPDATE Game SET finished=?, turn_color=? WHERE id=?";
        jdbcTemplate.update(query,
                gameUpdateDto.getFinished(),
                gameUpdateDto.getCurrentTurnColor(),
                gameUpdateDto.getId()
        );
    }

    public void remove(final Long id) {
        final String query = "DELETE FROM Game WHERE id=?";
        jdbcTemplate.update(query, id);
    }
}
