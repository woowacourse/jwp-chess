package chess.dao;

import chess.controller.dto.GameDto;
import chess.exception.DataNotFoundException;
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


    public void delete(Long gameId) {
        String query = "DELETE FROM game WHERE game_id = (?)";
        if (jdbcTemplate.update(query, gameId) == 0) {
            throw new DataNotFoundException("해당 Id에 방 번호가 없습니다.");
        }
    }
}