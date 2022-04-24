package chess.database.dao.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.database.dao.GameDao;
import chess.database.dto.GameStateDto;

@Repository
public class SpringGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> readStateAndColor(String roomName) {
        String sql = "select * from game where room_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum)
                -> List.of(resultSet.getString("state"), resultSet.getString("turn_color")), roomName);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void saveGame(GameStateDto gameStateDto, String roomName) {
        String sql = "insert into game(room_name, turn_color, state) values (?, ?, ?)";
        jdbcTemplate.update(sql, roomName, gameStateDto.getTurnColor(), gameStateDto.getState());
    }

    @Override
    public void updateState(GameStateDto gameStateDto, String roomName) {
        final String sql = "UPDATE game SET state = ?, turn_color = ? WHERE room_name = ?";
        jdbcTemplate.update(sql, gameStateDto.getState(), gameStateDto.getTurnColor(), roomName);
    }

    @Override
    public void removeGame(String roomName) {
        final String sql = "DELETE FROM game WHERE room_name = ?";
        jdbcTemplate.update(sql, roomName);
    }
}
