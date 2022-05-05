package chess.database.dao;

import chess.database.dto.GameStateDto;
import chess.domain.Color;
import chess.domain.game.GameState;
import chess.domain.game.State;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(int roomId, GameState state) {
        String sql = "insert into game(room_id, turn_color, state) values (?, ?, ?)";
        jdbcTemplate.update(sql, roomId, state.getTurnColor().name(), state.getState().name());
    }

    public GameStateDto readStateAndColor(int roomId) {
        String sql = "select * from game where room_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
            (resultSet, rowNum) -> GameStateDto.of(
                State.valueOf(resultSet.getString("state")),
                Color.valueOf(resultSet.getString("turn_color")))
            , roomId)).orElseThrow(() -> new IllegalArgumentException("[ERROR] 존자하지 않는 게임입니다."));
    }

    public void updateState(int roomId, GameStateDto gameStateDto) {
        final String sql = "UPDATE game SET state = ?, turn_color = ? WHERE room_id = ?";
        jdbcTemplate.update(sql, gameStateDto.getState().name(), gameStateDto.getTurnColor().name(), roomId);
    }

    public void removeGame(int roomId) {
        final String sql = "DELETE FROM game WHERE room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
