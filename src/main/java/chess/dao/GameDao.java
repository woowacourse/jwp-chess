package chess.dao;

import chess.controller.dto.GameDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<GameDto> gameRowWrapper = (resultSet, rowNum) -> new GameDto(
            resultSet.getLong("game_id"),
            resultSet.getString("game_name")
    );

    public long insert(String gameName) {
        String insertGame = "INSERT INTO game (game_name) VALUES (?)";
        String findLastId = "SELECT last_insert_id();";

        jdbcTemplate.update(insertGame, gameName);
        return jdbcTemplate.queryForObject(findLastId, Long.class);
    }

    public List<GameDto> selectAll() {
        String query = "SELECT * FROM game";
        return jdbcTemplate.query(query, gameRowWrapper);
    }


    public int delete(Long gameId) {
        String query = "DELETE FROM game WHERE game_id = (?)";
        return jdbcTemplate.update(query, gameId);
    }
}