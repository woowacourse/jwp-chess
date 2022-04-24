package chess.dao.mysql;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import chess.dao.connect.CustomJdbcTemplate;
import chess.dao.dto.GameDto;
import chess.dao.dto.GameUpdateDto;

@Component
public class GameDao {

    private final CustomJdbcTemplate customJdbcTemplate;

    public GameDao(final CustomJdbcTemplate customJdbcTemplate) {
        this.customJdbcTemplate = customJdbcTemplate;
    }

    public Long save(final GameDto gameDto) {
        final String query = "INSERT INTO Game (player_id1, player_id2, finished, turn_color) VALUES (?,?,?,?)";
        return customJdbcTemplate.executeInsertQuery(query,
                preparedStatement -> {
                    preparedStatement.setLong(1, gameDto.getPlayer_id1());
                    preparedStatement.setLong(2, gameDto.getPlayer_id2());
                    preparedStatement.setBoolean(3, gameDto.getFinished());
                    preparedStatement.setString(4, gameDto.getCurrentTurnColor());
                });
    }

    public GameDto findById(final Long id) {
        final String query = "SELECT id, player_id1, player_id2, finished, turn_color FROM Game WHERE id=?";
        return customJdbcTemplate.executeQuery(query,
                preparedStatement -> preparedStatement.setLong(1, id),
                resultSet -> new GameDto(
                        resultSet.getLong("id"),
                        resultSet.getLong("player_id1"),
                        resultSet.getLong("player_id2"),
                        resultSet.getBoolean("finished"),
                        resultSet.getString("turn_color")
                ));
    }

    public Map<Long, Boolean> findIdAndFinished() {
        final String query = "SELECT id, finished FROM Game ORDER BY id DESC";
        return customJdbcTemplate.executeQuery(query,
                resultSet -> {
                    final Map<Long, Boolean> gameStates = new LinkedHashMap<>();
                    while (resultSet.next()) {
                        final Long gameId = resultSet.getLong("id");
                        final Boolean finished = resultSet.getBoolean("finished");
                        gameStates.put(gameId, finished);
                    }
                    return gameStates;
                });
    }

    public void update(final GameUpdateDto gameUpdateDto) {
        final String query = "UPDATE Game SET finished=?, turn_color=? WHERE id=?";
        customJdbcTemplate.executeQuery(query,
                preparedStatement -> {
                    preparedStatement.setBoolean(1, gameUpdateDto.getFinished());
                    preparedStatement.setString(2, gameUpdateDto.getCurrentTurnColor());
                    preparedStatement.setLong(3, gameUpdateDto.getId());
                });
    }

    public void remove(final long id) {
        final String query = "DELETE FROM Game WHERE id=?";
        customJdbcTemplate.executeQuery(query,
                preparedStatement -> {
                    preparedStatement.setLong(1, id);
                });
    }
}
