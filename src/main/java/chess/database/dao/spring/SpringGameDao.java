package chess.database.dao.spring;

import chess.database.dao.GameDao;
import chess.database.dto.GameStateDto;
import chess.domain.Color;
import chess.domain.game.State;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public GameStateDto readStateAndColor(int roomId) {
        String sql = "select * from game where room_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
            (resultSet, rowNum) -> GameStateDto.of(
                State.valueOf(resultSet.getString("state")),
                Color.valueOf(resultSet.getString("turn_color")))
            , roomId)).orElseThrow(() -> new IllegalArgumentException("[ERROR] 존자하지 않는 게임입니다."));
    }

    @Override
    public void create(GameStateDto gameStateDto, int roomId) {
        String sql = "insert into game(room_id, turn_color, state) values (?, ?, ?)";
        jdbcTemplate.update(sql, roomId, gameStateDto.getTurnColor().name(), gameStateDto.getState().name());
    }

    @Override
    public void updateState(GameStateDto gameStateDto, int roomId) {
        final String sql = "UPDATE game SET state = ?, turn_color = ? WHERE room_id = ?";
        jdbcTemplate.update(sql, gameStateDto.getState().name(), gameStateDto.getTurnColor().name(), roomId);
    }

    @Override
    public void removeGame(int roomId) {
        final String sql = "DELETE FROM game WHERE room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
