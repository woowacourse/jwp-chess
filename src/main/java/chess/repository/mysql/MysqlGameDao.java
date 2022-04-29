package chess.repository.mysql;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import chess.repository.GameDao;
import chess.repository.dto.game.GameDto;
import chess.repository.dto.game.GameFinishedDto;
import chess.repository.dto.game.GameUpdateDto;

@Component
public class MysqlGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public MysqlGameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
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

    @Override
    public List<GameFinishedDto> findIdAndFinished() {
        final String query = "SELECT id, finished FROM Game ORDER BY id DESC";
        return jdbcTemplate.query(query,
                (resultSet, rowNum) -> new GameFinishedDto(
                        resultSet.getLong(1),
                        resultSet.getBoolean(2)
                ));
    }

    @Override
    public void update(final GameUpdateDto gameUpdateDto) {
        final String query = "UPDATE Game SET finished=?, turn_color=? WHERE id=?";
        jdbcTemplate.update(query,
                gameUpdateDto.getFinished(),
                gameUpdateDto.getCurrentTurnColor(),
                gameUpdateDto.getId()
        );
    }

    @Override
    public void remove(final Long id) {
        final String query = "DELETE FROM Game WHERE id=?";
        jdbcTemplate.update(query, id);
    }
}
