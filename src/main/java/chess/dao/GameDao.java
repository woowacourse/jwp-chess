package chess.dao;

import chess.controller.dto.GameDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class GameDao {
    private static final RowMapper<GameDto> gameRowWrapper = (resultSet, rowNum) -> new GameDto(
            resultSet.getLong("game_id"),
            resultSet.getString("game_name")
    );

    private final JdbcTemplate jdbcTemplate;


    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(String gameName) {
        String sql = "INSERT INTO game (game_name) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection con) -> {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, gameName);
            return pstmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey())
                      .longValue();
    }

    public GameDto findById(Long id) {
        String sql = "SELECT * FROM game WHERE game_id = (?)";
        return jdbcTemplate.queryForObject(sql, gameRowWrapper, id);
    }

    public List<GameDto> selectAll() {
        String sql = "SELECT * FROM game";
        return jdbcTemplate.query(sql, gameRowWrapper);
    }


    public int delete(Long id) {
        String sql = "DELETE FROM game WHERE game_id = (?)";
        return jdbcTemplate.update(sql, id);
    }
}