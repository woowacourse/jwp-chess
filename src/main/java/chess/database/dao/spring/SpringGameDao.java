package chess.database.dao.spring;

import chess.database.dao.GameDao;
import chess.database.dto.GameStateDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> readStateAndColor(int roomId) {
        String sql = "select * from game where room_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum)
                    -> List.of(resultSet.getString("state"), resultSet.getString("turn_color")),
                roomId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void create(GameStateDto gameStateDto, int roomId) {
        String sql = "insert into game(room_id, turn_color, state) values (?, ?, ?)";
        jdbcTemplate.update(sql, roomId, gameStateDto.getTurnColor(), gameStateDto.getState());
    }

    @Override
    public void updateState(GameStateDto gameStateDto, int roomId) {
        final String sql = "UPDATE game SET state = ?, turn_color = ? WHERE room_id = ?";
        jdbcTemplate.update(sql, gameStateDto.getState(), gameStateDto.getTurnColor(), roomId);
    }

    @Override
    public void removeGame(int roomId) {
        final String sql = "DELETE FROM game WHERE room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
