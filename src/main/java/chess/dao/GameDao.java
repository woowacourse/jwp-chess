package chess.dao;

import chess.domain.piece.Color;
import chess.dto.LogInDto;
import chess.dto.RoomDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(LogInDto logInDto) {
        final String sql = "insert into game (id, password, turn) values (?, ?, ?)";

        jdbcTemplate.update(sql, logInDto.getGameId(), logInDto.getGamePassword(), Color.BLACK.getName());
    }

    public boolean isInId(String gameId) {
        final String sql = "select count(*) from game where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameId) > 0;
    }

    public boolean findForceEndFlag(String gameId) {
        final String sql = "select force_end_flag from game where id = ?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, gameId);
    }

    public Color findTurn(String gameId) {
        final String sql = "select turn from game where id = ?";

        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                Color.of(resultSet.getString("turn")), gameId);
    }

    public void updateTurn(Color nextTurn, String gameId) {
        final String sql = "update game set turn = ? where id = ?";
        jdbcTemplate.update(sql, nextTurn.getName(), gameId);
    }

    public void updateForceEndFlag(boolean forceEndFlag, String gameId) {
        final String sql = "update game set force_end_flag = ? where id = ?";
        jdbcTemplate.update(sql, forceEndFlag, gameId);
    }

    public void delete(String gameId) {
        final String sql = "delete from game where id = ?";

        jdbcTemplate.update(sql, gameId);
    }

    public boolean isValidPassword(LogInDto logInDto) {
        final String sql = "select count(*) from game where id = ? and password = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, logInDto.getGameId(), logInDto.getGamePassword()) > 0;
    }

    public List<RoomDto> findAllGame() {
        final String sql = "select id, password, force_end_flag from game";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new RoomDto(
                rs.getString("id"),
                rs.getString("password"),
                rs.getBoolean("force_end_flag")
                ));
    }
}
